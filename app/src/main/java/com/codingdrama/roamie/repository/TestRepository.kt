package com.codingdrama.roamie.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

class TestRepository @Inject constructor() {
    fun getMessage(): String = "Hello from Repository!"
}
