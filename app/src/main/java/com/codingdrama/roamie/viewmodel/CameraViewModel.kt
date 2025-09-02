package com.codingdrama.roamie.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.codingdrama.roamie.model.data.DiscoveredObject
import com.codingdrama.roamie.repository.DetectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(private val detectionRepository: DetectionRepository) : ViewModel() {
    companion object {
        const val TAG = "CameraViewModel"
    }

    private var latestDiscoveredObject = mutableStateOf<DiscoveredObject?>(null)
    fun runOneDetection(context: Context, bitmap: Bitmap): DiscoveredObject? {
        val discoveredObject = detectionRepository.detectHighestConfidentObject(context, bitmap)
        if (discoveredObject == null) return null

        if (latestDiscoveredObject.value == null || !latestDiscoveredObject.value?.objectCategory?.name.equals(discoveredObject.objectCategory.name)) {
            latestDiscoveredObject.value = discoveredObject
        }

        return discoveredObject
    }
}