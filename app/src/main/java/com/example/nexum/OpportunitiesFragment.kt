package com.example.nexum

import android.content.ContentValues
import android.os.Bundle
import android.util.EventLog.readEvents
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nexum.adapter.OpportunityItemAdapter
import com.example.nexum.adapter.SharedItemAdapter
import com.example.nexum.firebasefunctions.oppoFromMap
import com.example.nexum.model.Opportunity
import com.example.nexum.model.SharedFile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OpportunitiesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OpportunitiesFragment : Fragment() {

    val dataset= mutableListOf<Opportunity>()
    lateinit var adapter: OpportunityItemAdapter
    lateinit var opportunityRecyclerView: RecyclerView

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        readOppo(param1!!.toInt())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_opportunities, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        opportunityRecyclerView=view.findViewById(R.id.opportunitiesRecyclerView)
        val linearLayoutManager= LinearLayoutManager(this.context)
        opportunityRecyclerView.layoutManager=linearLayoutManager


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
        fun <T> newOppoInstance(param1: String) =
            OpportunitiesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
    private fun readOppo(tabPosition:Int)
    {
        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("opportunities")
        val auth= Firebase.auth
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataset.clear()
                // Get Post object and use the values to update the UI
                for(snapshot in dataSnapshot.children) {
                    val opportunityMap=snapshot.value as Map<String,Any?>
                    val opportunity=oppoFromMap(opportunityMap)
                    if(tabPosition==0 || opportunity.uid==auth.currentUser!!.uid) {
                        dataset.add(opportunity)

                    }
                }
                adapter = OpportunityItemAdapter(dataset)
                opportunityRecyclerView.adapter=adapter

                val addButton: FloatingActionButton =requireActivity().findViewById(R.id.addButton)
                opportunityRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

}