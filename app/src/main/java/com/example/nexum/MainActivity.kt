package com.example.nexum

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nexum.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

class MainActivity : AppCompatActivity() {
    private val tabsFragment = listOf<Fragment>(TabsFragment(),TabsFragment(),TabsFragment(),LocationFragment())
    private var activeFragment=tabsFragment[0]
    var dataset=mutableListOf<String>()
    init {
        val bundle = listOf(Bundle(),Bundle(),Bundle())
        bundle[0].putString("fragment", "events")
        bundle[1].putString("fragment", "opportunities")
        bundle[2].putString("fragment", "shared")

        for (i in 0..2) {
            tabsFragment[i].arguments = bundle[i]
        }
        activeFragment=tabsFragment[0]

    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataset.add("Srinath")
        dataset.add("Bommi")
        dataset.add("apple")
        dataset.add("apprehension")

        val navbar:BottomNavigationView = findViewById(R.id.bottom_navigation)
        val searchView: SearchView = findViewById(R.id.searchView)
        val searchBar: SearchBar = findViewById(R.id.searchBar)
        searchView.setupWithSearchBar(searchBar)
        searchView.clearFocus()

        searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                // TODO Auto-generated method stub
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun afterTextChanged(s: Editable) {
                // filter your list from your input
                filter(s.toString())
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        })

        searchView.editText.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
                searchBar.text = searchView.text
                searchView.hide()
                false
        }


//        loadFragment(tabsFragment[0])
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment, tabsFragment[0])
            for(i in 1 until tabsFragment.size)
                add(R.id.fragment, tabsFragment[i]).hide(tabsFragment[i])
        }.commit()

        loadFragment(tabsFragment[0])

        var selectedItem=R.id.event

        val add: FloatingActionButton = view.findViewById(R.id.addButton)
        navbar.setOnItemSelectedListener {
            selectedItem=it.itemId

            add.visibility= VISIBLE
            when (it.itemId) {
                R.id.event -> {
                    loadFragment(tabsFragment[0])
                    true
                }
                R.id.opportunities -> {
                    loadFragment(tabsFragment[1])
                    true
                }
                R.id.shared -> {
                    loadFragment(tabsFragment[2])
                    true
                }
                R.id.location -> {
                    add.visibility= INVISIBLE
                    loadFragment(tabsFragment[3])
                    true
                }
                else -> {
                    add.visibility= INVISIBLE
                    false
                }
            }
        }
        add.setOnClickListener {
            when(selectedItem){
                R.id.event->{
                    Intent(this,AddEventActivity::class.java).also {
                        startActivity(it)
                    }
                }
                R.id.opportunities->{
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filter(searchPrefix:String) {
        // creating a new array list to filter our data.
        val filteredList = mutableListOf<String>()

        // running a for loop to compare elements.
        for (item in dataset) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.contains(searchPrefix, true) && searchPrefix.isNotEmpty()) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(item)
            }
        }
        Log.i("check", filteredList.toString())
    }



    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
    }

    override fun onBackPressed() {
        val searchView: SearchView = findViewById(R.id.searchView)
        val searchBar: SearchBar = findViewById(R.id.searchBar)
        if (searchView.isShowing) {
            searchView.hide()
            searchBar.clearText()
            return
        }
        super.onBackPressed()
    }
}
