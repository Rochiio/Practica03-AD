package repositories.users

import model.users.Customer
import org.litote.kmongo.Id
import repositories.ICRUD
import java.util.UUID

interface CustomerRepository: ICRUD<Customer, Id<Customer>> {
    suspend fun findByEmail(email : String) : Customer?
    suspend fun findByUuid(uuid: UUID): Customer?
}