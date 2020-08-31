package com.medical.mycompanylocation.repository

import android.app.Application
import android.net.ConnectivityManager
import android.telecom.ConnectionService
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.medical.mycompanylocation.datamodels.CompanyInfo
import com.medical.mycompanylocation.serviceinterface.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//import com.medical.mycompanylocation.datamodels.CompanyInfo

class MainCompanyRepository(val app: Application){

//    private var companyInfoArraylist: MutableList<CompanyInfo>?=null
////    private var companyInfoMutableLiveData: MutableLiveData<CompanyInfo>
//    private var mutableLiveData = MutableLiveData<List<CompanyInfo>>()

    private  var data = MutableLiveData<List<CompanyInfo>>()
    lateinit var webserviceResponseList: List<CompanyInfo>



    fun getMutableLiveData(): MutableLiveData<List<CompanyInfo>> {
        try {
            if (networkConnection()) {
                val apiService = RetrofitInstance.getCompanyInfoService()
                val call = apiService.companyInfo
                call.enqueue(object : Callback<List<CompanyInfo>> {
                    override fun onResponse(call: Call<List<CompanyInfo>>, response: Response<List<CompanyInfo>>) {
                        if(response.isSuccessful){
                            var mBlogWrapper = response.body()
                            if(mBlogWrapper!=null) {
                                for (i in 0..mBlogWrapper.size step 1) {
                                    Log.e("the", "json data" + response.body())
                                    webserviceResponseList = mBlogWrapper as MutableList<CompanyInfo>
                                    data.value = webserviceResponseList
                                }
                            }
                        }

                    }
                    override fun onFailure(call: Call<List<CompanyInfo>>, t: Throwable) {
                        Toast.makeText(app,"Error in fetching",Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        return data
    }

    @Suppress("DEPRECATION")
    public fun networkConnection():Boolean{
        val connectionManager = app.getSystemService(ConnectionService.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting?:false
    }


}