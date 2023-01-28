package model.lists

import kotlinx.serialization.Serializable
import model.users.Employee
import util.serializer.LocalDateSerializer
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