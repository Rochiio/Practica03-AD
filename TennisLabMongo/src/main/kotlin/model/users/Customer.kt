package model.users

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import util.serializer.LocalDateTimeSerializer
import util.serializer.UUIDSerializer
import java.time.LocalDateTime
import java.util.*

@Serializable
data class Customer(
    @BsonId @Contextual
    var id: Id<Customer> = newId<Customer>(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var name: String,
    var surname:String,
    var email:String,
    var password:String,
    var available: Boolean,
    //Lista de pedidos del cliente
    var orderList: List<String>, //TODO cambiar a lista de pedidos (ObjectID)
    var tennisRacketsList: List<String> //TODO cambiar a lista de raquetas (ObjectID)
)