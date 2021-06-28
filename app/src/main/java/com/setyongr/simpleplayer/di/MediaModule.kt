package com.setyongr.simpleplayer.di

import android.media.MediaPlayer
import com.setyongr.simpleplayer.domain.mediacontrol.MediaController
import com.setyongr.simpleplayer.domain.mediacontrol.MediaControllerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {
    @Provides
    fun provideMediaControl(): MediaController {
        return MediaControllerImpl(mediaPlayer = MediaPlayer())
    }
}