package com.codingdrama.roamie.repository.basic

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    // you can choose use bind or provide for interface implementation
//    @Binds
//    @BindTFDetection
//    abstract fun bindTFDetection(detection: TFDetection): IDetection
//
//    @Binds
//    @BindMLDetection
//    abstract fun bindMLDetection(detection: TFDetection): IDetection
}