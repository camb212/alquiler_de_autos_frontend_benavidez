package com.trackpets.app.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VehicleDto(
    val id: Int? = null,
    val brand: String = "",
    val model: String = "",
    val year: Int = 0,
    val plate: String = "",
    @Json(name = "price_per_day") val pricePerDay: String = "0.00",
    val available: Boolean = true,
    val category: Int? = null,
)
