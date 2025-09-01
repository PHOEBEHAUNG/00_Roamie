package com.codingdrama.roamie.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codingdrama.roamie.viewmodel.MainViewModel

@Composable
fun PageAdventures(viewModel: MainViewModel = hiltViewModel()) {
    Button(
        onClick = { },
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(text = "PageAdventures")
    }
}

@Preview()
@Composable
fun PageAdventuresPreview() {
    PageAdventures()
}