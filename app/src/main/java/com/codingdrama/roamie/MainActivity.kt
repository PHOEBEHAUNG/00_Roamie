package com.codingdrama.roamie

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.codingdrama.roamie.model.PermissionUtility
import com.codingdrama.roamie.ui.composables.ViewCameraPreview
import com.codingdrama.roamie.ui.theme.RoamieTheme
import com.codingdrama.roamie.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }
    private val viewModel: MainViewModel by viewModels()
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

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            RoamieTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainPage(modifier = Modifier.padding(innerPadding))
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
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (mode.value == MainViewModel.ViewMode.CAMERA) {
                    ViewCameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        onBitmapAvailable = { bitmap ->
                            // do not do heavy work here to avoid blocking the frame update
                            // get bitmap from camera preview
                            // send it ML Kit for processing
                            Log.d("ViewCameraPreview", "Bitmap received: ${bitmap.width}x${bitmap.height}")
                        },
                        onBackClick = { mode.value = MainViewModel.ViewMode.MAIN })
                } else {
                    // A Button show start camera and capture photo or video
                    Button(
                        onClick = { openCamera() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter) // align to bottom center
                            .padding(16.dp)
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

