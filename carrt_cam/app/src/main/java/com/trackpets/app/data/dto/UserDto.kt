package com.trackpets.app.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: Int? = null,
    val username: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "CLIENT",
    // Agregamos el campo password para que el registro funcione
    val password: String? = null
)
