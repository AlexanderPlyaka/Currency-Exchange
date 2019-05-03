package com.obriylabs.currencyandroid.di.modules

import com.obriylabs.currencyandroid.data.storage.ExchangersFileHandlerImpl
import com.obriylabs.currencyandroid.data.storage.IFileHandler
import com.obriylabs.currencyandroid.presentation.ui.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @Binds
    abstract fun bindFileHandler(dataSource: ExchangersFileHandlerImpl): IFileHandler

}