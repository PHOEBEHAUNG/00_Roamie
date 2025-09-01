package com.codingdrama.roamie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.codingdrama.roamie.ui.composables.PageCameraPreview
import com.codingdrama.roamie.ui.theme.RoamieTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraActivity : ComponentActivity() {
    companion object {
        private const val TAG = "CameraActivity"
    }

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
                    },
                    onBackClick = { finish() })
            }
        }
    }
}