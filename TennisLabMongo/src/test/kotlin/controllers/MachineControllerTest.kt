package controllers

import exception.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import model.TypeMachine
import model.machines.Customizer
import model.machines.Stringer
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import repositories.machines.CustomizerRepositoryImpl
import repositories.machines.StringerRepositoryImpl
import service.reactive.Watchers
import java.time.LocalDate

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class MachineControllerTest {

    @MockK
    private lateinit var stringerRepo: StringerRepositoryImpl
    @MockK
    private lateinit var customizerRepo: CustomizerRepositoryImpl
    @MockK
    private lateinit var watchers: Watchers
    @InjectMockKs
    private lateinit var controller: MachineController

    private val customizer = Customizer(brand = "test", model = "test", acquisitionDate = LocalDate.now(), available = true,
        maneuverability = true, balance = false, rigidity = true)
    private val stringer = Stringer(brand = "test", model = "test", acquisitionDate = LocalDate.now(), available = true,
        automatic = TypeMachine.MANUAL, maximumTension = 50, minimumTension = 10)


    init {
        MockKAnnotations.init(this)
    }


    @Test
    fun addStringer() = runTest{
        coEvery { stringerRepo.findById(stringer.id) } returns null
        coEvery { stringerRepo.save(stringer)} returns stringer
        val get = controller.addStringer(stringer)
        val resultSuccess = get as StringerSuccess<Stringer>

        assertAll(
            { assertTrue(resultSuccess.code==201) },
            { assertEquals(resultSuccess.data.id, stringer.id) },
            { assertEquals(resultSuccess.data.brand, stringer.brand) },
            { assertEquals(resultSuccess.data.model, stringer.model) },
            { assertEquals(resultSuccess.data.acquisitionDate, stringer.acquisitionDate) },
            { assertEquals(resultSuccess.data.available, stringer.available) },
            { assertEquals(resultSuccess.data.automatic, stringer.automatic) },
            { assertEquals(resultSuccess.data.maximumTension, stringer.maximumTension) },
            { assertEquals(resultSuccess.data.minimumTension, stringer.minimumTension) },
        )

        coVerify(exactly = 1){stringerRepo.findById(stringer.id)}
        coVerify(exactly = 1){stringerRepo.save(stringer)}
    }

    @Test
    fun addStringerError() = runTest{
        coEvery { stringerRepo.findById(stringer.id) } returns stringer
        val get = controller.addStringer(stringer)
        val result = get as StringerErrorExists

        assertAll(
            { assertTrue(result.code == 403) },
            { assertEquals(result.message, "Ya existe una encordadora con este id")}
        )

        coVerify(exactly = 1){stringerRepo.findById(stringer.id)}
    }

    @Test
    fun getStringerById() = runTest{
        coEvery { stringerRepo.findById(stringer.id) } returns stringer
        val get = controller.getStringerById(stringer.id)
        val resultSuccess = get as StringerSuccess<Stringer>
        assertAll(
            { assertTrue(resultSuccess.code==200) },
            { assertEquals(resultSuccess.data.id, stringer.id) },
            { assertEquals(resultSuccess.data.brand, stringer.brand) },
            { assertEquals(resultSuccess.data.model, stringer.model) },
            { assertEquals(resultSuccess.data.acquisitionDate, stringer.acquisitionDate) },
            { assertEquals(resultSuccess.data.available, stringer.available) },
            { assertEquals(resultSuccess.data.automatic, stringer.automatic) },
            { assertEquals(resultSuccess.data.maximumTension, stringer.maximumTension) },
            { assertEquals(resultSuccess.data.minimumTension, stringer.minimumTension) },
        )

        coVerify(exactly = 1){stringerRepo.findById(stringer.id)}
    }

    @Test
    fun getStringerByIdError() = runTest{
        coEvery { stringerRepo.findById(stringer.id) } returns null
        val get = controller.getStringerById(stringer.id)
        val result = get as StringerErrorNotFound

        assertAll(
            { assertTrue(result.code == 404)},
            { assertEquals(result.message, "No existe encordadora con este id")}
        )

        coVerify(exactly = 1){stringerRepo.findById(stringer.id)}
    }

    @Test
    fun updateStringer() = runTest{
        coEvery { stringerRepo.update(stringer) } returns stringer
        val get = controller.updateStringer(stringer)
        val resultSuccess = get as StringerSuccess<Stringer>

        assertAll(
            { assertTrue(resultSuccess.code==200) },
            { assertEquals(resultSuccess.data.id, stringer.id) },
            { assertEquals(resultSuccess.data.brand, stringer.brand) },
            { assertEquals(resultSuccess.data.model, stringer.model) },
            { assertEquals(resultSuccess.data.acquisitionDate, stringer.acquisitionDate) },
            { assertEquals(resultSuccess.data.available, stringer.available) },
            { assertEquals(resultSuccess.data.automatic, stringer.automatic) },
            { assertEquals(resultSuccess.data.maximumTension, stringer.maximumTension) },
            { assertEquals(resultSuccess.data.minimumTension, stringer.minimumTension) }
        )

        coVerify(exactly = 1){stringerRepo.update(stringer)}
    }

    @Test
    fun getAllStringers() = runTest{
        coEvery { stringerRepo.findAll() } returns flowOf(stringer)
        val get = controller.getAllStringers()
        val resultSuccess = get as StringerSuccess<Flow<Stringer>>
        val flow = resultSuccess.data.toList()

        assertAll(
            { assertTrue(resultSuccess.code == 200)},
            { assertEquals(flow[0].id, stringer.id) },
            { assertEquals(flow[0].brand, stringer.brand) },
            { assertEquals(flow[0].model, stringer.model) },
            { assertEquals(flow[0].acquisitionDate, stringer.acquisitionDate) },
            { assertEquals(flow[0].available, stringer.available) },
            { assertEquals(flow[0].automatic, stringer.automatic) },
            { assertEquals(flow[0].maximumTension, stringer.maximumTension) },
            { assertEquals(flow[0].minimumTension, stringer.minimumTension) }
        )

        coVerify(exactly = 1){stringerRepo.findAll()}
    }

    @Test
    fun deleteStringer() = runTest{
        coEvery { stringerRepo.delete(stringer) } returns true
        val get = controller.deleteStringer(stringer)
        val resultSuccess = get as StringerSuccess<Boolean>

        assertAll(
            {assertTrue(resultSuccess.code == 200)},
            {assertTrue(resultSuccess.data)}
        )

        coVerify(exactly = 1){stringerRepo.delete(stringer)}
    }

    @Test
    fun addCustomizer() = runTest{
        coEvery { customizerRepo.findById(customizer.id) } returns null
        coEvery { customizerRepo.save(customizer) } returns customizer
        val get = controller.addCustomizer(customizer)
        val resultSuccess = get as CustomizerSuccess<Customizer>

        assertAll(
            { assertTrue(resultSuccess.code==201) },
            { assertEquals(resultSuccess.data.id, customizer.id) },
            { assertEquals(resultSuccess.data.brand, customizer.brand) },
            { assertEquals(resultSuccess.data.model, customizer.model) },
            { assertEquals(resultSuccess.data.acquisitionDate, customizer.acquisitionDate) },
            { assertEquals(resultSuccess.data.available, customizer.available) },
            { assertEquals(resultSuccess.data.maneuverability, customizer.maneuverability) },
            { assertEquals(resultSuccess.data.balance, customizer.balance) },
            { assertEquals(resultSuccess.data.rigidity, customizer.rigidity) },
        )

        coVerify(exactly = 1){customizerRepo.save(customizer)}
        coVerify(exactly = 1){customizerRepo.findById(customizer.id)}
    }

    @Test
    fun addCustomizerError() = runTest{
        coEvery { customizerRepo.findById(customizer.id) } returns customizer
        val get = controller.addCustomizer(customizer)
        val result = get as CustomizerErrorExists

        assertAll(
            {assertTrue(result.code == 403)},
            {assertEquals(result.message, "Ya existe una personalizadora con este id")}
        )

        coVerify(exactly = 1){customizerRepo.findById(customizer.id)}
    }

    @Test
    fun getCustomizerById() = runTest{
        coEvery { customizerRepo.findById(customizer.id) } returns customizer
        val get = controller.getCustomizerById(customizer.id)
        val resultSuccess = get as CustomizerSuccess<Customizer>

        assertAll(
            { assertTrue(resultSuccess.code==200) },
            { assertEquals(resultSuccess.data.id, customizer.id) },
            { assertEquals(resultSuccess.data.brand, customizer.brand) },
            { assertEquals(resultSuccess.data.model, customizer.model) },
            { assertEquals(resultSuccess.data.acquisitionDate, customizer.acquisitionDate) },
            { assertEquals(resultSuccess.data.available, customizer.available) },
            { assertEquals(resultSuccess.data.maneuverability, customizer.maneuverability) },
            { assertEquals(resultSuccess.data.balance, customizer.balance) },
            { assertEquals(resultSuccess.data.rigidity, customizer.rigidity) },
        )

        coVerify(exactly = 1){customizerRepo.findById(customizer.id)}
    }

    @Test
    fun getCustomizerByIdError() = runTest{
        coEvery { customizerRepo.findById(customizer.id) } returns null
        val get = controller.getCustomizerById(customizer.id)
        val result = get as CustomizerErrorNotFound

        assertAll(
            { assertTrue(result.code == 404)},
            { assertEquals(result.message, "No existe la personalizadora con este id") }
        )

        coVerify(exactly = 1){customizerRepo.findById(customizer.id)}
    }

    @Test
    fun updateCustomizer() = runTest{
        coEvery { customizerRepo.update(customizer) } returns customizer
        val get = controller.updateCustomizer(customizer)
        val resultSuccess = get as CustomizerSuccess<Customizer>

        assertAll(
            { assertTrue(resultSuccess.code==200) },
            { assertEquals(resultSuccess.data.id, customizer.id) },
            { assertEquals(resultSuccess.data.brand, customizer.brand) },
            { assertEquals(resultSuccess.data.model, customizer.model) },
            { assertEquals(resultSuccess.data.acquisitionDate, customizer.acquisitionDate) },
            { assertEquals(resultSuccess.data.available, customizer.available) },
            { assertEquals(resultSuccess.data.maneuverability, customizer.maneuverability) },
            { assertEquals(resultSuccess.data.balance, customizer.balance) },
            { assertEquals(resultSuccess.data.rigidity, customizer.rigidity) },
        )

        coVerify(exactly = 1){customizerRepo.update(customizer)}
    }

    @Test
    fun getAllCustomizers() = runTest{
        coEvery { customizerRepo.findAll() } returns flowOf(customizer)
        val get = controller.getAllCustomizers()
        val resultSuccess = get as CustomizerSuccess<Flow<Customizer>>
        val flow = resultSuccess.data.toList()

        assertAll(
            { assertTrue(resultSuccess.code==200) },
            { assertEquals(flow[0].id, customizer.id) },
            { assertEquals(flow[0].brand, customizer.brand) },
            { assertEquals(flow[0].model, customizer.model) },
            { assertEquals(flow[0].acquisitionDate, customizer.acquisitionDate) },
            { assertEquals(flow[0].available, customizer.available) },
            { assertEquals(flow[0].maneuverability, customizer.maneuverability) },
            { assertEquals(flow[0].balance, customizer.balance) },
            { assertEquals(flow[0].rigidity, customizer.rigidity) },
        )

        coVerify(exactly = 1){customizerRepo.findAll()}
    }

    @Test
    fun deleteCustomizer() = runTest{
        coEvery { customizerRepo.delete(customizer) } returns true
        val get = controller.deleteCustomizer(customizer)
        val result = get as CustomizerSuccess<Boolean>

        assertAll(
            {assertTrue(result.code == 200)},
            { assertTrue(result.data) }
        )

        coVerify(exactly = 1){customizerRepo.delete(customizer)}
    }
}