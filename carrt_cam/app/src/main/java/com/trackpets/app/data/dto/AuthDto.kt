package com.trackpets.app.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestDto(
    val username: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class TokenResponseDto(
    @Json(name = "token") val token: String? = null,
    @Json(name = "key") val key: String? = null,
    @Json(name = "user") val user: UserDto? = null
)

@JsonClass(generateAdapter = true)
data class ReviewDto(
    val id: Int? = null,
    val vehicle: Int,
    val user: Int? = null,
    val rating: Int,
    val comment: String,
    @Json(name = "created_at") val createdAt: String? = null
)
