package repositories.usuarios

import model.users.Customer
import org.litote.kmongo.Id
import repositories.ICRUD

interface CustomerRepository: ICRUD<Customer, Id<Customer>> {
    suspend fun findByEmail(email : String) : Customer?
}