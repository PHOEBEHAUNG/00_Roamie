package com.codingdrama.roamie.repository

import android.content.Context
import android.graphics.Bitmap
import com.codingdrama.roamie.model.data.BoundingBox
import com.codingdrama.roamie.model.data.DiscoveredObject
import com.codingdrama.roamie.model.data.ObjectCategory
import com.codingdrama.roamie.model.objectdetect.IDetection
import com.codingdrama.roamie.repository.basic.BindTFDetection
import com.google.mlkit.vision.objects.DetectedObject
import org.tensorflow.lite.task.vision.detector.Detection
import javax.inject.Inject

class DetectionRepository @Inject constructor(@BindTFDetection val detector: IDetection) {
    fun detectHighestConfidentObject(context: Context?, bitmap: Bitmap?): DiscoveredObject? {
        val objectList = detector.detect(context, bitmap)
        if (objectList == null || objectList.isEmpty()) return null

        val discoveredObjectList = mutableListOf<DiscoveredObject?>()
        return when (objectList[0]) {
            is Detection -> {
                objectList.forEachIndexed { index, obj ->
                    val discoveredObject = convertDetectionResultToDiscoveredObject(obj as Detection)
                    if (discoveredObject != null) {
                        discoveredObjectList.add(discoveredObject)
                    }
                }
                discoveredObjectList.maxByOrNull { it?.confidence ?: 0f }
            }
            is DetectedObject -> {
                objectList.forEachIndexed { index, obj ->
                    val discoveredObject = convertDetectionResultToDiscoveredObject(obj as DetectedObject)
                    if (discoveredObject != null) {
                        discoveredObjectList.add(discoveredObject)
                    }
                }
                discoveredObjectList.maxByOrNull { it?.confidence ?: 0f }
            }
            else -> null
        }
    }

    private fun convertDetectionResultToDiscoveredObject(detection: Detection?): DiscoveredObject? {
        val box = detection?.boundingBox
        // choose the category with highest confidence
        val bestCategory = detection?.categories?.maxByOrNull { it.score }
        val objCategory = ObjectCategory.fromLabel(bestCategory?.label ?: "Unknown")

        if (objCategory == null || bestCategory == null) { return null }

        return DiscoveredObject(
            id = objCategory.id,
            objectCategory = objCategory,
            confidence = bestCategory.score.times(100),
            boundingBox = BoundingBox(box?.left ?: 0.0f, box?.top ?: 0.0f, box?.right ?: 0.0f, box?.bottom ?: 0.0f)
        )
    }

    private fun convertDetectionResultToDiscoveredObject(detection: DetectedObject?): DiscoveredObject? {
        val box = detection?.boundingBox
        // choose the category with highest confidence
        val bestCategory = detection?.labels?.maxByOrNull { it.confidence }
        val objCategory = ObjectCategory.fromLabel(bestCategory?.text ?: "Unknown")

        if (objCategory == null || bestCategory == null) { return null }

        return DiscoveredObject(
            id = objCategory.id,
            objectCategory = objCategory,
            confidence = bestCategory.confidence.times(100),
            boundingBox = BoundingBox(box?.left?.toFloat() ?: 0.0f, box?.top?.toFloat() ?: 0.0f, box?.right?.toFloat() ?: 0.0f, box?.bottom?.toFloat() ?: 0.0f)
        )
    }
}