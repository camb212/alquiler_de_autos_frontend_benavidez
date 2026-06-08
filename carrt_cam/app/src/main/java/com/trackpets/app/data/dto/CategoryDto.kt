package com.trackpets.app.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryDto(
    val id: Int? = null,
    val name: String = "",
    val description: String? = null,
)
