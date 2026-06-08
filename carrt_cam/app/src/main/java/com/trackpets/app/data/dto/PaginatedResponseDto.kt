package com.trackpets.app.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaginatedResponseDto<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)
