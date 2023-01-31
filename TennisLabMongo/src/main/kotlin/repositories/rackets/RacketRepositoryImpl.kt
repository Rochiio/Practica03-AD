package repositories.rackets

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import model.Racket
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

/**
 * Implementación repositorio raquetas.
 */
@Single
class RacketRepositoryImpl: RacketRepository {
    private var logger = KotlinLogging.logger {}
    private var dbMongo = MongoDbManager.database

    /**
     * Buscar una raqueta por su id.
     * @param id id de la raqueta a buscar.
     * @return la raqueta si ha sido encontrada.
     */
    override suspend fun findById(id: String): Racket? {
        logger.debug { "Buscando raqueta con id: $id"}
        return dbMongo.getCollection<Racket>()
            .findOneById(id)
    }


    /**
     * Salvar a una raqueta.
     * @param item raqueta a salvar.
     * @return raqueta que ha sido salvada.
     */
    override suspend fun save(item: Racket): Racket {
        logger.debug { "Salvando raqueta: $item" }
        return dbMongo.getCollection<Racket>()
            .save(item).let { item }
    }


    /**
     * Actualizar a una raqueta.
     * @param item raqueta a actualizar.
     * @return la raqueta actualizada.
     */
    override suspend fun update(item: Racket): Racket {
        logger.debug { "Actualizando raqueta: $item" }
        return dbMongo.getCollection<Racket>()
            .updateOneById(item.id, item).wasAcknowledged().let { item }
    }


    /**
     * Eliminar a una raqueta.
     * @param item raqueta a eliminar.
     * @return si la raqueta ha sido eliminada.
     */
    override suspend fun delete(item: Racket): Boolean {
        logger.debug { "Eliminando raqueta: $item" }
        return dbMongo.getCollection<Racket>()
            .deleteOneById(item.id).wasAcknowledged()
    }


    /**
     * Buscar todas las raquetas que hay.
     * @return flujo de raquetas.
     */
    override suspend fun findAll(): Flow<Racket> {
        logger.debug { "Buscando rauqetas" }
        return dbMongo.getCollection<Racket>().find().publisher.asFlow()
    }


    /**
     * Eliminar todas las raquetas.
     * @return si han sido eliminadas con éxito.
     */
    override suspend fun deleteAll(): Boolean {
        logger.debug { "Elminando todas las raquetas" }
        return dbMongo.getCollection<Racket>()
            .deleteMany("{}").wasAcknowledged()
    }
}