package controllers

import exception.EmployeeErrorExists
import exception.EmployeeErrorNotFound
import exception.EmployeeResult
import exception.EmployeeSuccess
import kotlinx.coroutines.flow.Flow
import model.users.Employee
import org.litote.kmongo.Id
import repositories.users.EmployeeRepository


class EmployeeController(private var repository: EmployeeRepository) {


    /**
     * Saber si existe un trabajador con un email y password.
     * @param email email a buscar si existe
     * @param password a ver si es correcta
     * @return trabajador dependiendo de si existe
     */
    suspend fun getEmployeeByEmailAndPassword(email: String, password: String): EmployeeResult<Employee>{
        val find = repository.findByEmail(email)
        find?.let {
            if (find.password != password){
                return EmployeeErrorNotFound("Usuario o contraseña incorrecto")
            }
        }?: run {
                return EmployeeErrorNotFound("Usuario o contraseña incorrecto")
        }
        return EmployeeSuccess(200, find)
    }


    /**
     * Buscar un empleado por el id.
     */
    suspend fun getEmployeeById(id: String): EmployeeResult<Employee>{
        val find = repository.findById(id)
        find?.let {
            return EmployeeSuccess(200, it)
        }
        return EmployeeErrorNotFound("Empleado no encontrado con id: $id")
    }


    /**
     * Añadir un trabajador
     */
    suspend fun addEmployee(employee: Employee): EmployeeResult<Employee>{
        val existe = repository.findByEmail(employee.email)
        existe?.let {
            return EmployeeErrorExists("Ya existe un trabajador con este email")
        }?: run{
            repository.save(employee)
            return EmployeeSuccess(201, employee)
        }
    }


    /**
     * Conseguir todos los trabajadores que existen.
     */
    suspend fun getAllEmployees():EmployeeResult<Flow<Employee>>{
        val flow = repository.findAll()
        return EmployeeSuccess(200, flow)
    }


    /**
     * Actualizar un employee
     */
    suspend fun updateEmployee(employee:Employee):EmployeeResult<Employee>{
        var update = repository.update(employee)
        return EmployeeSuccess(200, update)
    }


    /**
     * Eliminar un trabajador
     */
    suspend fun deleteEmployee(employee:Employee):EmployeeResult<Boolean>{
        var correcto =repository.delete(employee)
        return EmployeeSuccess(200, correcto)
    }

}