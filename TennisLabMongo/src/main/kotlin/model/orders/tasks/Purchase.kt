package model.orders.tasks

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.Product
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import util.serializer.UUIDSerializer
import java.util.*

@Serializable
data class Purchase(
    @BsonId
    val id: String = newId<Purchase>().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var price : Float,
    var product : Product
)