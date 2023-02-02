package controllers

import exception.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import model.Product
import model.TypeProduct
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

import repositories.orders.ProductRepositoryImpl
import service.reactive.Watchers
import kotlin.test.assertEquals

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
internal class ProductControllerTest {
    private var product =
        Product(
            type = TypeProduct.COMPLEMENTO,
            brand = "test brand",
            model = "test model",
            price = 10f,
            stock = 1
        )
    @MockK
    private lateinit var repository : ProductRepositoryImpl
    @MockK
    private lateinit var watchers: Watchers
    @InjectMockKs
    private lateinit var controller : ProductController

    init {
        MockKAnnotations.init(this)
    }
    @Test
    fun addProduct() = runTest{
        coEvery { repository.save(product) } returns product
        coEvery { repository.findById(product.id) } returns null

        val result = controller.addProduct(product)
        val res = result as ProductSuccess

        assertAll(
            { assertEquals(201, res.code) },
            { assertEquals(res.data.id, product.id) },
            { assertEquals(res.data.uuid, product.uuid) },
            { assertEquals(res.data.type, product.type) },
            { assertEquals(res.data.brand, product.brand) },
            { assertEquals(res.data.model, product.model) },
            { assertEquals(res.data.price, product.price) },
            { assertEquals(res.data.stock, product.stock) }
        )
    }

    @Test
    fun addProductError() = runTest {
        coEvery { repository.findById(product.id) } returns product
        val result = controller.addProduct(product)
        val res = result as ProductErrorExists

        Assertions.assertAll(
            { Assertions.assertEquals(403, res.code) },
            { Assertions.assertEquals( "Ya existe un producto con este id",res.message) }
        )
    }
    @Test
    fun getProductById() = runTest{
        coEvery { repository.findById(product.id) } returns product
        val result = controller.getProductById(product.id)
        val res = result as ProductSuccess
        assertAll(
            { assertEquals(200, res.code) },
            { assertEquals(res.data.id, product.id) },
            { assertEquals(res.data.uuid, product.uuid) },
            { assertEquals(res.data.type, product.type) },
            { assertEquals(res.data.brand, product.brand) },
            { assertEquals(res.data.model, product.model) },
            { assertEquals(res.data.price, product.price) },
            { assertEquals(res.data.stock, product.stock) }
        )
    }

    @Test
    fun getProductByIdError() = runTest {
        coEvery { repository.findById("") } returns null
        val result = controller.getProductById("")
        val res = result as ProductErrorNotFound
        assertAll(
            { Assertions.assertEquals(404, res.code) },
            { Assertions.assertEquals( "No existe un producto con este id" ,res.message)}
        )
    }
    @Test
    fun updateProduct() = runTest{
        coEvery { repository.update(product) } returns product
        val result = controller.updateProduct(product)
        val res = result as ProductSuccess
        Assertions.assertAll(
            { assertEquals(200, res.code) },
            { assertEquals(res.data.id, product.id) },
            { assertEquals(res.data.uuid, product.uuid) },
            { assertEquals(res.data.type, product.type) },
            { assertEquals(res.data.brand, product.brand) },
            { assertEquals(res.data.model, product.model) },
            { assertEquals(res.data.price, product.price) },
            { assertEquals(res.data.stock, product.stock) }
        )

    }

    @Test
    fun getAllProducts() = runTest{
        coEvery { repository.findAll() } returns flowOf(product)
        val result = controller.getAllProducts()
        val res = result as ProductSuccess
        val list = res.data.toList()
        Assertions.assertAll(
            { Assertions.assertTrue(res.code == 200) },
            { Assertions.assertTrue(list.isNotEmpty()) }
        )
    }

    @Test
    fun deleteProduct() = runTest {
        coEvery { repository.delete(product) } returns true
        val result = controller.deleteProduct(product)
        val res = result as ProductSuccess
        Assertions.assertAll(
            { Assertions.assertEquals(200, res.code) },
            { Assertions.assertTrue(res.data) }
        )
    }
}