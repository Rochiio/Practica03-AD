package model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import util.serializer.UUIDSerializer
import java.util.*

@Serializable
//TODO cambiar no se como se mide los 3 ultimos xD
data class Racket(
    @BsonId
    var id: String = newId<Racket>().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var brand: String,
    var model: String,
    var maneuverability: Float,
    var balance: Float,
    var rigidity: Float
) {
}