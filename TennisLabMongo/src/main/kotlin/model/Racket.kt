package model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
@Serializable
//TODO cambiar no se como se mide los 3 ultimos xD
data class Racket(
    @BsonId @Contextual
    var id: Id<Racket> = newId<Racket>(),
    var brand: String,
    var model: String,
    var maneuverability: Float,
    var balance: Float,
    var rigidity: Float
) {
}