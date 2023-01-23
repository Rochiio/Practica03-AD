package model.orders.tasks

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
 data class Customization(
    @BsonId @Contextual
    val id: Id<Customization> = newId(),
    var weight : Int,
    var balance : Float,
    var stiffness : Int,
    var price : Long,
    var racket_id : String
)
