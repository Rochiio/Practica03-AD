package model.machines

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import util.serializer.LocalDateSerializer
import util.serializer.UUIDSerializer
import java.time.LocalDate
import java.util.*

@Serializable
data class Customizer(
    @BsonId @Contextual
    var id: Id<Customizer> = newId<Customizer>(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var brand: String,
    var model: String,
    @Serializable(with = LocalDateSerializer::class)
    val acquisitionDate: LocalDate,
    var available: Boolean,
    var maneuverability: Boolean,
    var balance: Boolean,
    var rigidity: Boolean
) {
}