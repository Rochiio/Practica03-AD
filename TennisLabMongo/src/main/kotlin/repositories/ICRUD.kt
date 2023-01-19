package repositories

import kotlinx.coroutines.flow.Flow

/**
 * Entities, models, uuid
 */
interface ICRUD<T,ID> {

    suspend fun findById(id: ID):T?
    // TODO en mongo los id son UUID
    //fun findByUUID(uuid: UUID):T?
    suspend fun save(item : T) : T
    suspend fun update(item: T): T
    suspend fun delete(item: T):Boolean
    suspend fun findAll(): Flow<T>
    suspend fun deleteAll():Boolean
}