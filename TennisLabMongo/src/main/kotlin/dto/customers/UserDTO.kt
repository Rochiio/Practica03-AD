package dto.customers

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val address: Address,
    val company: Company,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val username: String,
    val website: String
)