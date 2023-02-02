package com.example.tennislabspring.controllers

import com.example.tennislabspring.exception.*
import com.example.tennislabspring.model.users.Employee
import com.example.tennislabspring.repositories.users.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

/**
 * Controlador de empleados.
 */
@Controller
class EmployeeController
    @Autowired constructor(
    private var repository: EmployeeRepository
) {


    /**
     * Saber si existe un trabajador con un email y password.
     * @param email email a buscar si existe
     * @param password a ver si es correcta
     * @return trabajador dependiendo de si existe
     */
    suspend fun getEmployeeByEmailAndPassword(email: String, password: String): EmployeeResult<Employee> {
        val find = repository.findEmployeeByEmail(email)
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
     * @param id id del empleado a buscar.
     * @return Result dependiendo de si ha sido correcto el resultado.
     */
    suspend fun getEmployeeById(id: String): EmployeeResult<Employee>{
        val find = repository.findById(id)
        find?.let {
            return EmployeeSuccess(200, it)
        }
        return EmployeeErrorNotFound("Empleado no encontrado con id: $id")
    }


    /**
     * Añadir un trabajador.
     * @param employee trabajador a añadir.
     * @return Result dependiendo de la accion.
     */
    suspend fun addEmployee(employee: Employee): EmployeeResult<Employee>{
        val existe = repository.findEmployeeByEmail(employee.email)
        existe?.let {
            return EmployeeErrorExists("Ya existe un trabajador con este email")
        }?: run{
            repository.save(employee)
            return EmployeeSuccess(201, employee)
        }
    }


    /**
     * Conseguir todos los trabajadores que existen.
     * @return Flujo de empleados.
     */
    suspend fun getAllEmployees():EmployeeResult<Flow<Employee>>{
        val flow = repository.findAll()
        return EmployeeSuccess(200, flow)
    }


    /**
     * Actualizar un employee
     * @param employee empleado a actualizar.
     * @return Result de la accion realizada.
     */
    suspend fun updateEmployee(employee:Employee):EmployeeResult<Employee>{
        var update = repository.save(employee)
        return EmployeeSuccess(200, update)
    }


    /**
     * Eliminar un trabajador.
     * @param employee empleado a eliminar.
     * @return Result dependiendo de si se ha eliminado correctamente el empleado.
     */
    suspend fun deleteEmployee(employee:Employee):EmployeeResult<Boolean>{
        repository.delete(employee)
        return EmployeeSuccess(200, true)
    }

    suspend fun deleteAll(){
        repository.deleteAll()
    }

}