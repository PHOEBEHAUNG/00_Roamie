package com.codingdrama.roamie.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codingdrama.roamie.viewmodel.MainViewModel

@Composable
fun PageExplorer(viewModel: MainViewModel = hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { },
            modifier = Modifier.padding(16.dp, 24.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF429EF0))
            ) {
            Text(text = "PageExplorer")
        }
    }
}

@Preview()
@Composable
fun PageExplorerPreview() {
    PageExplorer()
}