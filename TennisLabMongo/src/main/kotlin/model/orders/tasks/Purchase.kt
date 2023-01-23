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
    @BsonId @Contextual
    val id: Id<Purchase> = newId(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var price : Float,
    var product : Product
)