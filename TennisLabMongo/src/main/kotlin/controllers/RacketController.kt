package controllers

import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import exception.RacketErrorExists
import exception.RacketErrorNotFound
import exception.RacketResult
import exception.RacketSuccess
import kotlinx.coroutines.flow.Flow
import model.Racket
import org.koin.core.annotation.Single
import repositories.rackets.RacketRepository
import service.reactive.Watchers

/**
 * Controlador de raquetas.
 */
@Single
class RacketController(
    private var repository: RacketRepository, private var watchers: Watchers
) {

    /**
     * Salvar una raqueta.
     * @param item raqueta a salvar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun saveRacket(item: Racket): RacketResult<Racket>{
        val find = repository.findById(item.id)
        find?.let {
            return RacketErrorExists("Ya existe una raqueta con este id: ${item.id}")
        }?: run{
            repository.save(item)
            return RacketSuccess(201, item)
        }
    }


    /**
     * Encontrar una raqueta por su id.
     * @param id id de la raqueta a buscar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun findById(id: String): RacketResult<Racket>{
        val find = repository.findById(id)
        find?.let {
            return RacketSuccess(200, it)
        }
        return RacketErrorNotFound("No se ha encontrado la raqueta con el id:$id")
    }


    /**
     * Actualizar una rauqetas.
     * @param item raqueta a actualizar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun updateRacket(item: Racket): RacketResult<Racket>{
        repository.update(item)
        return RacketSuccess(200, item)
    }


    /**
     * Buscar todas las raquetas que exiten.
     * @return Flujo de raquetas
     */
    suspend fun getAllRackets(): RacketResult<Flow<Racket>>{
        val flow = repository.findAll()
        return  RacketSuccess(200, flow)
    }


    /**
     * Eliminar una raqueta.
     * @param item rauqeta a eliminar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun deleteRacket(item: Racket):RacketResult<Boolean>{
        val delete = repository.delete(item)
        return RacketSuccess(200, delete)
    }

    fun watchRackets() : ChangeStreamPublisher<Racket>{
        return watchers.watchRacket()
    }
}