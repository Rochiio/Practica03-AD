import controllers.CustomerController
import exception.CustomerError
import exception.CustomerSuccess
import kotlinx.coroutines.runBlocking
import model.users.Customer
import repositories.users.CustomerRepositoryImpl

fun main(args: Array<String>): Unit = runBlocking {

    var controller = CustomerController(CustomerRepositoryImpl())
    var user = Customer(name = "Prueba", username = "pruebis", email = "prueba@gmail.com", password = "1234",
        available = true, orderList = emptyList(), tennisRacketsList = emptyList())

    controller.addCustomer(user)

    var find = controller.getCustomerByUuid(user.uuid)
    when(find){
        is CustomerError -> println(find.message)
        is CustomerSuccess -> println(find.data)
    }

}