package com.obriylabs.currencyandroid.presentation.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager

import com.obriylabs.currencyandroid.R
import com.obriylabs.currencyandroid.presentation.ui.base.BaseFragment
import android.support.design.widget.Snackbar
import android.os.Build
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.PermissionChecker.checkCallingOrSelfPermission
import android.view.View
import androidx.navigation.Navigation
import com.obriylabs.currencyandroid.domain.ReceivedData
import com.obriylabs.currencyandroid.domain.entity.ExchangersEntity
import com.obriylabs.currencyandroid.domain.exception.Failure
import com.obriylabs.currencyandroid.extension.failure
import com.obriylabs.currencyandroid.extension.observe
import com.obriylabs.currencyandroid.presentation.viewmodel.StartViewModel


class StartFragment : BaseFragment<StartViewModel>(R.layout.start_fragment) {

    private val permissions: Array<String> = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    )

    companion object {
        const val PERMISSIONS_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = model {
            observe(responseBody, ::changedData)
            observe(listOfExchangers, ::changedData)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isPermissionGranted()) {
            requestPermissionWithRationale()
        } else {
            Navigation.findNavController(view).navigate(R.id.mapsFragment)
        }
    }

    private fun changedData(receivedData: ReceivedData?) {
        viewModel.loadListExchangers()
    }

    private fun changedData(items: List<ExchangersEntity.Result>?) {
        view?.let { Navigation.findNavController(it).navigate(R.id.mapsFragment) }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private fun isPermissionGranted(): Boolean {
        var allowed = true
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        permissions.forEach {
            val res = checkCallingOrSelfPermission(activity as Activity, it)
            allowed = res == PackageManager.PERMISSION_GRANTED
        }

        return allowed
    }

    private fun requestPermissionWithRationale() {
        val permissionStorageStatus = shouldShowRequestPermissionRationale(permissions[0])
        val permissionLocationStatus = shouldShowRequestPermissionRationale(permissions[1])

        if (permissionStorageStatus && permissionLocationStatus) {
            activity?.let {
                Snackbar.make(it.findViewById(R.id.nav_host_fragment), R.string.storage_perm_files_count, Snackbar.LENGTH_LONG)
                        .setAction("GRANT") { requestPerms() }.show()
            }
        } else {
            requestPerms()
        }
    }

    private fun requestPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSIONS_REQUEST)
        }
    }

    /**
     * Handles the result of the request for storage permissions.
     */
    //4
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        var allowed = true

        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()) {
                    // permission granted
                    grantResults.forEach {
                        allowed = allowed && (it == PackageManager.PERMISSION_GRANTED)
                    }
                }
            } // if user not granted permissions.
            else -> allowed = false
        }
        if (allowed){
            //user granted all permissions we can perform our task.
            viewModel.loadDataExchangers() // TODO
        } else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    notify(R.string.storage_perm_denied)
                } else {
                    showNoStoragePermission()
                }
            }
        }
    }

    private fun showNoStoragePermission() {
        activity?.let {
            Snackbar.make(it.findViewById(R.id.nav_host_fragment),R.string.storage_perm_not_granted, Snackbar.LENGTH_LONG)
                    .setAction("SETTINGS") {
                        openApplicationSettings()
                        notify(R.string.open_perm)
                    }.show()
        }
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity?.packageName))
        startActivityForResult(appSettingsIntent, PERMISSIONS_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PERMISSIONS_REQUEST) {
            viewModel.loadDataExchangers() // TODO
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> { notify(R.string.failure_network_connection); close() }
            is Failure.ServerError -> { notify(R.string.failure_server_error); close() }
            is Failure.FileError -> { notify(R.string.failure_file_error); close() }
        }
    }
}
