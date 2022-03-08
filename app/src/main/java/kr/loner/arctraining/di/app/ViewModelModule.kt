package kr.loner.arctraining.di.app

import kr.loner.arctraining.ui.NavigationViewModel
import kr.loner.arctraining.ui.NavigationViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
interface ViewModelModule {

    @ViewModelScoped
    @get:[Binds]
    val NavigationViewModelImpl.bindNavigationViewModelImpl: NavigationViewModel

}
