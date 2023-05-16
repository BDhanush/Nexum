package com.example.nexum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.nexum.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val tabsFragment = listOf<Fragment>(TabsFragment(),TabsFragment(),TabsFragment(),LocationFragment())
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

    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
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
//                    addFile();
                }
                else->{

                }
            }

        }
    }

//    private fun addFile() {
//        val intent = Intent()
//        intent.type = "/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode== Activity.RESULT_OK && requestCode==SELECT_PICTURE)
//        {
//
//        }
//    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
    }
}
