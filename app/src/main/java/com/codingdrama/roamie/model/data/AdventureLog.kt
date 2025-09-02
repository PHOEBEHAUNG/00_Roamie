package com.codingdrama.roamie.model.data

data class AdventureLog(
    val user: String,
    val avatarRes: Int,
    val location: String,
    val time: Long,
    val objects: List<DiscoveredObject>,
    val tags: List<String>,
    val likes: Int,
    val comments: Int
)

data class DiscoveredObject(
    val iconRes: Int,
    val name: String,
    val count: Int
)
