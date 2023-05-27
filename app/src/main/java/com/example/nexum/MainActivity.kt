package com.example.nexum

import android.content.ContentValues.TAG
import android.Manifest
import android.app.*
import android.app.PendingIntent.getActivity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.nexum.adapter.SharedItemAdapter
import com.example.nexum.databinding.ActivityMainBinding
import com.example.nexum.model.LocationModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.*
import java.util.*




class MainActivity : AppCompatActivity() {
    private val tabsFragment = mutableListOf<Fragment>(TabsFragment(),TabsFragment(),TabsFragment(),LocationFragment(),ProfileFragment())
    private var activeFragment=tabsFragment[0]
    init {
        val bundle = listOf(Bundle(),Bundle(),Bundle())
        bundle[0].putString("fragment", "events")
        bundle[1].putString("fragment", "opportunities")
        bundle[2].putString("fragment", "shared")

        for (i in 0..2) {
            tabsFragment[i].arguments = bundle[i]
        }
        activeFragment=tabsFragment[0]
//
//        val sp = getPreferences(MODE_PRIVATE)
//        val gson = Gson()
//        val j = sp.getString("MapNotificationID", null)
//        mapNotificationID = gson.fromJson(j,MutableMap<String, Int>)

    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

//        mapNotificationID = try {
//            val file = File(getDir("data", MODE_PRIVATE), "mapNotificationID")
//            val fos = ObjectInputStream(FileInputStream(file))
////            val fos: FileInputStream = baseContext.openFileInput("mapNotificationID")
//            val os = ObjectInputStream(fos)
//            val map: MutableMap<String, Int> = os.readObject() as MutableMap<String, Int>
//            os.close()
//            fos.close()
//            map
//        } catch (e: Exception) {
//            mutableMapOf()
//        }
//        Log.w(ContentValues.TAG, "${
//            mapNotificationID.size}")
//        set = try {
//            val file = File(getDir("data", MODE_PRIVATE), "setFreeIndex")
//            val fos = ObjectInputStream(FileInputStream(file))
////            val fos: FileInputStream = baseContext.openFileInput("mapNotificationID")
//            val os = ObjectInputStream(fos)
//            val r: MutableSet<Int> = os.readObject() as MutableSet<Int>
//            os.close()
//            fos.close()
//            r
//        } catch (e: Exception) {
//            mutableSetOf(1)
//        }
//        Log.w(ContentValues.TAG, "${
//            set.size}")
        getCurrentLocation()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navbar:BottomNavigationView = findViewById(R.id.bottom_navigation)


//        loadFragment(tabsFragment[0])
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment, tabsFragment[0])
            for(i in 1 until tabsFragment.size)
                add(R.id.fragment, tabsFragment[i]).hide(tabsFragment[i])
        }.commit()

        binding.progressBar.show()
        loadFragment(tabsFragment[0])

        var selectedItem=R.id.event

        navbar.setOnItemSelectedListener {
            selectedItem=it.itemId
            binding.searchView.visibility= GONE
            binding.searchBar.visibility= GONE

            binding.addButton.visibility= VISIBLE
            when (it.itemId) {
                R.id.event -> {
                    loadFragment(tabsFragment[0])
                    true
                }
                R.id.opportunity -> {
                    loadFragment(tabsFragment[1])
                    true
                }
                R.id.shared -> {
                    loadFragment(tabsFragment[2])
                    binding.searchView.visibility= VISIBLE
                    binding.searchBar.visibility= VISIBLE
                    true
                }
                R.id.location -> {
                    binding.addButton.visibility= INVISIBLE
//                    tabsFragment[3]=LocationFragment()
//                    supportFragmentManager.beginTransaction().apply {
//                        add(R.id.fragment, tabsFragment[3]).hide(tabsFragment[3])
//                    }.commit()
                    loadLocation(tabsFragment[3] as LocationFragment)
                    true
                }
                R.id.profile -> {
                    binding.addButton.visibility= INVISIBLE
//                    tabsFragment[3]=LocationFragment()
//                    supportFragmentManager.beginTransaction().apply {
//                        add(R.id.fragment, tabsFragment[3]).hide(tabsFragment[3])
//                    }.commit()
                    loadFragment(tabsFragment[4] as ProfileFragment)
                    true
                }

                else -> {
                    binding.addButton.visibility= INVISIBLE
                    false
                }
            }
        }
        binding.addButton.setOnClickListener {
            when(selectedItem){
                R.id.event->{
                    Intent(this,AddEventActivity::class.java).also {
                        startActivity(it)
                    }
                }
                R.id.opportunity->{
                    Intent(this,AddOpportunityActivity::class.java).also {
                        startActivity(it)
                    }
                }
                R.id.shared->{
                    Intent(this,AddSharedActivity::class.java).also {
                        startActivity(it)
                    }
                }
                else->{

                }
            }

        }
    }

    private fun loadLocation(fragment: LocationFragment)
    {
        fragment.refreshMap()
        loadFragment(fragment)
    }
    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
//        supportFragmentManager.beginTransaction().replace(R.id.fragment,fragment).commit()

    }

    private fun getCurrentLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        val provider = locationManager.getBestProvider(criteria, false)

        if (provider != null) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
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
                        val currentLatLng = LocationModel(LatLng(location.latitude, location.longitude))
                        val auth = Firebase.auth
                        currentLatLng.push(auth.currentUser!!.uid)

                        locationManager.removeUpdates(this)
                    }

                    override fun onProviderDisabled(provider: String) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                })
        }
    }

//    override fun onPause() {
//        super.onPause()
//        val file = File(getDir("data", MODE_PRIVATE), "mapNotificationID")
//        val outputStream = ObjectOutputStream(FileOutputStream(file))
//        outputStream.writeObject(mapNotificationID)
//        outputStream.flush()
//        outputStream.close()
//
//        val fileSet = File(getDir("data", MODE_PRIVATE), "setFreeIndex")
//        val outputStreamSet = ObjectOutputStream(FileOutputStream(fileSet))
//        outputStreamSet.writeObject(set)
//        outputStreamSet.flush()
//        outputStreamSet.close()
//    }
    override fun onBackPressed() {

        val searchView: SearchView = findViewById(R.id.searchView)
        val searchBar: SearchBar = findViewById(R.id.searchBar)
        if (searchView.isShowing) {
            searchView.setText("")
            searchView.hide()
            searchBar.clearText()
            return
        }
        super.onBackPressed()

    }
}
