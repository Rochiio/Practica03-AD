package com.example.tennislabspring.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetAllDto<T>(
    val page : Int = 0,
    val per_page : Int? = 0,
    val data : List<T>,
    val support : SupportDto
)
