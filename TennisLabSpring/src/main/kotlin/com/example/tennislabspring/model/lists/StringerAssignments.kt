package com.example.tennislabspring.model.lists

import com.example.tennislabspring.model.users.Employee
import com.example.tennislabspring.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

/**
 * Clase de asignacion de encordadores a pedidos para la lista.
 */
@Serializable
data class StringerAssignments(
    val idOrder: String,
    val stringer: Employee,
    @Serializable(with= LocalDateSerializer::class)
    val date: LocalDate = LocalDate.now()
) {
}