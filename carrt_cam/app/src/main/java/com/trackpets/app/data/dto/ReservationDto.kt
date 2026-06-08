package com.trackpets.app.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReservationDto(
    val id: Int? = null,
    val user: Int? = null,
    val vehicle: Int? = null,
    @Json(name = "start_date") val startDate: String = "",
    @Json(name = "end_date") val endDate: String = "",
    @Json(name = "total_price") val totalPrice: String = "0.00",
    val status: String = "PENDING",
    @Json(name = "created_at") val createdAt: String? = null,
)
