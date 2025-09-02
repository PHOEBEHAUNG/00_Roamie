package com.codingdrama.roamie.repository.basic

import com.codingdrama.roamie.model.objectdetect.IDetection
import com.codingdrama.roamie.model.objectdetect.MLDetection
import com.codingdrama.roamie.model.objectdetect.TFDetection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProviderModule {
    // you can choose use bind or provide for interface implementation
    @Provides
    @BindTFDetection
    fun provideTFDetection(): IDetection {
        return TFDetection()
    }

    @Provides
    @BindMLDetection
    fun provideMLDetection(): IDetection {
        return MLDetection()
    }
}