package repositories.users

import model.users.Customer
import repositories.ICRUD
import java.util.UUID

interface CustomerRepository: ICRUD<Customer, String> {
    suspend fun findByEmail(email : String) : Customer?
    suspend fun findByUuid(uuid: UUID): Customer?
}