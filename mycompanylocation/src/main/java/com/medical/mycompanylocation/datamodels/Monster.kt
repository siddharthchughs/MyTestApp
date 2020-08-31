package com.medical.mycompanylocation.datamodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Monster(
    val monsterName: String,
    val caption: String,
    val description: String,
    val price: Double,
    val scariness: Int
)
