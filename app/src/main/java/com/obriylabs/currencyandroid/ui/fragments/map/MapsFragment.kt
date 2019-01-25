package com.obriylabs.currencyandroid.ui.fragments.map

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.location.Location
import android.os.Bundle
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
import com.obriylabs.currencyandroid.ui.base.BaseFragment
import com.obriylabs.currencyandroid.extension.logD
import com.obriylabs.currencyandroid.extension.logE
import javax.inject.Inject

class MapsFragment : BaseFragment(R.layout.maps_fragment), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    // The entry point to the Fused Location Provider.
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var mLastKnownLocation: Location? = null
    private var mCameraPosition: CameraPosition? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MapsViewModel

    companion object {
        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // A default location (Sydney, Australia) and default zoom to use when location permission is
        // not granted.
        private val mDefaultLocation = LatLng(50.449784, 30.523818)
        private const val DEFAULT_ZOOM: Float = 15f
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapsViewModel::class.java)
        // TODO: Use the ViewModel
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
     * Manipulates the map when it's available.
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
     * Gets the current location of the device, and positions the map's camera.
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
                        // Set the map's camera position to the current location of the device.
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
                        this.logE("Exception: %s", task.exception)
                        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM)

                        googleMap?.moveCamera(CameraUpdateFactory
                                .newLatLng(mDefaultLocation))
                        googleMap?.animateCamera(cameraUpdate)
                        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            })
        } catch (e: SecurityException) {
            logE("Exception: %s", e.message)
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private fun updateLocationUI() {
        googleMap ?: return

        try {
            googleMap?.isMyLocationEnabled = true
            googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        } catch (e: SecurityException) {
            logE("Exception: %s", e.message)
        }
    }

}
