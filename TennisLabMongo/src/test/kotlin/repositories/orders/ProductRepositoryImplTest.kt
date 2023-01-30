package repositories.orders

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import model.Product
import model.TypeProduct
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class ProductRepositoryImplTest {

    private var product =
        Product(
            type = TypeProduct.COMPLEMENTO,
            brand = "test brand",
            model = "test model",
            price = 10f,
            stock = 1
        )
    private val repo = ProductRepositoryImpl()

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
        repo.save(product)
        val res = repo.findById(product.id)
        assertAll(
            { assertNotNull(res) },
            { assertEquals(res?.id, product.id) },
            { assertEquals(res?.uuid, product.uuid) },
            { assertEquals(res?.type, product.type) },
            { assertEquals(res?.brand, product.brand) },
            { assertEquals(res?.model, product.model) },
            { assertEquals(res?.price, product.price) },
            { assertEquals(res?.stock, product.stock) }
        )
    }

    @Test
    fun save() = runTest {
        val res = repo.save(product)
        assertAll(
            { assertNotNull(res) },
            { assertEquals(res.id, product.id) },
            { assertEquals(res.uuid, product.uuid) },
            { assertEquals(res.type, product.type) },
            { assertEquals(res.brand, product.brand) },
            { assertEquals(res.model, product.model) },
            { assertEquals(res.price, product.price) },
            { assertEquals(res.stock, product.stock) }
        )

    }

    @Test
    fun update() = runTest {
        val save = repo.save(product)
        save.brand = "updated brand"
        val update = repo.update(save)
        assertAll(
            { assertEquals(update.id, product.id) },
            { assertEquals(update.uuid, product.uuid) },
            { assertEquals(update.type, product.type) },
            { assertEquals(update.brand, product.brand) },
            { assertEquals(update.model, product.model) },
            { assertEquals(update.price, product.price) },
            { assertEquals(update.stock, product.stock) }
        )

    }

    @Test
    fun delete() = runTest {
        repo.save(product)
        val res = repo.delete(product)
        assertTrue(res)
    }

    @Test
    fun findAll() = runTest {
        repo.save(product)
        val res = repo.findAll().toList()
        assertAll(
            { assertTrue(res.isNotEmpty()) },
            { assertEquals(res[0].id, product.id) },
            { assertEquals(res[0].uuid, product.uuid) },
            { assertEquals(res[0].type, product.type) },
            { assertEquals(res[0].brand, product.brand) },
            { assertEquals(res[0].model, product.model) },
            { assertEquals(res[0].price, product.price) },
            { assertEquals(res[0].stock, product.stock) }
        )
    }

    @Test
    fun deleteAll() = runTest {
        repo.save(product)
        val res = repo.deleteAll()
        assertTrue(res)
    }
}