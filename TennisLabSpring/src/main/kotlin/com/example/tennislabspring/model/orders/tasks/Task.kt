package com.example.tennislabspring.model.orders.tasks

import com.example.tennislabspring.model.TypeTask
import com.example.tennislabspring.model.machines.Customizer
import com.example.tennislabspring.model.machines.Stringer
import com.example.tennislabspring.model.orders.Order
import com.example.tennislabspring.model.users.Employee
import com.example.tennislabspring.serializer.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.ReadOnlyProperty
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.util.*

@Serializable
@Document("task")
data class Task(
    @Id
    var id:String = ObjectId.get().toString(),
    var nId: Int = 0,
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    //trabajador y maquina referenciados
    @Contextual
    @DocumentReference
    var idEmployee: Employee?,
    @Contextual
    @DocumentReference
    var idStringer: Stringer?,
    @Contextual
    @DocumentReference
    var idCustomizer: Customizer?,
    var price: Float,
    //datos de la tarea embedidos
    var description: String,
    var taskType: TypeTask,
    var available: Boolean,
){
    @ReadOnlyProperty
    @DocumentReference(lookup = "{'order':?#{#self._id} }")
    var orders: List<Order>? = null
}
