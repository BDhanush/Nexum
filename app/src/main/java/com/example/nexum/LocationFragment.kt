package com.example.nexum

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.example.nexum.model.heatmapData
import com.google.android.gms.maps.CameraUpdateFactory
//import androidx.lifecycle.viewmodel.CreationExtras.Empty.map
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationFragment : Fragment() {
    // TODO: Rename and change types of parameters



    // Request code for location permission
        private val LOCATION_PERMISSION_REQUEST_CODE = 1

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Request location permission
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
            mapFragment?.getMapAsync { googleMap ->
                    getCurrentLocation(googleMap)
                val btnMyLocation = view.findViewById<Button>(R.id.btn_my_location)
                btnMyLocation.setOnClickListener {

                }
            }
        }

        private fun getCurrentLocation(googleMap: GoogleMap?) {
            val locationManager =
                activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            val provider = locationManager.getBestProvider(criteria, false)

            if (provider != null) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                locationManager.requestLocationUpdates(
                    provider,
                    1000,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            // Remove previous marker
                            googleMap?.clear()

                            // Add new marker to current location
                            val currentLatLng = LatLng(location.latitude, location.longitude)
                            googleMap?.addMarker(MarkerOptions().position(currentLatLng))

                            // Center camera on current location
                            // Create a CameraPosition object for the current location
                            val cameraPosition = CameraPosition.Builder()
                                .target(currentLatLng)
                                .zoom(16F)
                                .tilt(30F)
                                .bearing(0F)
                                .build()

// Create a CameraUpdate object from the CameraPosition object
                            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

// Apply the CameraUpdate to the map
                            googleMap?.animateCamera(cameraUpdate)


                            // Stop listening for location updates
                            locationManager.removeUpdates(this)
                        }

                        override fun onProviderDisabled(provider: String) {}
                        override fun onProviderEnabled(provider: String) {}
                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    })
            }
        }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_location, container, false)
    }
    //17.572166866434433, 78.43342470827403
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
//        mapFragment?.getMapAsync { googleMap ->
//            getCurrentLocation(googleMap)
//
//            // Find the button and set an OnClickListener to it
//            val btnMyLocation = view.findViewById<Button>(R.id.btn_my_location)
//            btnMyLocation.setOnClickListener {
//                getCurrentLocation(googleMap)
//            }
//        }
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LocationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                LocationFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}