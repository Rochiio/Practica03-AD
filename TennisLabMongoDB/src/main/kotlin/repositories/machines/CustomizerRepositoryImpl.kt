package repositories.maquinas


import kotlinx.coroutines.flow.Flow
import model.machines.Customizer
import mu.KotlinLogging
import org.litote.kmongo.Id

class CustomizerRepositoryImpl: CustomizerRepository {
    private var logger = KotlinLogging.logger {}
    override suspend fun findById(id: Id<Customizer>): Customizer? {
        TODO("Not yet implemented")
    }

    override suspend fun save(item: Customizer): Customizer {
        TODO("Not yet implemented")
    }

    override suspend fun update(item: Customizer): Customizer {
        TODO("Not yet implemented")
    }

    override suspend fun delete(item: Customizer): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): Flow<Customizer> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll(): Boolean {
        TODO("Not yet implemented")
    }


}