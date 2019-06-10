package com.obriylabs.currencyandroid.di.modules

import com.obriylabs.currencyandroid.di.scopes.FragmentScope
import com.obriylabs.currencyandroid.presentation.ui.map.MapsFragment
import com.obriylabs.currencyandroid.presentation.ui.start.StartFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector(modules = [(StartFragmentModule::class)])
    @FragmentScope
    abstract fun contributeStartFragment(): StartFragment

    @ContributesAndroidInjector
    abstract fun contributeMapsFragment(): MapsFragment

}