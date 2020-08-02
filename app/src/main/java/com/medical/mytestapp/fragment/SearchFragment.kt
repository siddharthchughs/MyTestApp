package com.medical.mytestapp.fragment

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.medical.mytestapp.LocalDbManager.DatabaseHelper
import com.medical.mytestapp.LocalDbManager.DatabaseHelperManager
import com.medical.mytestapp.R
import com.medical.mytestapp.Utility.ITEM_SPAN_COUNT_LANDSCAPE
import com.medical.mytestapp.Utility.ITEM_SPAN_COUNT_PORTRAIT
import com.medical.mytestapp.ViewModel.SearchViewModel
import com.medical.mytestapp.adaptermanager.SelectItemAdapter
import com.medical.mytestapp.model.Data
import com.medical.mytestapp.ui.DetailActivity


class SearchFragment : Fragment() , SelectItemAdapter.SelectedClickListener,View.OnClickListener{

    lateinit var searchRecyerliew : RecyclerView
    lateinit var searchAdapter : SelectItemAdapter
    private val RECYCLER_POSITION_KEY = "recycler_position"
    private var mPosition = RecyclerView.NO_POSITION
    private val mBundleState: Bundle? = null
    private var mainViewModel: SearchViewModel? = null
    lateinit var btnRetryConnectionServcie: Button
    lateinit var layoutCheckNetwork: LinearLayout
    var connected: Boolean ?= false
    lateinit var imgNetworkAvaiable: ImageView
    lateinit var imgNetworknotAvaiable: ImageView
    lateinit var imgClear: ImageView
    lateinit var tvc_Networklabel: TextView
    lateinit var btnCheckwifi: Button
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var dh: DatabaseHelper? = null
     var sd: ArrayList<Data>? = null
     var ksd: ArrayList<Data>? = null
    lateinit var editSearch: EditText
    var dt:String?=null
    lateinit var db : DatabaseHelperManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)
//        requireContext().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(SearchViewModel::class.java)
        mainViewModel!!.allBlog.observe(requireActivity(), Observer { blogList ->
            if(blogList!=null){
                showSearchdata(blogList)
                layoutCheckNetwork.visibility = GONE
            }
            else{
                layoutCheckNetwork.visibility = VISIBLE
            }
        })
        Uinitialize(view)
        swipeRefreshLayout.setOnRefreshListener {
            mainViewModel!!.refreshData()
        }
        return view
    }

    fun Uinitialize(v:View){
        searchRecyerliew = v.findViewById(R.id.searchRecycler) as RecyclerView
        btnRetryConnectionServcie = v.findViewById(R.id.btnCheckwifi) as Button
        editSearch = v.findViewById(R.id.editSearch) as EditText
        swipeRefreshLayout = v.findViewById(R.id.refreshItems) as SwipeRefreshLayout
        layoutCheckNetwork = v.findViewById(R.id.layoutCheckNetwork) as LinearLayout
        imgNetworkAvaiable = v.findViewById(R.id.imgNetworkAvailable)
        imgNetworknotAvaiable = v.findViewById(R.id.imgNetworkAvailable)
        imgClear = v.findViewById(R.id.imgClear) as ImageView
        tvc_Networklabel = v.findViewById(R.id.tvc_Networklabel)
        btnCheckwifi = v.findViewById(R.id.btnCheckwifi)
        imgNetworknotAvaiable.visibility = GONE
        layoutCheckNetwork.visibility = GONE
        btnRetryConnectionServcie.setOnClickListener(this)
        imgClear.setOnClickListener(this)
        mainViewModel?.allBlog?.observe(requireActivity(), Observer { blogList ->
            if(blogList!=null){
                showSearchdata(blogList)
                layoutCheckNetwork.visibility = GONE
            }
            else{
                layoutCheckNetwork.visibility = VISIBLE
            }
        })
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editSearch.text!= null) {
                    dt = editSearch.text.toString()
                    searchfrom(dt.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {
                searchAdapter.getFilter().filter(s)
            }
        })

        sd = ArrayList()
        ksd = ArrayList()
        dh = DatabaseHelper(context)
        db = DatabaseHelperManager(requireContext())


    }



    private fun showSearchdata(blogList: List<Data>?) {
        if (blogList != null) {
            sd?.addAll(blogList)
            ksd?.addAll(blogList)
            dh?.addListItem(sd)
            db.addListItem(ksd!!)


            searchAdapter = SelectItemAdapter(requireContext(),blogList, this)
            searchRecyerliew.layoutManager = GridLayoutManager(activity, ITEM_SPAN_COUNT_PORTRAIT)
            searchRecyerliew.itemAnimator = DefaultItemAnimator()
            searchRecyerliew.adapter = searchAdapter
          //  searchRecyerliew.smoothScrollToPosition(0)
            searchRecyerliew.setHasFixedSize(true)
            swipeRefreshLayout.isRefreshing= false
        }

    }



    fun showfromLocal(){
//        ksd?.addAll(db.getAllCotacts)
        searchAdapter = SelectItemAdapter(requireContext(),db.getAllCotacts, this)
        searchRecyerliew.layoutManager = GridLayoutManager(activity, ITEM_SPAN_COUNT_PORTRAIT)
        searchRecyerliew.itemAnimator = DefaultItemAnimator()
        searchRecyerliew.adapter = searchAdapter
        //  searchRecyerliew.smoothScrollToPosition(0)
        searchRecyerliew.setHasFixedSize(true)
        swipeRefreshLayout.isRefreshing= false

    }
    fun searchfrom(data:String){
        searchAdapter = SelectItemAdapter(requireContext(),db.getSearchedLike(data), this)
        searchRecyerliew.layoutManager = GridLayoutManager(activity, ITEM_SPAN_COUNT_PORTRAIT)
        searchRecyerliew.itemAnimator = DefaultItemAnimator()
        searchRecyerliew.adapter = searchAdapter
        searchRecyerliew.setHasFixedSize(true)

    }

    override fun onResume() {
        super.onResume()
        Log.i("on","Local extraction")

        mainViewModel?.allBlog?.observe(requireActivity(), Observer { blogList ->
            if(blogList!=null){
                showSearchdata(blogList)
                layoutCheckNetwork.visibility = GONE
            }
            else{
                layoutCheckNetwork.visibility = VISIBLE
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onSearchImageItemClickEvent(imageSearch : Data) {
        val intent = Intent(requireContext(),DetailActivity::class.java)
        intent.putExtra("title",imageSearch.title)
        intent.putExtra("image",imageSearch.link)
        intent.putExtra("id",imageSearch.id)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }

    fun isOnline(): Boolean? {
        try {
          val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo ?= connectivityManager.getActiveNetworkInfo()
            connected = networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
            return connected as Boolean
        } catch (e: Exception) {
            println("CheckConnectivity Exception: " + e.message)
        }
        return this.connected
    }


    override fun onClick(v: View?) {
        val id = v?.id
        when(id){
            R.id.btnCheckwifi->{
                val i = if (this.isOnline()!!) {
                     imgNetworkAvaiable.visibility = VISIBLE
                    tvc_Networklabel.text = resources.getString(R.string.network_online)
                    btnCheckwifi.text = resources.getString(R.string.connect_label)
                    Toast.makeText(requireContext().applicationContext, "You are online!!!!",Toast.LENGTH_SHORT).show();

                } else {
                    imgNetworknotAvaiable.visibility = GONE
                    tvc_Networklabel.text = resources.getString(R.string.network_check)
                    btnCheckwifi.text = resources.getString(R.string.retry_label)
                    Toast.makeText(requireContext().applicationContext, "You are not online!!!!", Toast.LENGTH_SHORT).show();
                }
            }
            R.id.imgClear->{
                editSearch.setText("")
            }
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            searchRecyerliew.layoutManager = GridLayoutManager(activity, ITEM_SPAN_COUNT_PORTRAIT)
            searchRecyerliew.itemAnimator = DefaultItemAnimator()
            searchRecyerliew.adapter = searchAdapter
            searchRecyerliew.setHasFixedSize(true)
            Toast.makeText(requireContext(), "portrait", Toast.LENGTH_SHORT).show()
        } else {
            if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
                searchRecyerliew.layoutManager =
                    GridLayoutManager(activity, ITEM_SPAN_COUNT_LANDSCAPE)
                searchRecyerliew.itemAnimator = DefaultItemAnimator()
                searchRecyerliew.adapter = searchAdapter
                searchRecyerliew.setHasFixedSize(true)
                Toast.makeText(requireContext(), "landscape", Toast.LENGTH_SHORT).show()

            }
        }
    }

}