package dto.customers

import kotlinx.serialization.Serializable

@Serializable
data class CustomerDTO(
    val id : Int,
    val name : String,
    val username : String,
    var email : String
)
