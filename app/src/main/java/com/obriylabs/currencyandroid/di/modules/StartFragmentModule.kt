package com.obriylabs.currencyandroid.di.modules

import android.support.v4.app.Fragment
import com.obriylabs.currencyandroid.di.scopes.FragmentScope
import com.obriylabs.currencyandroid.presentation.ui.start.PermissionManager
import com.obriylabs.currencyandroid.presentation.ui.start.StartFragment
import dagger.Module
import dagger.Provides

@Module
class StartFragmentModule {

    @Provides
    @FragmentScope
    fun fragment(fragment: StartFragment): Fragment = fragment

    @Provides
    @FragmentScope
    fun providePermissionManager(fragment: Fragment): PermissionManager = PermissionManager(fragment)

}