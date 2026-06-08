package com.trackpets.app.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentDto(
    val id: Int? = null,
    @Json(name = "reservation") val reservation: Int,
    val amount: String = "0.00",
    @Json(name = "payment_method") val paymentMethod: String = "",
    val status: String = "PENDING",
    @Json(name = "transaction_id") val transactionId: String? = null,
    @Json(name = "created_at") val createdAt: String? = null
)
