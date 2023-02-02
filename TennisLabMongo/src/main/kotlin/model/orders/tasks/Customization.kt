package model.orders.tasks

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import util.serializer.UUIDSerializer
import java.util.*

@Serializable
 data class Customization(
    @BsonId
    val id: String = newId<Customization>().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var weight : Int,
    var balance : Float,
    var stiffness : Int,
    var price : Long,
    var racket_id : String
)
