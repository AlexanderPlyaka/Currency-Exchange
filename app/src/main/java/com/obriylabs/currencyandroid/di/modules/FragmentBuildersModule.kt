package com.obriylabs.currencyandroid.di.modules

import com.obriylabs.currencyandroid.ui.MapsFragment
import com.obriylabs.currencyandroid.ui.StartFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeStartFragment(): StartFragment

    @ContributesAndroidInjector
    abstract fun contributeMapsFragment(): MapsFragment

}