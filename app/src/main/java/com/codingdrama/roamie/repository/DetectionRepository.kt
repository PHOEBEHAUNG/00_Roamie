package com.codingdrama.roamie.repository

import android.content.Context
import android.graphics.Bitmap
import com.codingdrama.roamie.model.objectdetect.IDetection
import com.codingdrama.roamie.repository.basic.BindTFDetection
import javax.inject.Inject

class DetectionRepository @Inject constructor(@BindTFDetection val detector: IDetection) {
    fun detect(context: Context?, bitmap: Bitmap?) {
        detector.detect(context, bitmap)
    }
}