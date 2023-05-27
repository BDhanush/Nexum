package com.example.nexum

import android.content.ContentValues.TAG
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.nexum.adapter.EventItemAdapter
import com.example.nexum.databinding.ActivityAddEventBinding
import com.example.nexum.databinding.ActivityMainBinding
import com.example.nexum.databinding.FragmentEventBinding
import com.example.nexum.firebasefunctions.eventFromMap
import com.example.nexum.model.Event
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.checkerframework.checker.units.qual.s
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.min

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventFragment : Fragment() {
    var dataset= mutableListOf<Event>()
    lateinit var adapter:EventItemAdapter
    lateinit var eventRecyclerView:RecyclerView
    lateinit var progressBar:CircularProgressIndicator

    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
        progressBar= requireActivity().findViewById(R.id.progressBar)
        progressBar.show()
        readEvents(param1!!.toInt())

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        eventRecyclerView=view.findViewById(R.id.eventRecyclerView)
        val linearLayoutManager= LinearLayoutManager(this.context)
        eventRecyclerView.layoutManager=linearLayoutManager

//        val searchView: SearchView = requireActivity().findViewById(R.id.searchView)
//        val searchBar: SearchBar = requireActivity().findViewById(R.id.searchBar)

        val addButton:FloatingActionButton=requireActivity().findViewById(R.id.addButton)
        eventRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // if the recycler view is scrolled
                // above hide the addButton
                if (dy > 10 && addButton.isShown) {
                    addButton.hide()
//                    searchBar.visibility=View.GONE
//                    searchView.visibility=View.GONE
                }

                // if the recycler view is
                // scrolled above show the addButton
                if (dy < -10 && !addButton.isShown) {
                    addButton.show()
//                    searchBar.visibility=View.VISIBLE
//                    searchView.visibility=View.VISIBLE
                }

                // of the recycler view is at the first
                // item always show the addButton
                if (!recyclerView.canScrollVertically(-1)) {
                    addButton.show()
//                    searchBar.visibility=View.VISIBLE
//                    searchView.visibility=View.VISIBLE
                }
            }
        })

//        searchView.setupWithSearchBar(searchBar)
//        searchView.clearFocus()
//
//        searchView.editText.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // TODO Auto-generated method stub
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//                // TODO Auto-generated method stub
//            }
//
//            @RequiresApi(Build.VERSION_CODES.O)
//            override fun afterTextChanged(s: Editable) {
//                // filter your list from your input
//                filter(s.toString())
//                //you can use runnable postDelayed like 500 ms to delay search text
//            }
//        })

//        searchView.editText.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
//            searchBar.text = searchView.text
//            searchView.hide()
//            false
//        }
    }
    private fun readEvents(tabPosition:Int)
    {
        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("events")
        val auth=Firebase.auth
        database.addValueEventListener(object :ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataset.clear()
                // Get Post object and use the values to update the UI
                for(snapshot in dataSnapshot.children) {
                    val eventMap=snapshot.value as Map<String,Any?>
                    val event=eventFromMap(eventMap)
                    event.key=snapshot.key as String
                    if(tabPosition==0 || event.uid==auth.currentUser!!.uid) {
                        dataset.add(event)
                    }
                }
                sortExpired(dataset)
                adapter = EventItemAdapter(dataset)
                progressBar.hide()
                eventRecyclerView.adapter=adapter



            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filter(searchPrefix:String) {
        // creating a new array list to filter our data.
        val filteredList = mutableListOf<Event>()

        // running a for loop to compare elements.
        for (item in dataset) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.description!!.contains(searchPrefix, true) || item.title!!.contains(searchPrefix, true)) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(item)
            }
        }
        Log.i("check", filteredList.toString())
        adapter = EventItemAdapter(filteredList)
        eventRecyclerView.adapter=adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OpportunitiesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun <T> newEventInstance(param1: String) =
            EventFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    fun sortExpired(list:MutableList<Event>)
    {
        list.sortWith <Event> (object : Comparator <Event> {
            override fun compare (p0: Event, p1: Event) : Int {
                val epoch0=p0.epoch!!
                val epoch1=p1.epoch!!
                val epochCur=System.currentTimeMillis()

                if (max(epoch0,epoch1)<epochCur || min(epoch0,epoch1)>epochCur) {

                    return (if (max(epoch0,epoch1)<epochCur) -1 else 1) * if(epoch0>epoch1)
                        1
                    else if(epoch0<epoch1)
                        -1
                    else
                        0
                }else if (epoch0<epoch1) {
                    return 1
                }else if(epoch1==epoch0){
                    return 0
                }
                return -1
            }
        })

    }

}