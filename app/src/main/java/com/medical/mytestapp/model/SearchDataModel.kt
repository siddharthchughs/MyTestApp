package com.medical.mytestapp.model

import com.google.gson.annotations.SerializedName

data class SearchDataModel(@SerializedName("data") val data: List<Data>, val status: Int, val success: Boolean)