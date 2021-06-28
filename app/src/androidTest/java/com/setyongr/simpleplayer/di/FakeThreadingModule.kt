package com.setyongr.simpleplayer.di

import com.setyongr.simpleplayer.core.threading.AppDispatcher
import com.setyongr.simpleplayer.fake.FakeAppDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ThreadingModule::class]
)
object FakeThreadingModule {
    @Provides
    fun provideAppDispatcher(): AppDispatcher {
        return FakeAppDispatcher()
    }
}