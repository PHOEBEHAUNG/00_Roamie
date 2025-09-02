package com.codingdrama.roamie.ui.composables

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.icu.text.SimpleDateFormat
import android.os.Environment
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.codingdrama.roamie.R
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.Locale
import android.graphics.Matrix
import android.graphics.YuvImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import java.io.ByteArrayOutputStream

//@Composable
//fun ViewCameraPreview(
//    modifier: Modifier = Modifier,
//    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
//) {
//    val context = LocalContext.current
//    val cameraController = remember { LifecycleCameraController(context) }
//    AndroidView(
//        modifier = modifier,
//        factory = { ctx ->
//            PreviewView(ctx).apply {
//                controller = cameraController
//            }
//        },
//        update = { previewView ->
//            cameraController.bindToLifecycle(lifecycleOwner)
//            cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//        }
//    )
//}

@Composable
fun PageCameraPreview(
    modifier: Modifier = Modifier,
    isPreview: Boolean = false,
    onBitmapAvailable: (Bitmap) -> Unit,
    onTakePhotoClick: (Bitmap) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    // Torch state
    var isTorchOn by remember { mutableStateOf(false) }

    // Camera object
    var cameraControl: Camera? by remember { mutableStateOf(null) }

    // store latest bitmap for screenshot
    var latestBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // scale animation state
    var isAnimating by remember { mutableStateOf(false) }
    val scaleAnim = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier) {

        if (!isPreview) {
            AndroidView(
                modifier = modifier,
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }
                },
                update = { previewView ->
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        // 1. Preview
                        val preview = androidx.camera.core.Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }

                        // 2. ImageAnalysis
                        val imageAnalyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also { analysisUseCase ->
                                analysisUseCase.setAnalyzer(
                                    ContextCompat.getMainExecutor(context)
                                ) { imageProxy ->
                                    val bitmap = imageProxy.toBitmapCorrectOrientation()
                                    if (bitmap != null) {
                                        latestBitmap = bitmap // store latest bitmap
                                        onBitmapAvailable(bitmap)
                                    }
                                    imageProxy.close()
                                }
                            }

                        // 3. choose camera
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            // 4. lifecycle bind
                            cameraProvider.unbindAll()
                            // notice: what use case to bind must be one or more
                            val camera = cameraProvider.bindToLifecycle(
                                context as LifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalyzer
                            )

                            cameraControl = camera // control camera take/stop video, enableTorch...

                            // initialize torch state
                            cameraControl?.cameraControl?.enableTorch(isTorchOn)
                        } catch (e: Exception) {
                            Log.e("CameraPreview", "Use case binding failed", e)
                        }
                    }, ContextCompat.getMainExecutor(context))
                }
            )

            latestBitmap?.let { bitmap ->
                if (isAnimating) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .scale(scaleAnim.value) // 動態縮放
                            .align(Alignment.Center)
                    )
                }
            }
        }
        // Control buttons
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(16.dp, 16.dp, 16.dp, 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {

//            Text(text = "Torch")
//            Switch(
//                checked = isTorchOn,
//                onCheckedChange = { checked ->
//                    isTorchOn = checked
//                    cameraControl?.cameraControl?.enableTorch(checked)
//                }
//            )

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF429EF0)),
                modifier = Modifier
                    .padding(8.dp, 8.dp, 8.dp, 25.dp),
//                    .fillMaxWidth(0.6f),
                shape = RoundedCornerShape(70.dp),
                onClick = {
                    latestBitmap?.let { bitmap ->
                        isAnimating = true
                        coroutineScope.launch {
                            scaleAnim.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 1000)
                            )
                            onTakePhotoClick.invoke(bitmap)
                            // save bitmap to file
                            saveBitmapToFile(context, bitmap)
                            // reset animation state
                            scaleAnim.snapTo(1f)
                            isAnimating = false
                        }
                    }
                }
            ) {
                Icon(modifier = Modifier
                    .padding(10.dp, 10.dp)
                    .size(42.dp),
                    painter = painterResource(id = R.drawable.ic_camera_enhance_24), contentDescription = context.getString(R.string.take_photo))
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopStart) // align to top start
            .padding(20.dp, 30.dp, 20.dp, 20.dp)
        ){
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Color.White
            ) {
                IconButton(onClick = { onBackClick.invoke() }) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(id = R.drawable.ic_arrow_circle_left_24),
                        contentDescription = context.getString(R.string.back),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PageCameraPreviewPreview() {
    PageCameraPreview(
        modifier = Modifier,
        isPreview = true,
        onBitmapAvailable = {},
        onTakePhotoClick = {},
        onBackClick = {}
    )
}

// save bitmap to file
fun saveBitmapToFile(context: Context, bitmap: Bitmap) {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val fileName = "IMG_$timestamp.jpg"
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File(storageDir, fileName)

    try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        Log.d("CameraPreview", "Saved image: ${file.absolutePath}")
    } catch (e: Exception) {
        Log.e("CameraPreview", "Failed to save image", e)
    }
}

// Extension: convert ImageProxy to Bitmap
//fun ImageProxy.toBitmap(): Bitmap? {
//    val yBuffer = planes[0].buffer
//    val uBuffer = planes[1].buffer
//    val vBuffer = planes[2].buffer
//
//    val ySize = yBuffer.remaining()
//    val uSize = uBuffer.remaining()
//    val vSize = vBuffer.remaining()
//
//    val nv21 = ByteArray(ySize + uSize + vSize)
//
//    yBuffer.get(nv21, 0, ySize)
//    vBuffer.get(nv21, ySize, vSize)
//    uBuffer.get(nv21, ySize + vSize, uSize)
//
//    val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
//    val out = ByteArrayOutputStream()
//    yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, out)
//    val imageBytes = out.toByteArray()
//    return android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//}

fun ImageProxy.toBitmapCorrectOrientation(): Bitmap? {
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, out)
    val imageBytes = out.toByteArray()
    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size) ?: return null

    // rotate bitmap according to imageInfo.rotationDegrees
    val matrix = Matrix()
    matrix.postRotate(this.imageInfo.rotationDegrees.toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
