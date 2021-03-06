package com.cutta.kehance.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.cutta.kehance.di.ViewModelFactory
import com.cutta.kehance.di.key.ViewModelKey
import com.cutta.kehance.ui.detail.DetailViewModel
import com.cutta.kehance.ui.main.MainViewModel
import com.cutta.kehance.ui.user.UserDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by CuneytCarikci on 10/01/2018.
 */

@Module
internal abstract class ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    abstract fun provideMainVieWModel(mainViewModel: MainViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(DetailViewModel::class)
    abstract fun provideDetailVieWModel(detailViewModel: DetailViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(UserDetailViewModel::class)
    abstract fun provideUserDetailVieWModel(userDetailViewModel: UserDetailViewModel): ViewModel


    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}