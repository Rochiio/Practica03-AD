package controllers

import exception.*
import kotlinx.coroutines.flow.Flow
import model.machines.Customizer
import model.machines.Stringer
import org.litote.kmongo.Id
import repositories.machines.CustomizerRepository
import repositories.machines.StringerRepository
import java.util.*

class MachineController(
    private var stringerRepo: StringerRepository,
    private var customizerRepo: CustomizerRepository
) {


    /**
     * Añadir una encordadora
     */
    suspend fun addStringer(item: Stringer): StringerResult<Stringer>{
        val find = stringerRepo.findById(item.id)
        find?.let {
            return StringerErrorExists("Ya existe una encordadora con este id")
        }
        stringerRepo.save(item)
        return StringerSuccess(201, item)
    }


    /**
     * Buscar una encordadora por su uuid.
     */
    suspend fun getStringerById(id: String): StringerResult<Stringer> {
        var existe = stringerRepo.findById(id)
        existe?.let {
            return StringerSuccess(200, it)
        }?: run{
            return StringerErrorNotFound("No existe encordadora con este id")
        }
    }


    /**
     * Actualizar una encordadora
     */
    suspend fun updateStringer(item: Stringer):StringerResult<Stringer>{
        val update = stringerRepo.update(item)
        return StringerSuccess(200, update)
    }


    /**
     * Conseguir todas las encordadoras.
     */
    suspend fun getAllStringers(): StringerResult<Flow<Stringer>> {
        val flow = stringerRepo.findAll()
        return StringerSuccess(200, flow)
    }


    /**
     * Eliminar una encordadora.
     */
    suspend fun deleteStringer(stringer: Stringer):StringerResult<Boolean> {
        val delete = stringerRepo.delete(stringer)
        return StringerSuccess(200, delete)
    }


    /**
     * Añadir una personalizadora
     */
    suspend fun addCustomizer(item: Customizer):CustomizerResult<Customizer>{
        var existe = customizerRepo.findById(item.id)
        existe?.let {
            return CustomizerErrorExists("Ya existe una personalizadora con este id")
        }?: run{
            customizerRepo.save(item)
            return CustomizerSuccess(201, item)
        }
    }


    /**
     * Conseguir personalizadora por su uuid.
     */
    suspend fun getCustomizerById(id: String): CustomizerResult<Customizer> {
        val existe = customizerRepo.findById(id)
        existe?.let {
            return CustomizerSuccess(200, it)
        }?: run{
            return CustomizerErrorNotFound("No existe la personalizadora con este id")
        }
    }


    /**
     * Actualizar una personalizadora
     */
    suspend fun updateCustomizer(item: Customizer):CustomizerResult<Customizer>{
        val update = customizerRepo.update(item)
        return CustomizerSuccess(200, update)
    }


    /**
     * Conseguir todas las personalizadoras.
     */
    suspend fun getAllCustomizers(): CustomizerResult<Flow<Customizer>> {
        val flow = customizerRepo.findAll()
        return CustomizerSuccess(200, flow)
    }


    /**
     * Eliminar una personalizadora.
     */
    suspend fun deleteCustomizer(customizer: Customizer):CustomizerResult<Boolean> {
        val delete = customizerRepo.delete(customizer)
        return CustomizerSuccess(200, delete)
    }



}