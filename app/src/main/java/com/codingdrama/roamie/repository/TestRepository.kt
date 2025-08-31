package com.codingdrama.roamie.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

// Repository
class TestRepository @Inject constructor() {
    // ...
}

// Module
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideTestRepository(): TestRepository = TestRepository()
}