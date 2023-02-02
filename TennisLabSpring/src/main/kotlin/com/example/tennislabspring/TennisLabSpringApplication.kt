package com.example.tennislabspring

import com.example.tennislabspring.model.lists.*
import com.example.tennislabspring.service.cache.UsersCache
import com.example.tennislabspring.service.files.*
import com.example.tennislabspring.service.reactive.Watchers
import com.example.tennislabspring.util.Data
import com.example.tennislabspring.view.Vista
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.system.exitProcess


val t = Terminal()
val properties = Properties()
@SpringBootApplication
class TennisLabSpringApplication
@Autowired constructor(
	private val watchers: Watchers,
	private val vista: Vista,
):CommandLineRunner {

	override fun run(vararg args: String?) = runBlocking{
			properties.load(javaClass.classLoader.getResourceAsStream("database.properties"))
			clearData()
			sampleData()

			var salir = false
			withContext(Dispatchers.IO) {
				val cache = launch {
					do {
						UsersCache.refresh()
					} while ((!salir))
				}
				val w = launch {
					launch {
						watchers.watchOrder()
							.collect { println("\uD83D\uDC49 Evento: ${it.operationType?.value} -> ${it.body}") }
					}
				}


				launch {
					vista.runVista()
					salir = true
					cache.cancel()
					w.cancel()
				}
			}

			//makeJsonListas()
			exitProcess(0)
		}

	}

	suspend fun clearData() {
		var clear = properties.getProperty("database.clear").toBoolean()
		if (clear) {
			t.println(TextColors.green("🤖🧹 LIMPIANDO DATOS DEL PROGRAMA"))
//TODO("SAmple data")

		}
	}

	suspend fun sampleData() {
		var sample = properties.getProperty("database.sample").toBoolean()
		if (sample) {
			t.println(TextColors.green("🤖🔋 CARGANDO DATOS DE PRUEBA"))
			//TODO("SAmple data")
		}
	}

	/**
	 * Hacer los ficheros con json
	 */
	suspend fun makeJsonListas() = withContext(Dispatchers.IO) {
		if (!Files.isDirectory(Path.of(Data.DIR_JSON))) {
			Files.createDirectories(Path.of(Data.DIR_JSON))
		}

		val productos = async {
			makeListadoProductosServicios()
		}

		val asignaciones = async {
			makeListadoAsignacionesEncordadores()
		}

		val pendientes = async {
			makeListadoPedidosPendientes()
		}

		val completados = async {
			makeListadoPedidosCompletados()
		}

		val pedido = async {
			makePedido()
		}


		productos.await()
		asignaciones.await()
		pendientes.await()
		completados.await()
		pedido.await()
	}

	/**
	 * Crear fichero de pedido completados.
	 */
	suspend fun makePedido() = withContext(Dispatchers.IO) {
		t.println(TextColors.cyan.bg("pedido.json ..."))
		var job = async {
			JsonFilesOrder().writeFichero(
				Data.DIR_JSON + "pedido.json",
				Data.completeOrders[0]
			)
		}

		job.await()
	}

	/**
	 * Crear fichero de pedidos completados.
	 */
	suspend fun makeListadoPedidosCompletados() = withContext(Dispatchers.IO) {
		t.println(TextColors.cyan.bg("listado_pedidos_completados.json ..."))
		var job = async {
			JsonFilesCompleteOrders().writeFichero(
				Data.DIR_JSON + "listado_pedidos_completados.json",
				CompleteOrdersList(Data.completeOrders.toList())
			)
		}

		job.await()
	}


	/**
	 * Crear fichero de pedidos pendientes
	 */
	suspend fun makeListadoPedidosPendientes() = withContext(Dispatchers.IO) {
		t.println(TextColors.cyan.bg("listado_pedidos_pendientes.json ..."))

		val job = async {
			JsonFilesPendingOrders().writeFichero(
				Data.DIR_JSON + "listado_pedidos_pendientes.json",
				PendingOrdersList(Data.pendingOrders.toList())
			)
		}
		job.await()
	}


	/**
	 * Crear fichero de asignaciones de encordadores
	 */
	suspend fun makeListadoAsignacionesEncordadores() = withContext(Dispatchers.IO) {
		t.println(TextColors.cyan.bg("listado_asignaciones_encordadores.json ..."))

		val job = async {
			JsonFilesAssignments().writeFichero(
				Data.DIR_JSON + "listado_asignaciones_encordadores.json",
				StringerAssignmentsList(Data.assignments.toList())
			)
		}

		job.await()
	}


	/**
	 * Crear fichero de productos y servicios
	 */
	suspend fun makeListadoProductosServicios() = withContext(Dispatchers.IO) {
		t.println(TextColors.cyan.bg("listado_productos_servicios.json ..."))

		val job = async {
			JsonFilesProductsServices().writeFichero(
				Data.DIR_JSON + "listado_productos_servicios.json",
				ListProductsServices(Data.services, Data.products)
			)
		}

		job.await()
	}



fun main(args: Array<String>) {
	runApplication<TennisLabSpringApplication>(*args)
}

