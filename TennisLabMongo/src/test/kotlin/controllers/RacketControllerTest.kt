package controllers

import exception.RacketErrorExists
import exception.RacketErrorNotFound
import exception.RacketSuccess
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
import model.Racket
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import repositories.rackets.RacketRepositoryImpl
import service.reactive.Watchers

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class RacketControllerTest {
    @MockK
    private lateinit var repository: RacketRepositoryImpl
    @MockK
    private lateinit var watchers: Watchers
    @InjectMockKs
    private lateinit var controller: RacketController

    private var racket = Racket(brand = "Brand", model="Modelo",
        maneuverability = 2.5f, balance = 6.4f, rigidity = 2.2f)

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun saveRacket() = runTest{
        coEvery { repository.findById(racket.id) } returns null
        coEvery { repository.save(racket) } returns racket

        val save = controller.saveRacket(racket)
        val resultSuccess = save as RacketSuccess<Racket>

        assertAll(
            { assertEquals(resultSuccess.code, 201) },
            { assertEquals(resultSuccess.data.id, racket.id) },
            { assertEquals(resultSuccess.data.uuid, racket.uuid) },
            { assertEquals(resultSuccess.data.brand, racket.brand) },
            { assertEquals(resultSuccess.data.model, racket.model) },
            { assertEquals(resultSuccess.data.maneuverability, racket.maneuverability) },
            { assertEquals(resultSuccess.data.balance, racket.balance) },
            { assertEquals(resultSuccess.data.rigidity, racket.rigidity) },
        )

        coVerify(exactly = 1){repository.findById(racket.id)}
        coVerify(exactly = 1){repository.save(racket)}
    }

    @Test
    fun saveRacketError() = runTest{
        coEvery { repository.findById(racket.id) } returns racket

        val save = controller.saveRacket(racket)
        val resultError = save as RacketErrorExists

        assertAll(
            { assertEquals(resultError.code, 403) },
            { assertEquals(resultError.message, "Ya existe una raqueta con este id: ${racket.id}") }
        )

        coVerify(exactly = 1){repository.findById(racket.id)}
    }

    @Test
    fun findById() = runTest{
        coEvery { repository.findById(racket.id) } returns racket

        val find = controller.findById(racket.id)
        val resultSuccess = find as RacketSuccess<Racket>

        assertAll(
            { assertEquals(resultSuccess.code, 200) },
            { assertEquals(resultSuccess.data.id, racket.id) },
            { assertEquals(resultSuccess.data.uuid, racket.uuid) },
            { assertEquals(resultSuccess.data.brand, racket.brand) },
            { assertEquals(resultSuccess.data.model, racket.model) },
            { assertEquals(resultSuccess.data.maneuverability, racket.maneuverability) },
            { assertEquals(resultSuccess.data.balance, racket.balance) },
            { assertEquals(resultSuccess.data.rigidity, racket.rigidity) },
        )

        coVerify(exactly = 1){repository.findById(racket.id)}
    }

    @Test
    fun findByIdError() =runTest{
        coEvery { repository.findById(racket.id) } returns null

        val find = controller.findById(racket.id)
        val resultError = find as RacketErrorNotFound

        assertAll(
            { assertEquals(resultError.code, 404) },
            { assertEquals(resultError.message, "No se ha encontrado la raqueta con el id:${racket.id}") }
        )

        coVerify(exactly = 1){repository.findById(racket.id)}
    }

    @Test
    fun updateRacket() = runTest{
        coEvery { repository.update(racket) } returns racket

        val update = controller.updateRacket(racket)
        val resultSuccess = update as RacketSuccess<Racket>

        assertAll(
            { assertEquals(resultSuccess.code, 200) },
            { assertEquals(resultSuccess.data.id, racket.id) },
            { assertEquals(resultSuccess.data.uuid, racket.uuid) },
            { assertEquals(resultSuccess.data.brand, racket.brand) },
            { assertEquals(resultSuccess.data.model, racket.model) },
            { assertEquals(resultSuccess.data.maneuverability, racket.maneuverability) },
            { assertEquals(resultSuccess.data.balance, racket.balance) },
            { assertEquals(resultSuccess.data.rigidity, racket.rigidity) },
        )

        coVerify(exactly = 1){repository.update(racket)}
    }

    @Test
    fun getAllRackets() = runTest{
        coEvery { repository.findAll() } returns flowOf(racket)

        val all = controller.getAllRackets()
        val resultSuccess = all as RacketSuccess<Flow<Racket>>
        val flow = resultSuccess.data.toList()

        assertAll(
            { assertEquals(resultSuccess.code, 200) },
            { assertTrue(flow.isNotEmpty()) },
            { assertEquals(flow[0].id, racket.id) },
            { assertEquals(flow[0].uuid, racket.uuid) },
            { assertEquals(flow[0].brand, racket.brand) },
            { assertEquals(flow[0].model, racket.model) },
            { assertEquals(flow[0].maneuverability, racket.maneuverability) },
            { assertEquals(flow[0].balance, racket.balance) },
            { assertEquals(flow[0].rigidity, racket.rigidity) },
        )

        coVerify(exactly = 1){repository.findAll()}
    }

    @Test
    fun deleteRacket() = runTest{
        coEvery { repository.delete(racket) } returns true
        val delete = controller.deleteRacket(racket)
        val resultSuccess = delete as RacketSuccess<Boolean>

        assertAll(
            {assertEquals(resultSuccess.code, 200)},
            {assertTrue(resultSuccess.data)}
        )

        coVerify(exactly = 1){repository.delete(racket)}
    }
}