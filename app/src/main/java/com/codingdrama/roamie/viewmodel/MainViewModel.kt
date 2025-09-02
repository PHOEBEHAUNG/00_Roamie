package com.codingdrama.roamie.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.codingdrama.roamie.R
import com.codingdrama.roamie.model.data.AdventureLog
import com.codingdrama.roamie.model.data.BoundingBox
import com.codingdrama.roamie.model.data.DiscoveredObject
import com.codingdrama.roamie.model.data.ObjectCategory
import com.codingdrama.roamie.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val testRepository: TestRepository) : ViewModel() {
    fun getGreeting() = testRepository.getMessage()
    var adventures: List<AdventureLog> = listOf(
        AdventureLog(
            user = "Emma",
            avatarRes = R.drawable.ic_trophy_24,
            location = "New York, NY",
            time = 1756783016,
            objects = listOf(
                DiscoveredObject(1, ObjectCategory.BASEBALL_BAT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.FIRE_HYDRANT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.AIRPLANE, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                ),
            tags = listOf("UrbanAdventure", "PetSpotting"),
            likes = 21,
            comments = 4
        ),
        AdventureLog(
            user = "Liam",
            avatarRes = R.drawable.ic_trophy_24,
            location = "Paris, France",
            time = 1756783016,
            objects = listOf(
                DiscoveredObject(1, ObjectCategory.BASEBALL_BAT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.FIRE_HYDRANT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.AIRPLANE, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                ),
            tags = listOf("CityWalk"),
            likes = 15,
            comments = 2
        ),
        AdventureLog(
            user = "Liam",
            avatarRes = R.drawable.ic_trophy_24,
            location = "Paris, France",
            time = 1756783016,
            objects = listOf(
                DiscoveredObject(1, ObjectCategory.BASEBALL_BAT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.FIRE_HYDRANT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.AIRPLANE, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
            ),
            tags = listOf("CityWalk"),
            likes = 15,
            comments = 2
        ),
        AdventureLog(
            user = "Liam",
            avatarRes = R.drawable.ic_trophy_24,
            location = "Paris, France",
            time = 1756783016,
            objects = listOf(
                DiscoveredObject(1, ObjectCategory.BASEBALL_BAT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.FIRE_HYDRANT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.AIRPLANE, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
            ),
            tags = listOf("CityWalk"),
            likes = 15,
            comments = 2
        ),
        AdventureLog(
            user = "Liam",
            avatarRes = R.drawable.ic_trophy_24,
            location = "Paris, France",
            time = 1756783016,
            objects = listOf(
                DiscoveredObject(1, ObjectCategory.BASEBALL_BAT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.FIRE_HYDRANT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.AIRPLANE, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
            ),
            tags = listOf("CityWalk"),
            likes = 15,
            comments = 2
        ),
        AdventureLog(
            user = "Liam",
            avatarRes = R.drawable.ic_trophy_24,
            location = "Paris, France",
            time = 1756783016,
            objects = listOf(
                DiscoveredObject(1, ObjectCategory.BASEBALL_BAT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.FIRE_HYDRANT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.AIRPLANE, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
            ),
            tags = listOf("CityWalk"),
            likes = 15,
            comments = 2
        ),
        AdventureLog(
            user = "Liam",
            avatarRes = R.drawable.ic_trophy_24,
            location = "Paris, France",
            time = 1756783016,
            objects = listOf(
                DiscoveredObject(1, ObjectCategory.BASEBALL_BAT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.FIRE_HYDRANT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.AIRPLANE, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
            ),
            tags = listOf("CityWalk"),
            likes = 15,
            comments = 2
        ),
        AdventureLog(
            user = "Liam",
            avatarRes = R.drawable.ic_trophy_24,
            location = "Paris, France",
            time = 1756783016,
            objects = listOf(
                DiscoveredObject(1, ObjectCategory.BASEBALL_BAT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.FIRE_HYDRANT, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
                DiscoveredObject(1, ObjectCategory.AIRPLANE, 2.0f, boundingBox = BoundingBox(1f,1f,1f,1f)),
            ),
            tags = listOf("CityWalk"),
            likes = 15,
            comments = 2
        )
    )

    // create a enum class to define what view mode should be
    enum class ViewMode {
        MAIN, CAMERA
    }

    var viewMode = mutableStateOf(ViewMode.MAIN)
}