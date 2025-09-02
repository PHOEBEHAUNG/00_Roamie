package com.codingdrama.roamie.model.objectdetect

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import javax.inject.Inject

class TFDetection @Inject constructor(): IDetection {
    companion object {
        const val TAG = "TFDetection"
        const val isDebug = false
    }

    override fun detect(context: Context?, bitmap: Bitmap?): List<Any?>? {
        if (context == null || bitmap == null) return null
        // create TFLite's TensorImage object
        val image = TensorImage.fromBitmap(bitmap)
        // initialize the detector object
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setMaxResults(5)
            .setScoreThreshold(0.5f)
            .build()
        val detector = ObjectDetector.createFromFileAndOptions(
            context, // the application context
            "detect_model.tflite",
            options
        )
        // feed given image to the model and print the detection result
        val results = detector.detect(image)
        if (isDebug) debugPrint(results)
        return results
    }

    private fun debugPrint(results : List<Detection>) {
        for ((i, obj) in results.withIndex()) {
            val box = obj.boundingBox

            Log.d(TAG, "Detected object: ${i} ")
            Log.d(TAG, "  boundingBox: (${box.left}, ${box.top}) - (${box.right},${box.bottom})")

            for ((j, category) in obj.categories.withIndex()) {
                Log.d(TAG, "    Label $j: ${category.label}")
                val confidence: Int = category.score.times(100).toInt()
                Log.d(TAG, "    Confidence: ${confidence}%")
            }
        }
    }
}