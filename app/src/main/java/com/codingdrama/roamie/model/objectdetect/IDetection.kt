package com.codingdrama.roamie.model.objectdetect

import android.content.Context
import android.graphics.Bitmap

interface IDetection {
    fun detect(context: Context?, bitmap: Bitmap?): List<Any?>?
}