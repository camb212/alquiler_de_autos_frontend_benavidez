package com.trackpets.app.domain.model

data class Payment(
    val id: Int? = null,
    val reservationId: Int,
    val amount: Double,
    val paymentMethod: String,
    val status: String,
    val transactionId: String?,
    val createdAt: String? = null
)
