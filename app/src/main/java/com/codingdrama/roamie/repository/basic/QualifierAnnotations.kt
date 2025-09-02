package com.codingdrama.roamie.repository.basic

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BindTFDetection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BindMLDetection
