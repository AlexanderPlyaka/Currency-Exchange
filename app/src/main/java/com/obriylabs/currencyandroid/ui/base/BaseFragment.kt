package com.obriylabs.currencyandroid.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.obriylabs.currencyandroid.di.Injectable
import javax.inject.Inject

abstract class BaseFragment<VM : ViewModel>(@LayoutRes private val viewId: Int) : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: VM

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        viewModel = model<ViewModel>() as VM
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        // Inflate the layout for this fragment
        inflater.inflate(viewId, container, false)


    private inline fun <reified T : ViewModel> model(): T =
            ViewModelProviders.of(this, viewModelFactory).get(T::class.java)

}