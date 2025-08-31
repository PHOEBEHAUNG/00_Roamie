package com.codingdrama.roamie

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.codingdrama.roamie.model.PermissionUtility
import com.codingdrama.roamie.ui.composables.ViewCameraPreview
import com.codingdrama.roamie.ui.theme.RoamieTheme
import com.codingdrama.roamie.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias LumaListener = (luma: Double) -> Unit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
    private val viewModel: MainViewModel by viewModels()

    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private lateinit var cameraExecutor: ExecutorService

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in PermissionUtility.REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoamieTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainPage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Request camera permissions
        if (!PermissionUtility.allPermissionsGranted(this)) {
            PermissionUtility.requestPermissions(activityResultLauncher)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    @Composable
    fun MainPage(modifier: Modifier = Modifier) {
        val mode = viewModel.viewMode
        if (mode.value == MainViewModel.ViewMode.CAMERA) {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ViewCameraPreview(
                        modifier = Modifier
                            .fillMaxSize()) { bitmap ->
                                // do not do heavy work here to avoid blocking the frame update
                                // 這裡拿到即時影像 Bitmap
                                // 可以傳給 ML Kit 或做自訂分析
                                Log.d("CameraPreview", "Bitmap received: ${bitmap.width}x${bitmap.height}")
                            }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart) // 固定在底部
                        .padding(16.dp)) {
                        Button(
                            onClick = { mode.value = MainViewModel.ViewMode.MAIN },
                            modifier = Modifier.padding(16.dp)             // 距離邊緣保留空間
                        ) {
                            Text(text = getString(R.string.back))
                        }
                    }
                }
            }
            return
        } else {
            // A Button show start camera and capture photo or video
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = { openCamera() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter) // 固定在底部中間
                            .padding(16.dp) // 與邊界留一點距離
                    ) {
                        Text(text = getString(R.string.open_camera))
                    }
                }
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun GreetingPreview() {
        RoamieTheme {
            MainPage()
        }
    }

    private fun openCamera() {
        viewModel.viewMode.value = MainViewModel.ViewMode.CAMERA
    }
}

