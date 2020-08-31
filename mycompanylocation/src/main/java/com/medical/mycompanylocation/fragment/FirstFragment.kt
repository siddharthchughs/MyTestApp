package com.medical.mycompanylocation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medical.mycompanylocation.R
import com.medical.mycompanylocation.adaptersection.CompanyAdapter
import com.medical.mycompanylocation.datamodels.CompanyInfo
import com.medical.mycompanylocation.viewmodel.MainViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() , CompanyAdapter.SelectedClickListener{

    private var mainViewModel : MainViewModel ?= null
    private var companyLocationAdapter: CompanyAdapter?=null
    private  var recyclerView: RecyclerView ?=null
    private val layoutManager : RecyclerView.LayoutManager?=null
    lateinit var textView: TextView
//     lateinit var textview_first:TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_first, container, false)
        recyclerView  = view.findViewById(R.id.recyclerview) as RecyclerView
//        textview_first = view.findViewById(R.id.textview_first) as TextView
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        mainViewModel?.data?.observe(requireActivity(), Observer {

            val companydata = StringBuilder()
            showSearchdata(it)

//            for (extracted in it){
//                Log.i("the","company"+extracted.name)
//                companydata.append(extracted.name)
//           }
         //textview_first.text= companydata
        })
        return view
    }


    private fun showSearchdata(blogList: List<CompanyInfo>?) {
        if (blogList!=null) {
            companyLocationAdapter = CompanyAdapter(requireContext(),blogList,this)
        }
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = companyLocationAdapter
        recyclerView!!.setHasFixedSize(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onSearchImageItemClickEvent(data: CompanyInfo) {

    }

//    override fun onSearchImageItemClickEvent(data: Monster) {
//        TODO("Not yet implemented")
//    }
}
