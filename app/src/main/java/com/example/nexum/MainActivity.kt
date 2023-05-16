package com.example.nexum

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.play.integrity.internal.f
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    val tabsFragment = listOf<Fragment>(TabsFragment(),TabsFragment(),TabsFragment(),LocationFragment())
    var activeFragment=tabsFragment[0]
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navbar:BottomNavigationView = findViewById(R.id.bottom_navigation)


//        loadFragment(tabsFragment[0])
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment, tabsFragment[0])
            for(i in 1 until tabsFragment.size)
                add(R.id.fragment, tabsFragment[i]).hide(tabsFragment[i])
        }.commit()

        loadFragment(tabsFragment[0])


        navbar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.event -> {
//                    val tabsFragment:Fragment = TabsFragment()
//                    val bundle = Bundle()
//                    bundle.putString("fragment","events")
//                    tabsFragment.arguments = bundle
                    loadFragment(tabsFragment[0])
                    true
                }
                R.id.opportunities -> {
//                    val tabsFragment:Fragment = TabsFragment()
//                    val bundle = Bundle()
//                    bundle.putString("fragment","opportunities")
//                    tabsFragment.arguments = bundle
                    loadFragment(tabsFragment[1])
                    true
                }
                R.id.shared -> {
//                    val tabsFragment:Fragment = TabsFragment()
//                    val bundle = Bundle()
//                    bundle.putString("fragment","shared")
//                    tabsFragment.arguments = bundle
                    loadFragment(tabsFragment[2])
                    true
                }
                R.id.location -> {
                    loadFragment(tabsFragment[3])
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
    }
}
