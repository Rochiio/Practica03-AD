package repositories.orders

import dto.TaskDTO
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import model.TypeTask
import model.orders.tasks.Task
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.expect

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class TasksApiRepositoryTest {

    @MockK
    private var api = TasksApiRepository()

    private val taskDto = TaskDTO(true, 1, "title test", 1)
    private val task = Task(
        idEmployee = null,
        idCustomizer = null,
        idStringer = null,
        price = 10f,
        description = "description",
        taskType = TypeTask.ENCORDADO,
        available = true
    )
    init {
        MockKAnnotations.init(this)
    }
    @Test
    fun findAll() = runTest {

        coEvery { api.findAll(0, 1) } returns listOf(taskDto)
        val res = api.findAll(0, 1)
        assertAll(
            { assertTrue(res.isNotEmpty()) },
            { assertEquals(res[0].id, taskDto.id) },
            { assertEquals(res[0].title, taskDto.title) },
            { assertEquals(res[0].userId, taskDto.userId) }
        )
    }
    @Test
    fun findAllError() = runTest {
        coEvery { api.findAll(0,1)} returns emptyList()
        val res = api.findAll(0,1)
        assertTrue(res.isEmpty())
    }

    @Test
    fun findBYId() = runTest {
        coEvery { api.findBYId(1) } returns taskDto
        val res = api.findBYId(1)
        assertAll(

            { assertEquals(res?.id, taskDto.id) },
            { assertEquals(res?.title, taskDto.title) },
            { assertEquals(res?.userId, taskDto.userId) }
        )
    }

    @Test
    fun findByIdError() = runTest {
        coEvery { api.findBYId(-1) } returns null
        val res = api.findBYId(-1)
        assertNull(res)
    }

    @Test
    fun findByUserId() = runTest {
        coEvery { api.findByUserId(1) } returns taskDto
        val res = api.findByUserId(1)
        assertAll(

            { assertEquals(res?.id, taskDto.id) },
            { assertEquals(res?.title, taskDto.title) },
            { assertEquals(res?.userId, taskDto.userId) }
        )
    }
    @Test
    fun findByUserError() = runTest {
        coEvery { api.findByUserId(-1) } returns null
        val res = api.findByUserId(-1)
        assertNull(res)
    }
    @Test
    fun save() = runTest {
        coEvery { api.save(task) } returns task
        val res = api.save(task)
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
        coEvery { api.update(task) } returns task
        coEvery { api.save(task) } returns task
        val save = api.save(task)
        save.taskType = TypeTask.ADQUISICION
        val res = api.update(save)
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
}