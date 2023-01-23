import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.TypeTask
import model.orders.tasks.Customization
import model.orders.tasks.Task
import repositories.users.CustomerApiRepository


fun main(args: Array<String>): Unit = runBlocking {

    val repo = CustomerApiRepository()
    var customers = repo.findAll(1, 100)
    println(customers)


}