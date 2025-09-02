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
    val id: Int,
    val objectCategory: ObjectCategory,
    val confidence: Float, // score
    val boundingBox: BoundingBox,
) {
    override fun toString(): String {
        return "DiscoveredObject(id=$id, objectCategory=$objectCategory, confidence=$confidence, boundingBox=$boundingBox)"
    }
}

data class BoundingBox(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)