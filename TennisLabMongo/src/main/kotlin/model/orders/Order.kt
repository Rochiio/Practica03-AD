package model.orders

import kotlinx.serialization.Serializable
import model.Status
import model.orders.tasks.Task
import model.users.Customer
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import util.serializer.LocalDateSerializer
import util.serializer.UUIDSerializer
import java.time.LocalDate
import java.util.*

@Serializable
data class Order(
    @BsonId
    var id: String = newId<Order>().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
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
    var tasks: List<Task>
)

