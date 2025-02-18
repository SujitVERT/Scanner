package com.example.docscan.module

import com.example.docscan.detection.ImageProcessor
import com.example.docscan.detection.EdgeDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideEdgeDetector(): EdgeDetector {
        return EdgeDetector()
    }

    @Provides
    @Singleton
    fun provideImageProcessor(): ImageProcessor {
        return ImageProcessor()
    }
}
