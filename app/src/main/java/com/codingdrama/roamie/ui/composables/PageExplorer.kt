package com.codingdrama.roamie.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codingdrama.roamie.R
import com.codingdrama.roamie.model.data.AdventureLog
import com.codingdrama.roamie.model.data.BoundingBox
import com.codingdrama.roamie.model.data.DiscoveredObject
import com.codingdrama.roamie.model.data.ObjectCategory
import com.codingdrama.roamie.viewmodel.MainViewModel

@Composable
fun PageExplorer(viewModel: MainViewModel = hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(viewModel.adventures) { adventure ->
                AdventureCard(adventure)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdventureCard(adventure: AdventureLog) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F2CE)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = adventure.avatarRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("by ${adventure.user}", fontWeight = FontWeight.Bold)
                        Text(
                            "${adventure.location}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
                Text("${adventure.time}", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Discovered objects
            Text("Discovered a " + adventure.objects[0].objectCategory.emoji, fontWeight = FontWeight.SemiBold)
//            Spacer(modifier = Modifier.height(6.dp))
//            Row {
//                adventure.objects.forEach { obj ->
//                    Row(
//                        modifier = Modifier.padding(end = 12.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            painter = painterResource(id = obj.iconRes),
//                            contentDescription = null,
//                            tint = Color.Unspecified,
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text("x${obj.count}")
//                    }
//                }
//            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tags
            FlowRow (
//                mainAxisSpacing = 8.dp,
//                crossAxisSpacing = 4.dp
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                adventure.tags.forEach { tag ->
                    AssistChip(onClick = { },
                        label = { Text("#$tag") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer interactions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(4.dp))
                Text("${adventure.likes}")
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text("${adventure.comments}")
            }
        }
    }
}

@Preview()
@Composable
fun PageExplorerPreview() {
    PageExplorer()
}

@Preview
@Composable
fun AdventureCardPreview() {
    AdventureCard(
        AdventureLog(
            user = "Emma",
            avatarRes = R.drawable.ic_trophy_24,
            location = "New York, NY",
            time = 1756783016,
            objects = listOf(
                DiscoveredObject(1, ObjectCategory.BASEBALL_BAT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.BASEBALL_BAT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.BASEBALL_BAT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f))
            ),
            tags = listOf("UrbanAdventure", "PetSpotting", "NatureWalk", "CityExploration"),
            likes = 21,
            comments = 4
        )
    )
}