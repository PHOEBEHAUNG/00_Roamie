package com.codingdrama.roamie.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.codingdrama.roamie.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
//class MainViewModel @Inject constructor(private val testRepository: TestRepository) : ViewModel() {
class MainViewModel : ViewModel() {

    // create a enum class to define what view mode should be
    enum class ViewMode {
        MAIN, CAMERA
    }

    var viewMode = mutableStateOf(ViewMode.MAIN)
}