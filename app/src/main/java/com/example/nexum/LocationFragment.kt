package com.example.nexum

//import androidx.lifecycle.viewmodel.CreationExtras.Empty.map
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.nexum.adapter.EventItemAdapter
import com.example.nexum.firebasefunctions.eventFromMap
import com.example.nexum.firebasefunctions.locationFromMap
import com.example.nexum.firebasefunctions.userFromMap
import com.example.nexum.model.heatmapData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.squareup.picasso.Picasso


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters


    lateinit var googleMap: GoogleMap
    var heatMap: TileOverlay?=null

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
            childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
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

    override fun onMapReady(p0: GoogleMap) {
        googleMap=p0
        p0.mapType = GoogleMap.MAP_TYPE_HYBRID;
        val northeast = LatLng(17.576182553320635, 78.43914773918324) // Upper right corner
        val southwest = LatLng(17.566954023457843, 78.43072619175284) // Lowerleftcorner

        val builder = LatLngBounds.Builder()

        //add them to builder

        //add them to builder
        builder.include(northeast)
        builder.include(southwest)

        val bounds = builder.build()

        //get width and height to current display screen

        //get width and height to current display screen
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels

        // 20% padding

        // 20% padding
        val padding = (width * 0.20).toInt()


        //set latlong bounds

        //set latlong bounds
        p0.setLatLngBoundsForCameraTarget(bounds)

        //move camera to fill the bound to screen

        //move camera to fill the bound to screen
        p0.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding))

        //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds

        //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
        addHeatMap(p0)
        p0.setMinZoomPreference(p0.getCameraPosition().zoom)
    }
    private fun addHeatMap(googleMap: GoogleMap) {
        var latLngs: MutableList<LatLng> = mutableListOf()

        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("location")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children) {
                    val locationMap=snapshot.value as Map<String,Any?>
                    val location= locationFromMap(locationMap)
                    latLngs.add(location.latLong)

                }
                if(latLngs.isEmpty())
                {
                    return;
                }
                val provider = HeatmapTileProvider.Builder()
                    .data(latLngs)
                    .build()
                heatMap = googleMap.addTileOverlay(TileOverlayOptions().tileProvider(provider))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })


    }
    fun refreshMap()
    {
        heatMap?.remove()
        addHeatMap(googleMap)
    }

}
