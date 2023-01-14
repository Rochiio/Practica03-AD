package repositories.usuarios

import entities.usuarios.TrabajadorDAO
import entities.usuarios.TrabajadorTable
import models.usuarios.Trabajador
import mu.KotlinLogging
import mappers.fromTrabajadorDaoToTrabajador
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * Implementación del repositorio de trabajadores.
 */
class TrabajadorRepositoryImpl(
    private var trabajadorDAO: IntEntityClass<TrabajadorDAO>,
) : TrabajadorRepository {

    private var logger = KotlinLogging.logger {}
    override fun findByEmail(email: String): Trabajador? =transaction{
        logger.debug { "buscando trabajador con email: $email" }
        trabajadorDAO.find { TrabajadorTable.email eq email }.firstOrNull()?.fromTrabajadorDaoToTrabajador()
    }


    /**
     * Buscar un trabajador por su id.
     * @param id id por el que buscar al trabajador.
     * @return trabajador o no dependiendo de si lo han encontrado.
     */
    override fun findById(id: Int): Trabajador? =transaction{
        logger.debug { "buscando trabajador con uuid: $id" }
        trabajadorDAO.findById(id)?.fromTrabajadorDaoToTrabajador()
    }


    /**
     * Buscar un trabajador por su uuid.
     * @param uuid por el que buscar al trabajador.
     * @return trabajador o no dependiendo de si lo han encontrado.
     */
    override fun findByUUID(uuid: UUID): Trabajador? =transaction{
        logger.debug { "buscando trabajador con uuid: $uuid" }
        trabajadorDAO.find { TrabajadorTable.uuid eq uuid }.firstOrNull()?.fromTrabajadorDaoToTrabajador()
    }


    /**
     * Salvar a un trabajador.
     * @param item trabajador a salvar.
     * @return trabajador
     */
    override suspend fun save(item: Trabajador): Trabajador =transaction{
        logger.debug { "Save trabajador" }
        item.id?.let {
            trabajadorDAO.findById(it)?.let { update ->
                return@transaction update(item, update)
            }
        } ?: run {
            return@transaction add(item)
        }
    }


    /**
     * Añadir un trabajador.
     * @param item trabajador a añadir.
     * @return trabajador
     */
    override suspend fun add(item: Trabajador): Trabajador =transaction{
        trabajadorDAO.new {
            nombre = item.nombre
            apellido = item.apellido
            email = item.email
            password = item.password
            disponible = item.disponible
            administrador= item.administrador
        }.fromTrabajadorDaoToTrabajador()
    }


    /**
     * Actualizar un trabajador.
     * @param item trabajador modelo actualizado.
     * @param updateItem trabajador DAO a actualizar en la base de datos.
     * @return trabajador
     */
    fun update(item: Trabajador, updateItem: TrabajadorDAO): Trabajador =transaction{
        logger.debug { "actualizando trabajador" }
        updateItem.apply {
            nombre = item.nombre
            apellido = item.apellido
            email = item.email
            password = item.password
            disponible = item.disponible
            administrador= item.administrador
        }.fromTrabajadorDaoToTrabajador()
    }


    /**
     * Eliminar a  un trabajador.
     * @param item trabajador a eliminar.
     * @return boolean dependiendo de si ha sido eliminado o no.
     */
    override suspend fun delete(item: Trabajador): Boolean =transaction{
        val existe = item.id?.let { trabajadorDAO.findById(it) } ?: return@transaction false
        logger.debug { "eliminando trabajador: $item" }
        existe.disponible = false
        return@transaction true
    }


    /**
     * Buscar a todos los trabajadores que hay.
     * @return lista de trabajadores.
     */
    override suspend fun findAll(): List<Trabajador> =transaction{
        logger.debug { "Buscando todos los trabajadores"}
        trabajadorDAO.all().map { it.fromTrabajadorDaoToTrabajador()}.toList()
    }


    /**
     * Eliminar todos los trabajadores que existen.
     * @return boolean dependiendo de si ha eliminado o no trabajadores.
     */
    override suspend fun deleteAll(): Boolean =transaction{
        logger.debug { "Eliminando todos los trabajadores"}
        var num = TrabajadorTable.deleteAll()
        return@transaction num>0
    }


}