package com.obriylabs.currencyandroid.presentation.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.obriylabs.currencyandroid.R
import com.obriylabs.currencyandroid.di.Injectable
import javax.inject.Inject

abstract class BaseFragment<VM : ViewModel>(@LayoutRes private val viewId: Int) : Fragment(), Injectable {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        // Inflate the layout for this fragment
        inflater.inflate(viewId, container, false)

    protected inline fun <reified T : ViewModel> model(body: T.() -> Unit): T {
        val vm = ViewModelProviders.of(this, viewModelFactory).get(T::class.java)
        vm.body()
        return vm
    }

    protected inline fun <reified T : ViewModel> modelActivity(body: T.() -> Unit): T? {
        val vm = activity?.run { ViewModelProviders.of(this, viewModelFactory).get(T::class.java) }
        vm?.body()
        return vm
    }

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    internal fun notify(@StringRes message: Int) =
            activity?.run { Snackbar.make(this.findViewById(R.id.nav_host_fragment), message, Snackbar.LENGTH_SHORT).show() }

    protected fun close() = fragmentManager?.popBackStack()

}