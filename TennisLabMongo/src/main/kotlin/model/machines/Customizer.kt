package model.machines

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import util.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class Customizer(
    @BsonId @Contextual
    var id: Id<Customizer> = newId<Customizer>(),
    //var uuid : UUID?,
    val brand: String,
    val model: String,
    @Serializable(with = LocalDateSerializer::class)
    val acquisitionDate: LocalDate,
    val available: Boolean,
    val maneuverability: Boolean,
    val balance: Boolean,
    val rigidity: Boolean
) {
}