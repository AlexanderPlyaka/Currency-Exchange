package com.obriylabs.currencyandroid.presentation.ui

import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

import com.obriylabs.currencyandroid.R
import com.obriylabs.currencyandroid.domain.exception.Failure
import com.obriylabs.currencyandroid.extension.failure
import com.obriylabs.currencyandroid.presentation.ui.base.BaseFragment
import com.obriylabs.currencyandroid.extension.logD
import com.obriylabs.currencyandroid.extension.logE
import com.obriylabs.currencyandroid.extension.observe
import com.obriylabs.currencyandroid.presentation.viewmodel.MapsViewModel

class MapsFragment : BaseFragment<MapsViewModel>(R.layout.maps_fragment), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    // The entry point to the Fused Location Provider.
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var mLastKnownLocation: Location? = null
    private var mCameraPosition: CameraPosition? = null

    companion object {
        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // A default location (Sydney, Australia) and default zoom to use when location permission is
        // not granted.
        private val mDefaultLocation = LatLng(50.449784, 30.523818)
        private const val DEFAULT_ZOOM: Float = 15f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = model {
            observe(listExchangers()) { changedList() }
            failure(failure, ::handleFailure)
        }
        if (viewModel.listExchangers().value == null) {
            viewModel.getListExchangersFromDb()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View? = super.onCreateView(inflater, container, savedInstanceState)

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
        }

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity as Activity)

        // Build the map.
        val mapFragment: SupportMapFragment =
                childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap?.cameraPosition)
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation)
            super.onSaveInstanceState(outState)
        }
    }

    /**
     * Manipulates the map when it'success available.
     * This callback is triggered when the map is ready to be used.
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }

    /**
     * Gets the current location of the device, and positions the map'success camera.
     */
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationResult = mFusedLocationProviderClient.lastLocation

            locationResult.addOnCompleteListener(activity as Activity, object : OnCompleteListener<Location> {

                override fun onComplete(task: Task<Location?>) {
                    mCameraPosition = googleMap?.cameraPosition

                    if (task.isSuccessful && mCameraPosition != null) {
                        // Set the map'success camera position to the current location of the device.
                        mLastKnownLocation = task.result

                        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                        mLastKnownLocation!!.latitude,
                                        mLastKnownLocation!!.longitude
                                ), DEFAULT_ZOOM)

                        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(mCameraPosition?.target))
                        googleMap?.animateCamera(cameraUpdate)
                    } else {
                        this.logD("Current location is null. Using defaults.")
                        this.logE("Exception: %success", task.exception)
                        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM)

                        googleMap?.moveCamera(CameraUpdateFactory
                                .newLatLng(mDefaultLocation))
                        googleMap?.animateCamera(cameraUpdate)
                        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            })
        } catch (e: SecurityException) {
            logE("Exception: %success", e.message)
        }
    }

    /**
     * Updates the map'success UI settings based on whether the user has granted location permission.
     */
    private fun updateLocationUI() {
        googleMap ?: return

        try {
            googleMap?.isMyLocationEnabled = true
            googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        } catch (e: SecurityException) {
            logE("Exception: %success", e.message)
        }
    }

    private fun changedList() {
        Log.d("asdasd", " ${viewModel.listExchangers().value?.size}") // TODO see if the database is empty
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> { notify(R.string.failure_network_connection); close() }
            is Failure.DatabaseError -> {
                 // TODO
            }
        }
    }

}
