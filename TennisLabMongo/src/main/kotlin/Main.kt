import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import model.lists.CompleteOrdersList
import model.lists.ListProductsServices
import model.lists.PendingOrdersList
import model.lists.StringerAssignmentsList
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import service.files.*
import util.Data
import view.Vista
import java.nio.file.Files
import java.nio.file.Path
import kotlin.system.exitProcess

val t = Terminal()
fun main(args: Array<String>): Unit = runBlocking {
    startKoin {
        defaultModule()
    }
    KoinApp().run()
}

class KoinApp : KoinComponent {
    private val vista: Vista by inject()

    suspend fun run() {
        do {
            val num = vista.principal()
            vista.opcionesPrincipal(num)
        } while (num != 0)

        //makeJsonListas()
        exitProcess(0)
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

    val pedido = async{
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
