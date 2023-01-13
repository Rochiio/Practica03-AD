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
    val brand: String,
    val model: String,
    @Serializable(with = LocalDateSerializer::class)
    val acquisitionDate: LocalDate,
    val available: Boolean,
    val automatic: TypeMachine,
    val maximumTension: Int,
    val minimumTension: Int
)