package com.obriylabs.currencyandroid.presentation.ui.start

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AlertDialog
import com.obriylabs.currencyandroid.R
import com.obriylabs.currencyandroid.di.scopes.FragmentScope
import com.obriylabs.currencyandroid.presentation.viewmodel.SharedExchangersViewModel

@FragmentScope
class PermissionManager(private val fragment: Fragment) {

    private var alertDialog: AlertDialog? = null

    private val permissions: Array<String> = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    )

    companion object {
        const val PERMISSIONS_REQUEST = 1
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    fun isPermissionGranted(): Boolean {
        val allowed: Boolean
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        val storagePermissions = PermissionChecker.checkCallingOrSelfPermission(fragment.activity as Activity, permissions[0])
        val locationPermissions = PermissionChecker.checkCallingOrSelfPermission(fragment.activity as Activity, permissions[1])

        allowed = storagePermissions == PackageManager.PERMISSION_GRANTED && locationPermissions == PackageManager.PERMISSION_GRANTED

        return allowed
    }

    fun requestPermissionWithRationale() {
        val storagePermissions = PermissionChecker.checkCallingOrSelfPermission(fragment.activity as Activity, permissions[0])
        val locationPermissions = PermissionChecker.checkCallingOrSelfPermission(fragment.activity as Activity, permissions[1])
        when {
            ((storagePermissions != PackageManager.PERMISSION_GRANTED) && (locationPermissions != PackageManager.PERMISSION_GRANTED)) -> {
                showDialogNoPermission(R.string.all_perm, R.string.description_of_perm) {
                    requestPerms()
                }
            }
            storagePermissions != PackageManager.PERMISSION_GRANTED -> {
                showDialogNoPermission(R.string.storage_perm, R.string.storage_perm_required) {
                    requestPerms()
                }
            }
            locationPermissions != PackageManager.PERMISSION_GRANTED -> {
                showDialogNoPermission(R.string.location_perm, R.string.location_perm_necessary) {
                    requestPerms()
                }
            }
        }
    }

    private fun requestPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fragment.requestPermissions(permissions, PERMISSIONS_REQUEST)
        }
    }

    fun permissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray, viewModel: SharedExchangersViewModel) {
        var allowed = true
        when(requestCode) {
            PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()) {
                    // permission granted
                    grantResults.forEach {
                        allowed = allowed && (it == PackageManager.PERMISSION_GRANTED)
                    }
                    if (allowed){
                        //user granted all permissions we can perform our task.
                        viewModel.loadDataOfExchangers()
                    } else {
                        // we will give warning to user that they haven't granted permissions.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            when {
                                (fragment.shouldShowRequestPermissionRationale(permissions[0]) &&
                                        fragment.shouldShowRequestPermissionRationale(permissions[1])) -> {
                                    showDialogNoPermission(R.string.perm_not_granted, R.string.description_of_necessity_perm) {
                                        requestPerms()
                                    }
                                }
                                fragment.shouldShowRequestPermissionRationale(permissions[0]) -> {
                                    showDialogNoPermission(R.string.no_storage_perm, R.string.storage_perm_not_granted) {
                                        requestPerms()
                                    }
                                }
                                fragment.shouldShowRequestPermissionRationale(permissions[1]) -> {
                                    showDialogNoPermission(R.string.no_location_perm, R.string.location_perm_not_granted) {
                                        requestPerms()
                                    }
                                }
                                else -> {
                                    showDialogNoPermission(R.string.perm_not_granted, R.string.perm_settings) {
                                        openApplicationSettings()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + fragment.activity?.packageName))
        fragment.startActivityForResult(appSettingsIntent, PERMISSIONS_REQUEST)
    }

    fun activityResult(requestCode: Int, viewModel: SharedExchangersViewModel) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (isPermissionGranted()) {
                viewModel.loadDataOfExchangers()
            } else {
                showDialogNoPermission(R.string.perm_not_granted, R.string.perm_settings) {
                    openApplicationSettings()
                }
            }
            return
        }
    }

    private fun showDialogNoPermission(@StringRes title: Int, @StringRes message: Int, action: () -> Unit) {
        if (alertDialog?.isShowing == true) {
            return // null or dialog already being shown
        }

        alertDialog = AlertDialog.Builder(fragment.activity as Activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    action()
                }
                .setCancelable(false) //to disable outside click for cancel
                .show()
    }
}