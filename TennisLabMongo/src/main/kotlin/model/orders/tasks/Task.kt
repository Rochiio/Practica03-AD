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

@Serializable
data class Task(
    @BsonId @Contextual
    var id : Id<Task> = newId(),
    //trabajador y maquina referenciados
    var idTrabajador : Id<Employee>?,
    var idStringer : Id<Stringer>?,
    var idCustomizer : Id<Customizer>?,
    //datos de la tarea embedidos
    var description : String,
    var taskType : TypeTask,
    var available : Boolean
)