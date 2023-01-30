package model.users

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import util.serializer.UUIDSerializer
import java.util.*

@Serializable
data class Customer(
    @BsonId @Contextual
    var id: String = newId<Customer>().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var nId : Int = 0,
    var name: String,
    var username:String,
    var email:String,
    var password:String = "",
    var available: Boolean,
    //Lista de pedidos del cliente
    var orderList: List<String>,
    var tennisRacketsList: List<String>
)