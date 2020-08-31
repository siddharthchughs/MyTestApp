package com.medical.mycompanylocation.repository

//import com.medical.mycompanylocation.datamodels.CompanyInfo
import android.app.Application
import android.net.ConnectivityManager
import android.telecom.ConnectionService
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.medical.mycompanylocation.R
import com.medical.mycompanylocation.Utility.MainHelper
import com.medical.mycompanylocation.datamodels.CompanyInfo
import com.medical.mycompanylocation.serviceinterface.ServiceInterface
import com.medical.mycompanylocation.utilityhelper.GlobalConstants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RepositoryCompanyMoshi(val app: Application) {

    var companyInfomutable= MutableLiveData<List<CompanyInfo>>()
    val listType = Types.newParameterizedType(List::class.java,CompanyInfo::class.java)

    init {
        parseText()
//        CoroutineScope(Dispatchers.IO).launch {
////            getParsedData()
//       //     parseText()
//        }

        Log.i("the","network availability"+ "${networkConnection()}")
    }

    @WorkerThread
    suspend fun getParsedData(){
      if (networkConnection()){
       val retrofit = Retrofit.Builder()
           .baseUrl(GlobalConstants.BASE_URL)
           .addConverterFactory(MoshiConverterFactory.create())
           .build()
          val service  = retrofit.create(ServiceInterface::class.java)
          val serviceData = service.getcompanyInfo().body()?: emptyList()
          companyInfomutable.postValue(serviceData)
      }
    }

    @Suppress("DEPRECATION")
    public fun networkConnection():Boolean{
        val connectionManager = app.getSystemService(ConnectionService.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting?:false
    }

    fun parseText(){
        val text = MainHelper.getTextFromResource(app, R.raw.company_data)
        val data = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter : JsonAdapter<List<CompanyInfo>> = data.adapter(listType)
        companyInfomutable.value =  adapter.fromJson(text)?: emptyList()
    }
}