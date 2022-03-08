package kr.loner.arctraining.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kr.loner.domain.di.IoDispatcher
import kr.loner.domain.di.MainImmediateDispatcher

@InstallIn(SingletonComponent::class)
@Module
class CoroutinesModule {

    @IoDispatcher
    @Provides
    fun providesIoDispatcher() = Dispatchers.IO

    @MainImmediateDispatcher
    @Provides
    fun providesMainImmediateDispatcher() = Dispatchers.Main.immediate
}