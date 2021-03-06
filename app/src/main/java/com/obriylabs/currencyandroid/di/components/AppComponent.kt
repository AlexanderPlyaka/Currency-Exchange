package com.obriylabs.currencyandroid.di.components

import android.app.Application
import com.obriylabs.currencyandroid.CurrencyExchangeApp
import com.obriylabs.currencyandroid.di.modules.AppModule
import com.obriylabs.currencyandroid.di.modules.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    MainActivityModule::class]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun applicationModule(appModule: AppModule): AppComponent.Builder

        fun build(): AppComponent
    }

    fun inject(currencyExchangeApp: CurrencyExchangeApp)
}