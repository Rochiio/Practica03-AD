package repositories.machines

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import model.machines.Customizer
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDate

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class CustomizerRepositoryImplTest {
    private val repo = CustomizerRepositoryImpl()
    private val customizer = Customizer(brand = "test", model = "test", acquisitionDate = LocalDate.now(), available = true,
        maneuverability = true, balance = false, rigidity = true)

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
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }


    @Test
    fun findById() = runTest{
        repo.save(customizer)
        val find = repo.findById(customizer.id)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(find?.id, customizer.id) },
            { assertEquals(find?.brand, customizer.brand) },
            { assertEquals(find?.model, customizer.model) },
            { assertEquals(find?.acquisitionDate, customizer.acquisitionDate) },
            { assertEquals(find?.available, customizer.available) },
            { assertEquals(find?.maneuverability, customizer.maneuverability) },
            { assertEquals(find?.balance, customizer.balance) },
            { assertEquals(find?.rigidity, customizer.rigidity) }
        )
    }

    @Test
    fun save() =runTest{
        val save = repo.save(customizer)

        assertAll(
            { assertEquals(save.id, customizer.id) },
            { assertEquals(save.brand, customizer.brand) },
            { assertEquals(save.model, customizer.model) },
            { assertEquals(save.acquisitionDate, customizer.acquisitionDate) },
            { assertEquals(save.available, customizer.available) },
            { assertEquals(save.maneuverability, customizer.maneuverability) },
            { assertEquals(save.balance, customizer.balance) },
            { assertEquals(save.rigidity, customizer.rigidity) }
        )
    }

    @Test
    fun update() = runTest{
        val save = repo.save(customizer)
        save.available = false
        val update = repo.update(save)

        assertAll(
            { assertEquals(update.id, save.id) },
            { assertEquals(update.brand, save.brand) },
            { assertEquals(update.model, save.model) },
            { assertEquals(update.acquisitionDate, save.acquisitionDate) },
            { assertEquals(update.available, save.available) },
            { assertEquals(update.maneuverability, save.maneuverability) },
            { assertEquals(update.balance, save.balance) },
            { assertEquals(update.rigidity, save.rigidity) }
        )
    }

    @Test
    fun delete() = runTest{
        repo.save(customizer)
        var delete = repo.delete(customizer)
        assertTrue(delete)
    }

    @Test
    fun findAll() =runTest{
        repo.save(customizer)
        var all = repo.findAll().toList()

        assertAll(
            { assertTrue(all.isNotEmpty()) },
            { assertEquals(all[0].id, customizer.id) },
            { assertEquals(all[0].brand, customizer.brand) },
            { assertEquals(all[0].model, customizer.model) },
            { assertEquals(all[0].acquisitionDate, customizer.acquisitionDate) },
            { assertEquals(all[0].available, customizer.available) },
            { assertEquals(all[0].maneuverability, customizer.maneuverability) },
            { assertEquals(all[0].balance, customizer.balance) },
            { assertEquals(all[0].rigidity, customizer.rigidity) }
        )
    }

    @Test
    fun deleteAll() = runTest{
        repo.save(customizer)
        var delete = repo.deleteAll()
        assertTrue(delete)
    }
}