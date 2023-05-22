package com.example.nexum

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nexum.adapter.EventItemAdapter
import com.example.nexum.adapter.OpportunityItemAdapter
import com.example.nexum.adapter.SharedItemAdapter
import com.example.nexum.firebasefunctions.oppoFromMap
import com.example.nexum.model.Event
import com.example.nexum.model.Opportunity
import com.example.nexum.model.SharedFile
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
        readOppo()

        val searchView: SearchView = requireActivity().findViewById(R.id.searchView)
        val searchBar: SearchBar = requireActivity().findViewById(R.id.searchBar)
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
        fun newInstance(param1: String, param2: String) =
            OpportunitiesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun readOppo()
    {
        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("opportunities")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataset.clear()
                // Get Post object and use the values to update the UI
                for(snapshot in dataSnapshot.children) {
                    val opportunityMap=snapshot.value as Map<String,Any?>
                    val opportunity=oppoFromMap(opportunityMap)
                    dataset.add(opportunity)
                }
                adapter = OpportunityItemAdapter(dataset)
                opportunityRecyclerView.adapter=adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filter(searchPrefix:String) {
        // creating a new array list to filter our data.
        val filteredList = mutableListOf<Opportunity>()

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
        adapter = OpportunityItemAdapter(filteredList)
        opportunityRecyclerView.adapter=adapter
    }

}