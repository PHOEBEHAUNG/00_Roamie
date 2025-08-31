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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
fun ViewCameraPreview(
    modifier: Modifier = Modifier,
    onBitmapAvailable: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    // 手電筒狀態
    var isTorchOn by remember { mutableStateOf(false) }

    // Camera 對象，用於控制閃光燈
    var cameraControl: Camera? by remember { mutableStateOf(null) }

    // 保存最新影像，按下截圖時使用
    var latestBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // 用於縮放動畫
    var isAnimating by remember { mutableStateOf(false) }
    val scaleAnim = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier) {
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
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
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
                                    latestBitmap = bitmap // 保存最新影像
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
                        val camera = cameraProvider.bindToLifecycle(
                            context as LifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalyzer
                        )

                        cameraControl = camera // 保存 camera 物件，用於控制閃光燈

                        // 初始化閃光燈狀態
                        camera.cameraControl.enableTorch(isTorchOn)
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

        // 手電筒開關 UI
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(16.dp)) {

//            Text(text = "Torch")
//            Switch(
//                checked = isTorchOn,
//                onCheckedChange = { checked ->
//                    isTorchOn = checked
//                    cameraControl?.cameraControl?.enableTorch(checked)
//                }
//            )

            Button(
                onClick = {
                    latestBitmap?.let { bitmap ->
                        isAnimating = true
                        coroutineScope.launch {
                            scaleAnim.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 1000)
                            )
                            // 縮小完成後儲存檔案
                            saveBitmapToFile(context, bitmap)
                            // 重置動畫
                            scaleAnim.snapTo(1f)
                            isAnimating = false
                        }
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = context.getString(R.string.take_photo))
            }
        }
    }
}

// 🔹 儲存 Bitmap 成 JPEG
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

    // 🔹 旋轉角度
    val matrix = Matrix()
    matrix.postRotate(this.imageInfo.rotationDegrees.toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
