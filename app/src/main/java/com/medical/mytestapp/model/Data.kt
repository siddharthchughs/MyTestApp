package com.medical.mytestapp.model

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Data(
    @SerializedName("id") var id: String,
    @SerializedName("cover")var cover: String,
    @SerializedName("title") var title: String,
    @SerializedName("link")var link: String,
    @SerializedName("type")var type: String
)
{
    constructor() : this("", "", "", "", "")
}