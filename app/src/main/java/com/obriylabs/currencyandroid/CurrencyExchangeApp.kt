package com.obriylabs.currencyandroid

import android.app.Activity
import android.app.Application
import android.content.Context
import com.obriylabs.currencyandroid.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class CurrencyExchangeApp : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        instance = this

        AppInjector.init(this)

        //UCEHandler.Builder(applicationContext).build()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //MultiDex.install(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    companion object {
        @JvmStatic lateinit var instance: CurrencyExchangeApp
            private set
    }

}