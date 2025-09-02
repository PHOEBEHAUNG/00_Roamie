package com.codingdrama.roamie.model.objectdetect

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import javax.inject.Inject
import kotlin.collections.forEachIndexed

class MLDetection @Inject constructor(): IDetection {
    companion object {
        const val TAG = "MLDetection"
        val isDebug = true
    }

    override fun detect(context: Context?, bitmap: Bitmap?) {
        if (context == null || bitmap == null) return

        // Step 1: create ML Kit's InputImage object
        val image = InputImage.fromBitmap(bitmap, 0)
        // Step 2: acquire detector object
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
        val objectDetector = ObjectDetection.getClient(options)
        // Step 3: feed given image to detector and setup callback
        objectDetector.process(image)
            .addOnSuccessListener {
                // Task completed successfully
                if (isDebug) debugPrint(it)
            }
            .addOnFailureListener {
                // Task failed with an exception
                Log.e(TAG, it.message.toString())
            }
    }

    private fun debugPrint(detectedObjects: List<DetectedObject>) {
        detectedObjects.forEachIndexed { index, detectedObject ->
            val box = detectedObject.boundingBox

            Log.d(TAG, "Detected object: $index")
            Log.d(TAG, " trackingId: ${detectedObject.trackingId}")
            Log.d(TAG, " boundingBox: (${box.left}, ${box.top}) - (${box.right},${box.bottom})")
            detectedObject.labels.forEach {
                Log.d(TAG, " categories: ${it.text}")
                Log.d(TAG, " confidence: ${it.confidence}")
            }
        }
    }
}