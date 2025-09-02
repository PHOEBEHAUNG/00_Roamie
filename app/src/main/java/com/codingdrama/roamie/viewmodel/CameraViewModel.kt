package com.codingdrama.roamie.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.codingdrama.roamie.repository.DetectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(private val detectionRepository: DetectionRepository) : ViewModel() {
    fun runDetection(context: Context, bitmap: Bitmap) {
        detectionRepository.detect(context, bitmap)
    }
}