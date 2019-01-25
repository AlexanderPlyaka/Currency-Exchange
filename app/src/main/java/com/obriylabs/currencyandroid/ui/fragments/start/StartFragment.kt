package com.obriylabs.currencyandroid.ui.fragments.start

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.widget.Toast

import com.obriylabs.currencyandroid.R
import com.obriylabs.currencyandroid.ui.base.BaseFragment
import javax.inject.Inject
import android.support.design.widget.Snackbar
import android.os.Build
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v4.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.navigation.Navigation


class StartFragment : BaseFragment(R.layout.start_fragment) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: StartViewModel

    private val permissions: Array<String> = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    )

    companion object {
        const val PERMISSIONS_REQUEST = 1
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionGranted())
            loadData()
        else
            requestPermissionWithRationale()
    }

    private fun loadData() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartViewModel::class.java)
        viewModel.exchangers.observe(this, Observer { repos ->
            if (repos?.data?.isNotEmpty() == true)
                view?.let { Navigation.findNavController(it).navigate(R.id.mapsFragment) }
        })

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

    private fun requestPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    permissions,
                    PERMISSIONS_REQUEST
            )
        }
    }

    /**
     * Handles the result of the request for storage permissions.
     */
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
                    loadData()
                }
            }
            else ->
                // if user not granted permissions.
                allowed = false
        }
        if (allowed){
            //user granted all permissions we can perform our task.
            loadData()
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                    Toast.makeText(activity, "Storage Permissions denied.", Toast.LENGTH_SHORT).show()

                } else {
                    showNoStoragePermission()
                }
            }
        }
    }

    private fun showNoStoragePermission() {
        Snackbar.make(activity!!.findViewById(R.id.nav_host_fragment), "Storage permission isn't granted", Snackbar.LENGTH_LONG)
                .setAction("SETTINGS") {
                    openApplicationSettings()

                    Toast.makeText(activity,
                            "Open Permissions and grant the Storage permission",
                            Toast.LENGTH_SHORT)
                            .show()
                }
                .show()
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity?.packageName))
        startActivityForResult(appSettingsIntent, PERMISSIONS_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PERMISSIONS_REQUEST) {
            loadData()
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun requestPermissionWithRationale() {
        val permissionStorageStatus = shouldShowRequestPermissionRationale(
                permissions[0])
        val permissionLocationStatus = shouldShowRequestPermissionRationale(
                permissions[1])

        if (permissionStorageStatus && permissionLocationStatus) {
            val message = "Storage permission is needed to show files count"
            Snackbar.make(activity!!.findViewById(R.id.nav_host_fragment), message, Snackbar.LENGTH_LONG)
                    .setAction("GRANT") { requestPerms() }
                    .show()
        } else {
            requestPerms()
        }
    }
}
