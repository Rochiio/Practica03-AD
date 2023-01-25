package repositories.users

import model.users.Employee
import org.litote.kmongo.Id
import repositories.ICRUD


interface EmployeeRepository: ICRUD<Employee, String> {
    suspend fun findByEmail(email: String): Employee?
}