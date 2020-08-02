package com.medical.mytestapp.repository

import android.app.Application
import android.net.ConnectivityManager
import android.telecom.ConnectionService
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.medical.mytestapp.ConnectionInterface.RetrofitInstance
import com.medical.mytestapp.model.Data
import com.medical.mytestapp.model.SearchDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SearchRepository(private val application: Application) {

    private var dataItems: ArrayList<Data>? = ArrayList()
    private val mutableLiveData = MutableLiveData<List<Data>?>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getMutableLiveData()
            refreshData()
        }
    }

    fun getMutableLiveData(): MutableLiveData<List<Data>?> {
        try {
            if (networkConnection()) {
                val apiService = RetrofitInstance.getApiService()
                val call = apiService.searchBlog
                call?.enqueue(object : Callback<SearchDataModel?> {
                    override fun onResponse(call: Call<SearchDataModel?>, response: Response<SearchDataModel?>) {
                        val mBlogWrapper = response.body()
                        if (mBlogWrapper != null) {
                            dataItems = mBlogWrapper.data as ArrayList<Data>?
                            mutableLiveData.value = dataItems
                        }
                    }
                    override fun onFailure(call: Call<SearchDataModel?>, t: Throwable) {

                        call.isCanceled
                        Toast.makeText(application,"Error in fetching",Toast.LENGTH_SHORT).show()

                    }
                })
            }
        }
        catch (e: Exception){
         e.printStackTrace()
        }
        return mutableLiveData
    }

    @Suppress("DEPRECATION")
    public fun networkConnection():Boolean{
        val connectionManager = application.getSystemService(ConnectionService.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting?:false
    }

    fun refreshData() {
        CoroutineScope(Dispatchers.IO).launch {
            getMutableLiveData()
        }
    }

}