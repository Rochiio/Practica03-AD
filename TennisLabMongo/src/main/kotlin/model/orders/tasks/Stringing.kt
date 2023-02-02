package model.orders.tasks

import kotlinx.serialization.Serializable
import model.Product
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import util.serializer.UUIDSerializer
import java.util.*
@Serializable
data class Stringing(
    @BsonId
    val id: String = newId<Stringing>().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var hTension : Int,
    var vTension : Int,
    var vString : Product,
    var hString : Product,
    var nKnots : Int,
    var price : Long,
    var racketId : String
)
