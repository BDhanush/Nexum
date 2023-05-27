package com.example.nexum

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nexum.adapter.OpportunityItemAdapter
import com.example.nexum.adapter.SharedItemAdapter
import com.example.nexum.firebasefunctions.fileFromMap
import com.example.nexum.model.Opportunity
import com.example.nexum.model.SharedFile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.ktx.auth
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
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
 * Use the [SharedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SharedFragment : Fragment() {

    val dataset= mutableListOf<SharedFile>()
    lateinit var adapter:SharedItemAdapter
    lateinit var sharedRecyclerView: RecyclerView
    lateinit var progressBar: CircularProgressIndicator

    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
        progressBar= requireView().findViewById(R.id.progressBar)
        progressBar.show()
        readShared(param1!!.toInt())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shared, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        sharedRecyclerView=view.findViewById(R.id.sharedRecyclerView)
        val linearLayoutManager= LinearLayoutManager(this.context)
        sharedRecyclerView.layoutManager=linearLayoutManager

        val addButton: FloatingActionButton =requireActivity().findViewById(R.id.addButton)
        sharedRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        val searchView: SearchView = requireActivity().findViewById(R.id.searchView)
        val searchBar: SearchBar = requireActivity().findViewById(R.id.searchBar)
        searchView.setupWithSearchBar(searchBar)
        searchView.clearFocus()

        searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

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
         * @return A new instance of fragment SharedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun <T> newSharedInstance(param1: String) =
            SharedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
    private fun readShared(tabPosition:Int)
    {
        var database = FirebaseDatabase.getInstance("https://nexum-c8155-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("files")
        val auth= Firebase.auth
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataset.clear()
                // Get Post object and use the values to update the UI
                for(snapshot in dataSnapshot.children) {
                    val fileMap=snapshot.value as Map<String,Any?>
                    val file= fileFromMap(fileMap)
                    file.key=snapshot.key as String
                    if(tabPosition==0 || file.uid==auth.currentUser!!.uid) {
                        dataset.add(file)
                    }
                }
                adapter = SharedItemAdapter(dataset)
                progressBar.hide()
                sharedRecyclerView.adapter=adapter

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
        val filteredList = mutableListOf<SharedFile>()

        // running a for loop to compare elements.
        for (item in dataset) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.title!!.contains(searchPrefix, true)) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(item)
            }
        }
        Log.i("check", filteredList.toString())
        adapter = SharedItemAdapter(filteredList)
        sharedRecyclerView.adapter=adapter
    }

}