package model.machines

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.TypeMachine
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import util.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class Stringer(
    @BsonId @Contextual
    var id: Id<Stringer> = newId<Stringer>(),
    var brand: String,
    var model: String,
    @Serializable(with = LocalDateSerializer::class)
    var acquisitionDate: LocalDate,
    var available: Boolean,
    var automatic: TypeMachine,
    var maximumTension: Int,
    var minimumTension: Int
)