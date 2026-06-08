package com.trackpets.app.domain.model

data class User(
    val id: Int? = null,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val role: String,
    val password: String? = null
)
