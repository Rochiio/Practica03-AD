import db.MongoDbManager
import dto.customers.CustomerDTO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.users.Customer
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import repositories.users.CustomerApiRepository
import repositories.users.CustomerRepositoryImpl

fun main(args: Array<String>): Unit = runBlocking {

    val repo = CustomerApiRepository()
    //var customers = repo.findAll(1, 4)
    var customer = repo.delete(1)
   // println(customers)


}