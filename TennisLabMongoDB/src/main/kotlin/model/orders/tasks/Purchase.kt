package model.orders.tasks

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.Product
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Purchase(
    @BsonId @Contextual
    val id: Id<Purchase> = newId(),
    var price : Float,
    var product : Product
)