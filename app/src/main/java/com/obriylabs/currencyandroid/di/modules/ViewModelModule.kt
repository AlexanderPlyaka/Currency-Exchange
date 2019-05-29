package com.obriylabs.currencyandroid.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.obriylabs.currencyandroid.di.ViewModelKey
import com.obriylabs.currencyandroid.presentation.ui.map.MapsViewModel
import com.obriylabs.currencyandroid.presentation.viewmodel.SharedExchangersViewModel
import com.obriylabs.currencyandroid.presentation.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SharedExchangersViewModel::class)
    abstract fun bindSharedViewModel(sharedViewModel: SharedExchangersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapsViewModel::class)
    abstract fun bindMapsViewModel(mapsViewModel: MapsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}