package com.example.tennislabspring.model.machines

import com.example.tennislabspring.model.TypeMachine
import com.example.tennislabspring.model.orders.tasks.Stringing
import com.example.tennislabspring.model.orders.tasks.Task
import com.example.tennislabspring.serializer.LocalDateSerializer
import com.example.tennislabspring.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.ReadOnlyProperty
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.LocalDate
import java.util.*

@Serializable
@Document("stringer")
data class Stringer(
    @Id
    var id: String = ObjectId.get().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var brand: String,
    var model: String,
    @Serializable(with = LocalDateSerializer::class)
    var acquisitionDate: LocalDate,
    var available: Boolean,
    var automatic: TypeMachine,
    var maximumTension: Int,
    var minimumTension: Int
){
    @ReadOnlyProperty
    @DocumentReference(lookup = "{'stringing':?#{#self._id} }")
    var stringings: List<Stringing>? = null

    @ReadOnlyProperty
    @DocumentReference(lookup = "{'task':?#{#self._id} }")
    var tasks: List<Task>? = null
}
