package com.medical.mytestapp.ConnectionInterface

import com.medical.mytestapp.Utility.IMGUR_WEB_URL
import com.medical.mytestapp.model.SearchDataModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created :: Siddharth
 */

interface ServicesUtility {
    @get: GET(IMGUR_WEB_URL)
    val searchBlog: Call<SearchDataModel>
}