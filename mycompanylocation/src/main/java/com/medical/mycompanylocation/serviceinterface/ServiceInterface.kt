package com.medical.mycompanylocation.serviceinterface

//import com.medical.mycompanylocation.datamodels.CompanyInfo
import com.medical.mycompanylocation.datamodels.CompanyInfo
import com.medical.mycompanylocation.utilityhelper.GlobalConstants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ServiceInterface {
        @GET(GlobalConstants.USERS_INFO_DATA)
        suspend fun getcompanyInfo(): Response<List<CompanyInfo>>

        @get:GET(GlobalConstants.USERS_INFO_DATA)
        val companyInfo: Call<List<CompanyInfo>>
}
