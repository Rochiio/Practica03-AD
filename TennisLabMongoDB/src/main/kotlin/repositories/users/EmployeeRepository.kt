package repositories.usuarios

import model.users.Employee
import org.litote.kmongo.Id
import repositories.ICRUD


interface EmployeeRepository: ICRUD<Employee, Id<Employee>> {
    suspend fun findByEmail(email: String): Employee?
}