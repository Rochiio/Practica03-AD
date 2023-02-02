package dto

import kotlinx.serialization.Serializable

@Serializable
data class SupportDto(
    val url: String?,
    val text: String?
)
