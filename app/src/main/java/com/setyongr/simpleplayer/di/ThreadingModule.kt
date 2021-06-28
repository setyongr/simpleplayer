package com.setyongr.simpleplayer.di

import com.setyongr.simpleplayer.core.threading.AppDispatcher
import com.setyongr.simpleplayer.core.threading.AppDispatcherImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ThreadingModule {

    @Provides
    fun provideAppDispatcher(): AppDispatcher {
        return AppDispatcherImpl()
    }
}