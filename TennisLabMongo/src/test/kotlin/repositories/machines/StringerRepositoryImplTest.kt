package repositories.machines

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import model.TypeMachine
import model.machines.Stringer
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class StringerRepositoryImplTest{
    private val repo = StringerRepositoryImpl()
    private val stringer = Stringer(brand = "test", model = "test", acquisitionDate = LocalDate.now(), available = true,
        automatic = TypeMachine.MANUAL, maximumTension = 50, minimumTension = 10)

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
        repo.save(stringer)
        val find = repo.findById(stringer.id)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(find?.id, stringer.id) },
            { assertEquals(find?.brand, stringer.brand) },
            { assertEquals(find?.model, stringer.model) },
            { assertEquals(find?.acquisitionDate, stringer.acquisitionDate) },
            { assertEquals(find?.available, stringer.available) },
            { assertEquals(find?.automatic, stringer.automatic) },
            { assertEquals(find?.maximumTension, stringer.maximumTension) },
            { assertEquals(find?.minimumTension, stringer.minimumTension) }
        )
    }

    @Test
    fun save() = runTest{
        val save = repo.save(stringer)

        assertAll(
            { assertEquals(save.id, stringer.id) },
            { assertEquals(save.brand, stringer.brand) },
            { assertEquals(save.model, stringer.model) },
            { assertEquals(save.acquisitionDate, stringer.acquisitionDate) },
            { assertEquals(save.available, stringer.available) },
            { assertEquals(save.automatic, stringer.automatic) },
            { assertEquals(save.maximumTension, stringer.maximumTension) },
            { assertEquals(save.minimumTension, stringer.minimumTension) }
        )
    }

    @Test
    fun update() = runTest{
        val save = repo.save(stringer)
        save.available = false
        val update = repo.update(save)

        assertAll(
            { assertEquals(update.id, save.id) },
            { assertEquals(update.brand, save.brand) },
            { assertEquals(update.model, save.model) },
            { assertEquals(update.acquisitionDate, save.acquisitionDate) },
            { assertEquals(update.available, save.available) },
            { assertEquals(update.automatic, save.automatic) },
            { assertEquals(update.maximumTension, save.maximumTension) },
            { assertEquals(update.minimumTension, save.minimumTension) }
        )
    }

    @Test
    fun delete() = runTest{
        repo.save(stringer)
        var delete = repo.delete(stringer)
        assertTrue(delete)
    }

    @Test
    fun findAll() = runTest{
        repo.save(stringer)
        var all = repo.findAll().toList()

        assertAll(
            { assertTrue(all.isNotEmpty()) },
            { assertEquals(all[0].id, stringer.id) },
            { assertEquals(all[0].brand, stringer.brand) },
            { assertEquals(all[0].model, stringer.model) },
            { assertEquals(all[0].acquisitionDate, stringer.acquisitionDate) },
            { assertEquals(all[0].available, stringer.available) },
            { assertEquals(all[0].automatic, stringer.automatic) },
            { assertEquals(all[0].maximumTension, stringer.maximumTension) },
            { assertEquals(all[0].minimumTension, stringer.minimumTension) }
        )
    }

    @Test
    fun deleteAll() = runTest{
        repo.save(stringer)
        var delete = repo.deleteAll()
        assertTrue(delete)
    }
}