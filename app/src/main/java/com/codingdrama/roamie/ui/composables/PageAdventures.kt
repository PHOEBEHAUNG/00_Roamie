package com.codingdrama.roamie.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codingdrama.roamie.R
import com.codingdrama.roamie.viewmodel.MainViewModel

@Composable
fun PageAdventures(viewModel: MainViewModel = hiltViewModel()) {
    val uiState by viewModel.viewMode
    PageAdventuresContent()
}

@Composable
fun PageAdventuresContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.padding(top = 36.dp).size(280.dp),
                painter = painterResource(id = R.drawable.drawable_roamie_robot),
                contentDescription = "Roamie Robot",
            )

            Row (modifier = Modifier.fillMaxWidth().padding(top = 24.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.Center) {
                BadgeCard("Badges", 3, ImageVector.vectorResource(id = R.drawable.ic_trophy_24), onClick = {})
                Spacer(modifier = Modifier.size(24.dp))
                BadgeCard("Trails", 11, ImageVector.vectorResource(id = R.drawable.ic_camera_enhance_24), onClick = {})
            }

            Text(modifier = Modifier.padding(top = 24.dp),
                text = "Embark on a Quest",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold)

            Text(modifier = Modifier.padding(top = 18.dp, start = 40.dp, end = 40.dp),
                text = "Explore, discover, collect, and observe the world around you. \nYour adventure awaits!",
                fontSize = 16.sp,
                textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun BadgeCard(title: String = "Badges", count: Int = 20, imageVector: ImageVector = Icons.Default.Star, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        color = Color(0xFFF5F2CE)
    ) {
        Column(
            modifier = Modifier.padding(20.dp, 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = title,
                    tint = Color.Black
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = "$count",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PageAdventuresPreview() {
    PageAdventuresContent()
}

@Preview
@Composable
fun BadgeCardPreview() {
    BadgeCard("Badges", count = 5, imageVector = Icons.Default.Star, onClick = {})
}