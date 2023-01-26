package model.orders.tasks

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.TypeTask
import model.machines.Customizer
import model.machines.Stringer
import model.users.Employee
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import util.serializer.UUIDSerializer
import java.util.*

@Serializable
data class Task(
    @BsonId
    var id : String = newId<Task>().toString(),
    var _id : Int,
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    //trabajador y maquina referenciados
    @Contextual
    var idTrabajador : Id<Employee>?,
    @Contextual
    var idStringer : Id<Stringer>?,
    @Contextual
    var idCustomizer : Id<Customizer>?,
    //datos de la tarea embedidos
    var description : String,
    var taskType : TypeTask,
    var available : Boolean
)
