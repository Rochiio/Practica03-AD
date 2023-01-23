import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.TypeTask
import model.orders.tasks.Customization
import model.orders.tasks.Task
import util.mappers.toDto

fun main(args: Array<String>): Unit = runBlocking {

    /*val repo = CustomerApiRepository()
    var customers = repo.findAll(1, 100)
    println(customers)

*/

    val c = Customization(
        weight = 1,
        balance = 2f,
        stiffness = 3,
        price = 4L,
        racket_id = "id"
    )
    val json = Json
    val s = json.encodeToString(c)
    val task = Task(
        idTrabajador = null,
        idStringer = null,
        idCustomizer = null,
        description = json.encodeToString(c),
        taskType = TypeTask.PERSONALIZADO,
        available = true
    )
    println(task)
    println("--------------")
    println(task.toDto())

}