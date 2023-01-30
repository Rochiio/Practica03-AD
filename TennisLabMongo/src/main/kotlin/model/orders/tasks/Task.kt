package model.orders.tasks

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.TypeTask
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import util.serializer.UUIDSerializer
import java.util.*

@Serializable
data class Task(
    @BsonId
    var id: String = newId<Task>().toString(),
    var nId: Int = 0,
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    //trabajador y maquina referenciados
    @Contextual
    var idEmployee: String?,

    @Contextual
    var idStringer: String?,
    @Contextual
    var idCustomizer: String?,
    var price: Float,
    //datos de la tarea embedidos
    var description: String,
    var taskType: TypeTask,
    var available: Boolean,
)
