package com.medical.mycompanylocation.datamodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompanyInfo (
        @Json(name="id")val id : Int,
        @Json(name="name")val name : String,
        @Json(name="username")val username : String,
        @Json(name="email")val email : String,
        @Json(name="address") val address : Address,
        @Json(name="phone") val phone : String,
        @Json(name="website") val website : String,
        @Json(name="company") val company : CompanyDetail

)

