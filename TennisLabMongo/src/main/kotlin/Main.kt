import controllers.CustomerController
import db.MongoDbManager
import exception.CustomerError
import exception.CustomerSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.users.Customer
import repositories.users.CustomerCacheRepositoryImpl
import repositories.users.CustomerRepositoryImpl
import service.cache.UsersCache
import kotlin.system.exitProcess
//TODO Revisar no para si no pones exitProcess
fun main(args: Array<String>): Unit = runBlocking {
    var controller = CustomerController(CustomerRepositoryImpl(), CustomerCacheRepositoryImpl())

    var refresh = launch {
        UsersCache.refresh()
    }


    var user = Customer(name = "Prueba", username = "pruebis", email = "prueba@gmail.com", password = "1234",
        available = true, orderList = emptyList(), _id = 0, tennisRacketsList = emptyList())

    controller.addCustomer(user)

    var find = controller.getCustomerById(user.id)
    when(find){
        is CustomerError -> println(find.message)
        is CustomerSuccess -> println(find.data)
    }

    refresh.cancel()
}