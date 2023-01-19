import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.users.Customer
import repositories.users.CustomerRepositoryImpl


fun main(args: Array<String>): Unit = runBlocking {
    var user1 = Customer(
        name = "rocio",
        surname = "yo",
        email = "rocio@gmail.com",
        password = "1234",
        available = true,
        orderList = emptyList(),
        tennisRacketsList = emptyList()
    )

    var repositoryImpl = CustomerRepositoryImpl()
    launch {
        var res = repositoryImpl.save(user1)
        res.name = "moha"
        repositoryImpl.update(res)
    }

}