package com.example.nexum

import android.content.ContentValues.TAG
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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
    val dataset= mutableListOf<Event>()
    lateinit var adapter:EventItemAdapter
    lateinit var eventRecyclerView:RecyclerView

    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }

        readEvents(param1!!.toInt())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        eventRecyclerView=view.findViewById(R.id.eventRecyclerView)
        val linearLayoutManager= LinearLayoutManager(this.context)
        eventRecyclerView.layoutManager=linearLayoutManager

        val addButton:FloatingActionButton=requireActivity().findViewById(R.id.addButton)
        eventRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // if the recycler view is scrolled
                // above hide the addButton
                if (dy > 10 && addButton.isShown) {
                    addButton.hide()
                }

                // if the recycler view is
                // scrolled above show the addButton
                if (dy < -10 && !addButton.isShown) {
                    addButton.show()
                }

                // of the recycler view is at the first
                // item always show the addButton
                if (!recyclerView.canScrollVertically(-1)) {
                    addButton.show()
                }
            }
        })
    }
    private fun readEvents(tabPosition:Int)
    {
        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("events")
        val auth=Firebase.auth
        database.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataset.clear()
                // Get Post object and use the values to update the UI
                for(snapshot in dataSnapshot.children) {
                    val eventMap=snapshot.value as Map<String,Any?>
                    val event=eventFromMap(eventMap)
                    if(tabPosition==0 || event.uid==auth.currentUser!!.uid) {
                        dataset.add(event)
                    }
                }
                adapter = EventItemAdapter(dataset)
                eventRecyclerView.adapter=adapter



            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
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

}