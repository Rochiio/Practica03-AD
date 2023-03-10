package repositories.machines


import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import model.machines.Customizer
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

private var logger = KotlinLogging.logger {}

@Single
class CustomizerRepositoryImpl : CustomizerRepository {
    private val dbMongo = MongoDbManager.database

    /**
     * Buscar una personalizadora por el id.
     * @param id id de la personalizadora a buscar.
     * @return personalizadora si ha sido encontrada.
     */
    override suspend fun findById(id: String): Customizer? {
        logger.info { "Buscando personalizadora por id: $id" }
        return dbMongo.getCollection<Customizer>()
            .findOneById(id)
    }


    /**
     * Salvar personalizadora.
     * @param item personalizadora salvar.
     * @return la personalizadora salvada.
     */
    override suspend fun save(item: Customizer): Customizer {
        logger.info { "Salvando personalizadora: $item" }
        return dbMongo.getCollection<Customizer>()
            .save(item).let { item }
    }


    /**
     * Actualizar personalizadora.
     * @param item personalizadora a actualizar.
     * @return personalizadora actualizada.
     */
    override suspend fun update(item: Customizer): Customizer {
        logger.info { "Actualizando personalizadora: $item" }
        return dbMongo.getCollection<Customizer>()
            .updateOneById(item.id, item).wasAcknowledged().let { item }
    }


    /**
     * Eliminar una personalizadora.
     * @param item personalizadora a eliminar.
     * @return si ha sido eliminada correctamente.
     */
    override suspend fun delete(item: Customizer): Boolean {
        logger.info { "Eliminando personalizadora: $item" }
        return dbMongo.getCollection<Customizer>()
            .deleteOneById(item.id).wasAcknowledged()
    }


    /**
     * Buscar todas las máquinas de personalizado.
     * @return flujo con las personalizadoras.
     */
    override suspend fun findAll(): Flow<Customizer> {
        logger.info { "Buscando todas las personalizadoras" }
        return dbMongo.getCollection<Customizer>().find().publisher.asFlow()
    }


    /**
     * TODO no se si va a funcionar
     * Eliminar todas las personalizadoras.
     * @return si han sido eliminadas con éxito.
     */
    override suspend fun deleteAll(): Boolean {
        logger.info { "Eliminando todas las personalizadoras" }
        return dbMongo.getCollection<Customizer>()
            .deleteMany("{}").wasAcknowledged()
    }

}