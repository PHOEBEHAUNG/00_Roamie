package com.codingdrama.roamie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.codingdrama.roamie.ui.composables.PageCameraPreview
import com.codingdrama.roamie.ui.theme.RoamieTheme
import com.codingdrama.roamie.viewmodel.CameraViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue

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
                PageCameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onBitmapAvailable = { bitmap ->
                        // do not do heavy work here to avoid blocking the frame update
                        // get bitmap from camera preview
                        // send it ML Kit for processing
//                            Log.d("ViewCameraPreview", "Bitmap received: ${bitmap.width}x${bitmap.height}")

                        // create a coroutine to run detection
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.runDetection(this@CameraActivity, bitmap)
                        }},
                    onBackClick = { finish() })
            }
        }
    }
}