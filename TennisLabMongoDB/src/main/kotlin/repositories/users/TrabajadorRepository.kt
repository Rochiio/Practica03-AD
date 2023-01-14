package repositories.usuarios

import model.users.Employee
import org.litote.kmongo.Id
import repositories.ICRUD


interface TrabajadorRepository: ICRUD<Employee, Id<Employee>> {
    fun findByEmail(email: String): Employee?
}