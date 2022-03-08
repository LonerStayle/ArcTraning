package kr.loner.arctraining.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.loner.data.remote.MovieApi
import kr.loner.data.remote.impl.MovieRepositoryImpl
import kr.loner.domain.repository.MovieRepository
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MovieModule {

    @Singleton
    @Provides
    fun provideMovieApi(@Named("retrofit_tmdb") retrofit: Retrofit): MovieApi = retrofit.create()

    @Singleton
    @Provides
    fun provideUpcomingMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository =
        movieRepositoryImpl

}