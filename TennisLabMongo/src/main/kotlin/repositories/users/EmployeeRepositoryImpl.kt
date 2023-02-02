package repositories.users

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import model.users.Employee
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.eq

/**
 * Implementación del repositorio de trabajadores.
 */
@Single
class EmployeeRepositoryImpl: EmployeeRepository {
    private var logger = KotlinLogging.logger {}
    private var dbMongo = MongoDbManager.database


    /**
     * Buscar un empleado por su correo.
     * @param email email por el que buscar.
     * @return el empleado si ha sido encontrado.
     */
    override suspend fun findByEmail(email: String): Employee? {
        logger.info { "Buscando empleado por email: $email"}
        return dbMongo.getCollection<Employee>()
            .findOne(Employee::email eq email)
    }


    /**
     * Buscar un empleado por su id.
     * @param id id del empleado a buscar.
     * @return el empleado si ha sido encontrado.
     */
    override suspend fun findById(id: String): Employee? {
        logger.info { "Buscando empleado por id: $id" }
        return dbMongo.getCollection<Employee>()
            .findOneById(id)
    }


    /**
     * Salvar a un empleado.
     * @param item empleado a salvar.
     * @return empleado que ha sido salvado.
     */
    override suspend fun save(item: Employee): Employee {
        logger.info { "Salvando empleado: $item"}
        return dbMongo.getCollection<Employee>()
            .save(item).let {item}
    }


    /**
     * Actualizar a un empleado.
     * @param item empleado a actualizar.
     * @return el empleado actualizado.
     */
    override suspend fun update(item: Employee): Employee {
        logger.info { "Actualizando empleado: $item"}
        return dbMongo.getCollection<Employee>()
            .updateOneById(item.id, item).wasAcknowledged().let { item }
    }


    /**
     * TODO no se si este va a funcionar
     * Eliminar a un empleado.
     * @param item empleado a eliminar.
     * @return si el empleado ha sido eliminado.
     */
    override suspend fun delete(item: Employee): Boolean {
        logger.info { "Eliminando empleado: $item"}
        return dbMongo.getCollection<Employee>()
            .updateOneById(item.id, "{${MongoOperator.set}:{'available' : false}}").wasAcknowledged()
    }


    /**
     * Buscar a todos los empleados que hay.
     * @return flujo de empleados.
     */
    override suspend fun findAll(): Flow<Employee> {
        logger.info { "Buscando todos los empleados."}
        return dbMongo.getCollection<Employee>().find().publisher.asFlow()
    }


    /**
     * TODO este tampoco sé si va a fufar.
     * Eliminar todos los empleados.
     * @return si han sido eliminados con éxito.
     */
    override suspend fun deleteAll(): Boolean {
        logger.info { "Eliminando todos los empleados." }
        return dbMongo.getCollection<Employee>()
            .deleteMany("{}").wasAcknowledged()
    }


}