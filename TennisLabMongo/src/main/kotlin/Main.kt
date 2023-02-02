import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import db.MongoDbManager
import kotlinx.coroutines.*
import kotlinx.coroutines.reactive.collect
import model.lists.CompleteOrdersList
import model.lists.ListProductsServices
import model.lists.PendingOrdersList
import model.lists.StringerAssignmentsList
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import service.cache.UsersCache
import service.files.*
import service.reactive.Watchers
import util.Data
import view.Vista
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties
import kotlin.system.exitProcess

val t = Terminal()
val properties = Properties()
fun main(args: Array<String>): Unit = runBlocking {

    properties.load(javaClass.classLoader.getResourceAsStream("application.properties"))
    clearData()
    sampleData()
    startKoin {
        defaultModule()
    }

    KoinApp().run()
}

class KoinApp : KoinComponent {
    private val vista: Vista by inject()
    val watchers = Watchers()

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun run(): Unit = runBlocking {
        var salir = false
        withContext(Dispatchers.IO) {
            var cache = launch {
                do {
                    UsersCache.refresh()
                } while ((!salir))
            }
            val w = launch {

                launch {
                    watchers.watchCustomers()
                        .collect { println("\uD83D\uDC49 Evento: ${it.operationType.value} -> ${it.fullDocument}") }
                }
                launch {
                    watchers.watchEmployee()
                        .collect { println("\uD83D\uDC49 Evento: ${it.operationType.value} -> ${it.fullDocument}") }
                }
                launch {
                    watchers.watchOrder()
                        .collect { println("\uD83D\uDC49 Evento: ${it.operationType.value} -> ${it.fullDocument}") }
                }
                launch {
                    watchers.watchProduct()
                        .collect { println("\uD83D\uDC49 Evento: ${it.operationType.value} -> ${it.fullDocument}") }
                }
                launch {
                    watchers.watchTask()
                        .collect { println("\uD83D\uDC49 Evento: ${it.operationType.value} -> ${it.fullDocument}") }
                }
                launch {
                    watchers.watchCustomizer()
                        .collect { println("\uD83D\uDC49 Evento: ${it.operationType.value} -> ${it.fullDocument}") }
                }
                launch {
                    watchers.watchRacket()
                        .collect { println("\uD83D\uDC49 Evento: ${it.operationType.value} -> ${it.fullDocument}") }
                }
                launch {
                    watchers.watchStringer()
                        .collect { println("\uD83D\uDC49 Evento: ${it.operationType.value} -> ${it.fullDocument}") }
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

        MongoDbManager.database.dropCollection("customer")
        MongoDbManager.database.dropCollection("customizer")
        MongoDbManager.database.dropCollection("employee")
        MongoDbManager.database.dropCollection("order")
        MongoDbManager.database.dropCollection("product")
        MongoDbManager.database.dropCollection("racket")
        MongoDbManager.database.dropCollection("stringer")
        MongoDbManager.database.dropCollection("task")
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
