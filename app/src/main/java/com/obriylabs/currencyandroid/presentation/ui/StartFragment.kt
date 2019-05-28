package com.obriylabs.currencyandroid.presentation.ui

import com.obriylabs.currencyandroid.R
import com.obriylabs.currencyandroid.presentation.ui.base.BaseFragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.obriylabs.currencyandroid.data.DateEquals
import com.obriylabs.currencyandroid.domain.exception.Failure
import com.obriylabs.currencyandroid.extension.failure
import com.obriylabs.currencyandroid.extension.observe
import com.obriylabs.currencyandroid.presentation.PermissionManager
import com.obriylabs.currencyandroid.presentation.viewmodel.SharedViewModel
import javax.inject.Inject

class StartFragment : BaseFragment<SharedViewModel>(R.layout.start_fragment) {

    @Inject lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = modelActivity {
            observe(observableResponse()) { changedData() }
            failure(failure, ::handleFailure)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (permissionManager.isPermissionGranted()) {
            viewModel.loadDataOfExchangers()
        } else {
            permissionManager.requestPermissionWithRationale()
        }
    }

    /**
     * Handles the result of the request for permissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionManager.permissionsResult(requestCode, permissions, grantResults, viewModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        permissionManager.activityResult(requestCode, viewModel)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun changedData() {
        viewModel.loadListExchangers()
        NavHostFragment.findNavController(this).navigate(R.id.mapsFragment)
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> { notify(R.string.failure_network_connection); close() }
            is Failure.ServerError -> { notify(R.string.failure_server_error); close() }
            is Failure.FileError -> { notify(R.string.failure_file_error); close() }
            is Failure.DatabaseError -> {
                // TODO
            }
            is DateEquals -> {
                NavHostFragment.findNavController(this).navigate(R.id.mapsFragment)
            }
        }
    }
}
