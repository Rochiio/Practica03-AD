package repositories.orders

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import model.TypeTask
import model.orders.tasks.Task
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class TaskRepositoryImplTest {

    private val repo = TaskRepositoryImpl()
    private val task = Task(
        nId = 1,
        idEmployee = null,
        idStringer = null,
        idCustomizer = null,
        description = "description",
        taskType = TypeTask.ADQUISICION,
        available = true,
        price = 10f
    )

    @Before
    fun setUp() = runTest {
        repo.deleteAll()
    }

    @AfterEach
    fun tearDown() = runTest {
        repo.deleteAll()
    }

    @Test
    fun findById() = runTest {
        repo.save(task)
        val res = repo.findById(task.id)
        assertAll(
            { assertNotNull(res) },
            { assertEquals(res?.id, task.id) },
            { assertEquals(res?.nId, task.nId) },
            { assertEquals(res?.uuid, task.uuid) },
            { assertEquals(res?.idEmployee, task.idEmployee) },
            { assertEquals(res?.idStringer, task.idStringer) },
            { assertEquals(res?.idCustomizer, task.idCustomizer) },
            { assertEquals(res?.description, task.description) },
            { assertEquals(res?.taskType, task.taskType) },
            { assertEquals(res?.available, task.available) }
        )
    }

    @Test
    fun save() = runTest {
        val res = repo.save(task)
        assertAll(

            { assertEquals(res.id, task.id) },
            { assertEquals(res.nId, task.nId) },
            { assertEquals(res.uuid, task.uuid) },
            { assertEquals(res.idEmployee, task.idEmployee) },
            { assertEquals(res.idStringer, task.idStringer) },
            { assertEquals(res.idCustomizer, task.idCustomizer) },
            { assertEquals(res.description, task.description) },
            { assertEquals(res.taskType, task.taskType) },
            { assertEquals(res.available, task.available) }
        )
    }

    @Test
    fun update() = runTest {
        val save = repo.save(task)
        save.taskType = TypeTask.ENCORDADO
        val res = repo.update(save)
        assertAll(

            { assertEquals(res.id, save.id) },
            { assertEquals(res.nId, save.nId) },
            { assertEquals(res.uuid, save.uuid) },
            { assertEquals(res.idEmployee, save.idEmployee) },
            { assertEquals(res.idStringer, save.idStringer) },
            { assertEquals(res.idCustomizer, save.idCustomizer) },
            { assertEquals(res.description, save.description) },
            { assertEquals(res.taskType, save.taskType) },
            { assertEquals(res.available, save.available) }
        )

    }

    @Test
    fun delete() = runTest {
        repo.save(task)
        val res = repo.delete(task)
        assertTrue(res)
    }

    @Test
    fun findAll() = runTest {
        repo.save(task)
        val res = repo.findAll().toList()
        assertAll(

            { assertEquals(res[0].id, task.id) },
            { assertEquals(res[0].nId, task.nId) },
            { assertEquals(res[0].uuid, task.uuid) },
            { assertEquals(res[0].idEmployee, task.idEmployee) },
            { assertEquals(res[0].idStringer, task.idStringer) },
            { assertEquals(res[0].idCustomizer, task.idCustomizer) },
            { assertEquals(res[0].description, task.description) },
            { assertEquals(res[0].taskType, task.taskType) },
            { assertEquals(res[0].available, task.available) }
        )
    }

    @Test
    fun deleteAll() = runTest {
        repo.save(task)
        val res = repo.deleteAll()
        assertTrue(res)
    }
}