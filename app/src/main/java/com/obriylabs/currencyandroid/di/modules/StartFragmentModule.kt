package com.obriylabs.currencyandroid.di.modules

import android.support.v4.app.Fragment
import com.obriylabs.currencyandroid.di.scopes.FragmentScope
import com.obriylabs.currencyandroid.presentation.PermissionManager
import com.obriylabs.currencyandroid.presentation.ui.StartFragment
import dagger.Module
import dagger.Provides

@Module
class StartFragmentModule {

    @Provides
    @FragmentScope
    fun fragment(fragment: StartFragment): Fragment = fragment

    @Provides
    @FragmentScope
    fun providePermissionManager(fragment: Fragment): PermissionManager {
        return PermissionManager(fragment)
    }
}