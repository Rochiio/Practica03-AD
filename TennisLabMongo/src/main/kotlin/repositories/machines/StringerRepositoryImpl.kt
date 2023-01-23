package repositories.machines

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import model.machines.Stringer
import mu.KotlinLogging
import org.litote.kmongo.Id


/**
 * Implementación repositorio de maquinas de encordar.
 */

class StringerRepositoryImpl : StringerRepository {
    private var logger = KotlinLogging.logger {}
    private var dbMongo = MongoDbManager.database


    /**
     * Buscar una encordadora por el id.
     * @param id id de la encordadora a buscar.
     * @return encordadora si ha sido encontrada.
     */
    override suspend fun findById(id: Id<Stringer>): Stringer? {
        logger.debug { "Buscando encordadora por id: $id" }
        return dbMongo.getCollection<Stringer>()
            .findOneById(id)
    }


    /**
     * Salvar encordadora.
     * @param item encordadoraa salvar.
     * @return la encordadora salvada.
     */
    override suspend fun save(item: Stringer): Stringer {
        logger.debug{"Salvando encordadora: $item"}
        return dbMongo.getCollection<Stringer>()
            .save(item).let { item }
    }


    /**
     * Actualizar encordadora.
     * @param item encordadora a actualizar.
     * @return encordadora actualizada.
     */
    override suspend fun update(item: Stringer): Stringer {
        logger.debug { "Actualizando encordadora: $item" }
        return dbMongo.getCollection<Stringer>()
            .updateOneById(item.id, item).wasAcknowledged().let { item }
    }


    /**
     * Eliminar una encordadora.
     * @param item encordadora a eliminar.
     * @return si ha sido eliminada correctamente.
     */
    override suspend fun delete(item: Stringer): Boolean {
        logger.debug { "Eliminando encordadora: $item"}
        return dbMongo.getCollection<Stringer>()
            .deleteOneById(item.id).wasAcknowledged()
    }


    /**
     * Buscar todas las máquinas de encordado.
     * @return flujo con las encordadoras.
     */
    override suspend fun findAll(): Flow<Stringer> {
        logger.debug { "Buscando todas las encordadoras" }
        return dbMongo.getCollection<Stringer>().find().publisher.asFlow()
    }


    /**
     * Eliminar todas las encordadoras.
     * @return si han sido eliminadas con éxito.
     */
    override suspend fun deleteAll(): Boolean {
        logger.debug { "Eliminando todas las encordadoras" }
        return dbMongo.getCollection<Stringer>()
            .deleteMany("{}").wasAcknowledged()
    }

}