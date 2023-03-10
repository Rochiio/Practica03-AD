package com.example.tennislabspring.controllers

import com.example.tennislabspring.exception.*
import com.example.tennislabspring.model.machines.Customizer
import com.example.tennislabspring.model.machines.Stringer
import com.example.tennislabspring.repositories.machines.CustomizerRepository
import com.example.tennislabspring.repositories.machines.StringerRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

/**
 * Controlador de Maquinas
 */
@Controller
class MachineController
    @Autowired constructor(
    private var stringerRepo: StringerRepository,
    private var customizerRepo: CustomizerRepository
) {


    /**
     * Añadir una encordadora.
     * @param item encordadora a añadir.
     * @return Result dependiendo de como se haya realizado la accion.
     */
    suspend fun addStringer(item: Stringer): StringerResult<Stringer> {
        val find = stringerRepo.findById(item.id)
        find?.let {
            return StringerErrorExists("Ya existe una encordadora con este id")
        }
        stringerRepo.save(item)
        return StringerSuccess(201, item)
    }


    /**
     * Buscar una encordadora por su id.
     * @param id id de la encordadora a buscar.
     * @return Result dependiendo del resultado de la accion.
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
     * @param item encordadora a actualizar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun updateStringer(item: Stringer):StringerResult<Stringer>{
        val update = stringerRepo.save(item)
        return StringerSuccess(200, update)
    }


    /**
     * Conseguir todas las encordadoras.
     * @return Flujo de encordadoras.
     */
    suspend fun getAllStringers(): StringerResult<Flow<Stringer>> {
        val flow = stringerRepo.findAll()
        return StringerSuccess(200, flow)
    }


    /**
     * Eliminar una encordadora.
     * @param stringer encordadora a eliminar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun deleteStringer(stringer: Stringer):StringerResult<Boolean> {
        stringerRepo.delete(stringer)
        return StringerSuccess(200, true)
    }


    /**
     * Añadir una personalizadora
     * @param item personalizadora a añadir.
     * @return Result dependiendo del resultado de la accion.
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
     * Conseguir personalizadora por su id.
     * @param id id de la personalizadora a buscar.
     * @return Result dependiendo del resultado de la accion.
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
     * Actualizar una personalizadora.
     * @param item personalizadora a actualizar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun updateCustomizer(item: Customizer):CustomizerResult<Customizer>{
        val update = customizerRepo.save(item)
        return CustomizerSuccess(200, update)
    }


    /**
     * Conseguir todas las personalizadoras.
     * @return Flujo de personalizadoras.
     */
    suspend fun getAllCustomizers(): CustomizerResult<Flow<Customizer>> {
        val flow = customizerRepo.findAll()
        return CustomizerSuccess(200, flow)
    }


    /**
     * Eliminar una personalizadora.
     * @param customizer personalizadora a eliminar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun deleteCustomizer(customizer: Customizer):CustomizerResult<Boolean> {
        customizerRepo.delete(customizer)
        return CustomizerSuccess(200, true)
    }

    suspend fun deleteAll(){
        stringerRepo.deleteAll()
        customizerRepo.deleteAll()
    }

}