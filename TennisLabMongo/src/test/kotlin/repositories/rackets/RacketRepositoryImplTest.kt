package repositories.rackets

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import model.Racket
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class RacketRepositoryImplTest {
    private val repo = RacketRepositoryImpl()
    private val racket = Racket(brand = "Test", model = "Test", maneuverability = 25.0f, balance =2.5f, rigidity = 1.8f)

    private val mainThreadSurrogate = newSingleThreadContext("Test thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @BeforeEach
    fun setUpEach() = runTest{
        repo.deleteAll()
    }

    @After
    fun tearDown() = runTest{
        repo.deleteAll()
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun findById() = runTest{
        repo.save(racket)
        val find = repo.findById(racket.id)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(find?.id, racket.id) },
            { assertEquals(find?.brand, racket.brand) },
            { assertEquals(find?.model, racket.model) },
            { assertEquals(find?.maneuverability, racket.maneuverability) },
            { assertEquals(find?.balance, racket.balance) },
            { assertEquals(find?.rigidity, racket.rigidity) }
        )
    }

    @Test
    fun save() =runTest{
        val save = repo.save(racket)

        assertAll(
            { assertEquals(save.id, racket.id) },
            { assertEquals(save.brand, racket.brand) },
            { assertEquals(save.model, racket.model) },
            { assertEquals(save.maneuverability, racket.maneuverability) },
            { assertEquals(save.balance, racket.balance) },
            { assertEquals(save.rigidity, racket.rigidity) }
        )
    }

    @Test
    fun update() = runTest{
        val save = repo.save(racket)
        save.brand="Nuevo test"
        val update = repo.update(save)

        assertAll(
            { assertEquals(update.id, save.id) },
            { assertEquals(update.brand, save.brand) },
            { assertEquals(update.model, save.model) },
            { assertEquals(update.maneuverability, save.maneuverability) },
            { assertEquals(update.balance, save.balance) },
            { assertEquals(update.rigidity, save.rigidity) }
        )
    }

    @Test
    fun delete() = runTest{
        repo.save(racket)
        val delete = repo.delete(racket)
        assertTrue(delete)
    }

    @Test
    fun findAll() = runTest{
        repo.save(racket)
        val all = repo.findAll().toList()

        assertAll(
            { assertNotNull(all.isNotEmpty()) },
            { assertEquals(all[0].id, racket.id) },
            { assertEquals(all[0].brand, racket.brand) },
            { assertEquals(all[0].model, racket.model) },
            { assertEquals(all[0].maneuverability, racket.maneuverability) },
            { assertEquals(all[0].balance, racket.balance) },
            { assertEquals(all[0].rigidity, racket.rigidity) }
        )
    }

    @Test
    fun deleteAll() = runTest{
        repo.save(racket)
        val delete = repo.deleteAll()
        assertTrue(delete)
    }
}