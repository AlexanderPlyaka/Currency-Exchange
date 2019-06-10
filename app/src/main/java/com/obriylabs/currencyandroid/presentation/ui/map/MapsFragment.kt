package com.obriylabs.currencyandroid.presentation.ui.map

import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

import com.obriylabs.currencyandroid.R
import com.obriylabs.currencyandroid.domain.exception.Failure
import com.obriylabs.currencyandroid.extension.failure
import com.obriylabs.currencyandroid.presentation.ui.base.BaseFragment
import com.obriylabs.currencyandroid.extension.logD
import com.obriylabs.currencyandroid.extension.logE
import com.obriylabs.currencyandroid.extension.observe
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.obriylabs.currencyandroid.data.model.Exchangers
import com.google.maps.android.clustering.ClusterManager
import com.obriylabs.currencyandroid.data.model.ExchangersItem
import com.google.android.gms.maps.model.Marker
import com.obriylabs.currencyandroid.presentation.viewmodel.SharedExchangersViewModel
import kotlinx.android.synthetic.main.custom_info_window.view.*
import java.lang.StringBuilder
import java.util.*
import javax.inject.Inject

class MapsFragment : BaseFragment<MapsViewModel>(R.layout.maps_fragment),
        OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<ExchangersItem>,
        ClusterManager.OnClusterItemClickListener<ExchangersItem> {

    @Inject lateinit var sharedViewModel: SharedExchangersViewModel

    private var googleMap: GoogleMap? = null
    // The entry point to the Fused Location Provider.
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var mLastKnownLocation: Location? = null
    private var mCameraPosition: CameraPosition? = null

    private var geocoder: Geocoder? = null
    private var addresses: List<Address>? = null

    // Declare a variable for the cluster manager.
    private var mClusterManager: ClusterManager<ExchangersItem>? = null

    private var exchangers: List<Exchangers>? = null

    private var exchangersItem: ExchangersItem? = null

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
        sharedViewModel = modelActivity {
            observe(observableListExchangers(), ::changedListExchangers)
            failure(failure, ::handleFailure)
        } ?: throw Exception("Invalid Activity")
        viewModel = model {
            observe(observableListExchangers(), ::changedListExchangers)
            failure(failure, ::handleFailure)
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

        geocoder = Geocoder(this.context, Locale.getDefault())

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity as Activity)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment: SupportMapFragment =
                childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        googleMap?.let {
            outState.putParcelable(KEY_CAMERA_POSITION, it.cameraPosition)
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation)
            super.onSaveInstanceState(outState)
        }
    }

    /**
     * Manipulates the map when it'success available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap

        // Initialize the manager with the context and the map.
        mClusterManager = ClusterManager(this.context, googleMap)

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        configureInfoWindowAdapterForMarkers()

        placeMarkers()
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
                                mLastKnownLocation?.let {
                                    LatLng(it.latitude, it.longitude)
                                }, DEFAULT_ZOOM)

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

    private fun configureInfoWindowAdapterForMarkers() {
        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mClusterManager?.markerCollection?.setOnInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                val view = View.inflate(this@MapsFragment.context, R.layout.custom_info_window, null)
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                exchangersItem?.let { addresses = geocoder?.getFromLocation(it.lat, it.lng, 1) }
                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                val lines = addresses?.get(0)?.getAddressLine(0)?.split(",")
                val address = StringBuilder()
                val lineSize = lines?.let { it.size - 3 }
                lineSize?.let {
                    for (i in 0..lineSize) {
                        if (i == lineSize) {
                            address.append(lines[i])
                            break
                        }
                        address.append(lines[i]).append(",")
                    }
                }
                val workingTime = exchangersItem?.workingTime?.split(":")

                view.exchangerAddress.text = address.toString()
                view.exchangerWorkingTime.text = exchangersItem?.workingTime

                return view
            }

            override fun getInfoContents(marker: Marker): View? {
                return null
            }
        })
    }

    private fun placeMarkers() {
        if (exchangers != null) {
            // Point the map's listeners at the listeners implemented by the cluster manager.
            googleMap?.setOnCameraIdleListener(mClusterManager)
            googleMap?.setOnMarkerClickListener(mClusterManager)
            googleMap?.setInfoWindowAdapter(mClusterManager?.markerManager)
            mClusterManager?.setOnClusterClickListener(this)
            mClusterManager?.setOnClusterItemClickListener(this)

            // Add cluster items (markers) to the cluster manager.
            viewModel.readItems(exchangers, mClusterManager)
        }
    }

    override fun onClusterClick(cluster: Cluster<ExchangersItem>?): Boolean {
        // Provide the bounds and time of updating camera position.
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(viewModel.getPositionsOfMarkersFromCluster(cluster), 70))
        return true
    }

    override fun onClusterItemClick(item: ExchangersItem?): Boolean {
        if (item != null) {
            exchangersItem = item
        }
        return false
    }

    private fun changedListExchangers(exchangers: List<Exchangers>?) {
        this.exchangers = exchangers
        if (googleMap != null) {
            placeMarkers()
        }
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
