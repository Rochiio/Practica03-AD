package model.lists

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.orders.Order
import model.users.Employee
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import util.serializer.LocalDateSerializer
import java.time.LocalDate

/**
 * Clase de asignacion de encordadores a pedidos para la lista.
 */
@Serializable
data class StringersAssignments(
    @BsonId @Contextual
    val idOrder: Id<Order>,
    val stringer: Employee,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate = LocalDate.now()
)