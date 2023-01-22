import db.MongoDbManager
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
    val customer = repo.findByEmail("Sincere@april.biz")
   // println(customers)
    println(customer)
}