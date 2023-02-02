package com.example.tennislabspring.dto

import kotlinx.serialization.Serializable

@Serializable
data class TaskDTO(
    val completed: Boolean,
    val id: Int,
    val title: String,
    val userId: Int
)