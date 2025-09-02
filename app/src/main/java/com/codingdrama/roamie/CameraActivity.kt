package com.codingdrama.roamie

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.getValue
import kotlin.math.roundToInt
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.core.view.WindowCompat
import com.codingdrama.roamie.model.data.DiscoveredObject
import com.codingdrama.roamie.ui.composables.PageCameraPreview
import com.codingdrama.roamie.ui.theme.RoamieTheme
import com.codingdrama.roamie.viewmodel.CameraViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraActivity : ComponentActivity() {
    companion object {
        private const val TAG = "CameraActivity"
    }

    private val viewModel: CameraViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            RoamieTheme {
                var startAnimation by remember { mutableStateOf(false) }
                var discoveredObject by remember { mutableStateOf<DiscoveredObject?>(null) }
                Box {
                    PageCameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        onBitmapAvailable = { bitmap ->
                            // do not do heavy work here to avoid blocking the frame update
                            // get bitmap from camera preview
                            // send it ML Kit for processing
//                            Log.d("ViewCameraPreview", "Bitmap received: ${bitmap.width}x${bitmap.height}")
                        },
                        onTakePhotoClick = { bitmap ->
                            if (startAnimation) return@PageCameraPreview

                            // create a coroutine to run detection
                            CoroutineScope(Dispatchers.IO).launch {
                                discoveredObject = viewModel.runOneDetection(this@CameraActivity, bitmap)
                                startAnimation = true
                            }
                        },
                        onBackClick = { finish() })

                    if (startAnimation) {
                        EmojiFirecrackerEffect(emojis = listOf(discoveredObject?.objectCategory?.emoji ?: "❓"), onAnimationEnd = {
                            startAnimation = false
                        })
                    }
                }
            }
        }
    }

    @Composable
    fun EmojiScaleIn(discoveredObject: DiscoveredObject?, onAnimationEnd: () -> Unit = {}) {
        var start by remember { mutableStateOf(false) }

        val scale by animateFloatAsState(
            targetValue = if (start) 1f else 3f,
            animationSpec = tween(durationMillis = 1000, easing = EaseOutBack),
            finishedListener = { onAnimationEnd() }
        )

        val rotation by animateFloatAsState(
            targetValue = if (start) 0f else 360f,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = discoveredObject?.objectCategory?.emoji ?: "❓",
                fontSize = (240 * scale).sp,
                modifier = Modifier.graphicsLayer(
                    rotationZ = rotation,
                    scaleX = scale,
                    scaleY = scale
                )
            )
        }

        LaunchedEffect(discoveredObject) {
            start = true
        }
    }

    @Composable
    fun EmojiBounceAcross(discoveredObject: DiscoveredObject?, onAnimationEnd: () -> Unit = {}) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val animatableX = remember { Animatable(-100f) }

        LaunchedEffect(discoveredObject) {
            animatableX.animateTo(
                targetValue = screenWidth.value + 600f,
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = { OvershootInterpolator(2f).getInterpolation(it) }
                )
            )
            onAnimationEnd()
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Text(
                text = discoveredObject?.objectCategory?.emoji ?: "❓",
                fontSize = 75.sp,
                modifier = Modifier
                    .offset { IntOffset(animatableX.value.roundToInt(), 200) }
            )
        }
    }

    data class EmojiParticle(
        val emoji: String,
        val x: Float,
        val y: Float,
        val dx: Float,
        val dy: Float,
        val size: Float,
        val lifetime: Int
    )

    @Composable
    fun EmojiFirecrackerEffect(
        modifier: Modifier = Modifier,
        emojis: List<String> = listOf("🎉", "✨", "💥", "🎊", "🔥"),
        particleCount: Int = 40,
        onAnimationEnd: () -> Unit = {}
    ) {
        var particles by remember { mutableStateOf(listOf<EmojiParticle>()) }

        // emitter: periodically generate a batch of particles
        LaunchedEffect(Unit) {
            var i = 1
            while (i > 0) {
                i--
                val startX = 0.45f
                val startY = 0.9f

                particles = List(particleCount) {
                    val angle = Random.nextDouble(Math.PI / 6, 5 * Math.PI / 6) // 30° ~ 150°
                    val speed = Random.nextDouble(2.0, 50.0)
                    EmojiParticle(
                        emoji = emojis.random(),
                        x = startX,
                        y = startY,
                        dx = (speed * kotlin.math.cos(angle)).toFloat(),
                        dy = (-speed * kotlin.math.sin(angle)).toFloat(),
                        size = Random.nextFloat() * 32f + 24f, // emoji 大小
                        lifetime = Random.nextInt(40, 100)
                    )
                }
                delay(1200) // trigger every 1.2 seconds
            }
            onAnimationEnd.invoke()
        }

        // Animation loop (simulate 60fps update)
        val infiniteTransition = rememberInfiniteTransition(label = "emoji-firecracker")
        val tick by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(16),
                repeatMode = RepeatMode.Restart
            ), label = "tick"
        )

        Canvas(modifier = modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            particles = particles.mapNotNull { p ->
                if (p.lifetime <= 0) null
                else p.copy(
                    x = p.x + p.dx / width,
                    y = p.y + p.dy / height,
                    dy = p.dy + 0.1f, // 重力
                    lifetime = p.lifetime - 1
                )
            }

            drawIntoCanvas { canvas ->
                particles.forEach { p ->
                    val alpha = p.lifetime / 100f
                    val paint = android.graphics.Paint().apply {
                        textSize = p.size.sp.toPx()
                        this.alpha = (alpha * 255).toInt()
                    }
                    canvas.nativeCanvas.drawText(
                        p.emoji,
                        p.x * width,
                        p.y * height,
                        paint
                    )
                }
            }
        }
    }

    @Composable
    fun EmojiFirecrackerDemo() {
        Box(modifier = Modifier.fillMaxSize()) {
            EmojiFirecrackerEffect(
                emojis = listOf("🎉", "✨", "💥", "🪄", "⭐️", "🔮")
            )
        }
    }

    @Preview
    @Composable
    fun EmojiFirecrackerPreview() {
        EmojiFirecrackerDemo()
    }
}