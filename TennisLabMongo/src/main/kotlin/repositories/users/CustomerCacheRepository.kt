package repositories.users

import kotlinx.coroutines.flow.Flow
import model.users.Customer

interface CustomerCacheRepository {
    suspend fun findById(id: String): Customer?
    suspend fun addCache(item: Customer): Customer
    suspend fun delete(item: Customer):Boolean
    suspend fun update(item: Customer): Customer
}