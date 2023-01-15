package model.orders

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.Status
import model.orders.tasks.Task
import model.users.Customer
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import util.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class Order(
    @BsonId @Contextual
    var id: Id<Order> = newId<Order>(),
    var state: Status,
    @Serializable(with = LocalDateSerializer::class)
    val entryDate: LocalDate?,
    @Serializable(with = LocalDateSerializer::class)
    var exitDate: LocalDate?,
    @Serializable(with = LocalDateSerializer::class)
    var finalDate: LocalDate?,
    @Serializable(with = LocalDateSerializer::class)
    val maxDate: LocalDate?,
    var totalPrice: Float,
    val client: Customer,
    var tasks: ArrayList<Task>
)

