package view

import com.github.ajalt.mordant.rendering.TextAlign
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import controller.*
import controllers.*
import exception.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.*
import model.lists.StringerAssignments
import model.machines.Customizer
import model.machines.Stringer
import model.orders.Order
import model.orders.tasks.Customization
import model.orders.tasks.Purchase
import model.orders.tasks.Stringing
import model.orders.tasks.Task
import model.users.Customer
import model.users.Employee
import org.koin.core.annotation.Single
import repositories.users.CustomerApiRepository
import util.Data
import service.PasswordParser
import util.mappers.fromDto
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.collections.ArrayList

/**
 * Vista del usuario.
 */
@Single
class Vista(
    private var employeeController: EmployeeController,
    private var machineController: MachineController,
    private var customerController: CustomerController,
    private var orderController: OrderController,
    private var taskController: TaskController,
    private var productController: ProductController,
    private var racketController: RacketController,

    ) {
    private var terminal = Terminal(width = 150)
    private var loggedEmployee: Employee? = null
    private var loggedCustomer: Customer? = null
    private var apiUsers = CustomerApiRepository()
    //private var apiTasks = TasksApiRepository()

    suspend fun runVista() {
        initUsers()
        val opt = principal()
        opcionesPrincipal(opt)
    }

    suspend fun initUsers() {
        val admin = Employee(
            name = "admin",
            surname = "admin",
            email = "admin@admin.com",
            password = PasswordParser.encriptar("1234"),
            available = true,
            isAdmin = true,
            entryTime = null,
            departureTime = null,
            orderList = mutableListOf()
        )

        employeeController.addEmployee(admin)
        val users = apiUsers.findAll(0, 100)
        users.forEach { customerController.addCustomer(it.fromDto()) }

    }

    /**
     * Funcion principal para el inicio
     */

     fun principal(): Int {



        var opcion: Int
        do {
            terminal.println(brightBlue("------ Bienvenido a tennislab🎾🎾 ------ \nelija una opción."))
            terminal.println(
                "1 - Iniciar sesión (Trabajador) \n" +
                        "2 - Iniciar sesión (Cliente)\n" +
                        "0 - Salir"
            )
            opcion = readln().toIntOrNull() ?: 3
        } while (opcion !in 0..2)
        return opcion
    }


    /**
     * Acciones a realizar dependiendo de la respuesta en la funcion principal
     */
    suspend fun opcionesPrincipal(num: Int) {
        when (num) {
            0 -> terminal.println(green.bg("Gracias por usar tennislab 🎾🎾"))
            1 -> loginBucle()
            2 -> loginClienteBucle()
        }
    }


    /**
     * Bucle de login del cliente.
     */
    private suspend fun loginClienteBucle() {
        var email: String
        terminal.println(brightBlue("------ Log In Cliente ------"))
        do {
            terminal.print("Correo: ")
            email = readln()
        } while (!email.matches(Regex("[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}")))

        terminal.print("Contraseña: ")
        val password: String = readln()

        when (val result = customerController.getCustomerByEmailAndPassword(email, password)) {
            is CustomerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is CustomerSuccess -> {
                loggedCustomer = result.data
                clienteBucle()
            }
        }
    }


    /**
     * Bucle de opciones del cliente loggeado
     */
    private suspend fun clienteBucle() {
        var opcion: Int
        do {
            terminal.println(brightBlue("------ Cliente ------"))
            do {
                terminal.println(
                    "1- Realizar pedido \n" +
                            "2- Comprobar pedidos \n" +
                            "3- Cancelar pedido \n" +
                            "4- Añadir Raqueta \n" +
                            "5- Actualizar Raqueta \n" +
                            "6- Eliminar Raqueta \n" +
                            "7- Mostrar Raquetas \n" +
                            "0- Salir"
                )
                opcion = readln().toIntOrNull() ?: -1
            } while (opcion !in 0..7)
            opcionesClienteBucle(opcion)
        } while (opcion != 0)


    }


    private suspend fun opcionesClienteBucle(opcion: Int) {
        when (opcion) {
            1 -> realizarPedidoBucle()
            2 -> comprobarPedidosBucle()
            3 -> cancelarPedidoBucle()
            4 -> addRaqueta()
            5 -> actuRaqueta()
            6 -> deleteRaqueta()
            7 -> getRackets()
            0 -> terminal.println(blue.bg("Saliendo de la sesión"))
        }
    }


    private suspend fun opcionesBucleTarea(opcion: Int) {
        when (opcion) {
            1 -> {
                val tarea = creacionTareaEncordado()
                taskController.addTask(tarea)
                Data.tasksCreated.add(tarea)
            }

            2 -> {
                val tarea = creacionTareaPersonalizado()
                taskController.addTask(tarea)
                Data.tasksCreated.add(tarea)
            }

            3 -> {
                val tarea = createTareaAdquisicion()
                taskController.addTask(tarea)
                Data.tasksCreated.add(tarea)
            }

            4 -> {
                val pedido = creacionPedido(Data.tasksCreated)
                pedido.let {
                    Data.pendingOrders.add(pedido)
                    orderController.addOrder(pedido)
                    terminal.println("Pedido creado")
                }
                when (val result = orderController.getOrders()) {
                    is OrderSuccess -> getPedidos(result.data.toList())
                    is OrderError -> terminal.println(red("❌${result.code}: ${result.message}"))

                }
            }

            0 -> terminal.println(blue.bg("Saliendo de la creacion de tareas"))
        }
    }


    //------------------------------------------------ PEDIDOS -------------------------------------------------

    /**
     * Bucle de pedidos para los trabajadores
     */
    private suspend fun administradorBuclePedidos() {
        var opcion: Int
        do {
            terminal.println(brightBlue("------ Pedidos ------"))
            do {
                terminal.println(
                    "1- Asignar Pedido \n" +
                            "2- Completar Pedido \n" +
                            "0- Salir"
                )
                opcion = readln().toIntOrNull() ?: -1
            } while (opcion < 0 || opcion > 4)
            opcionesBucleAdminPedidos(opcion)
        } while (opcion != 0)
    }


    /**
     * Opciones del bucle de pedidos de los administradores y trabajadores
     */
    private suspend fun opcionesBucleAdminPedidos(opcion: Int) {
        when (opcion) {
            1 -> asignarPedidoTrabajador()
            2 -> completarPedidoTrabajador()
            0 -> terminal.println(brightBlue.bg("Saliendo de pedidos🛒🛒"))
        }
    }


    /**
     * Completar un pedido del trabajador.
     */
    private suspend fun completarPedidoTrabajador() {
        var lista = mutableListOf<Order>()
        loggedEmployee?.orderList?.forEach {
            when (val result = orderController.getOrderById(it)) {
                is OrderError -> TODO()
                is OrderSuccess -> lista.add(result.data)
            }
        }

        lista = lista.filter { it.state == Status.EN_PROCESO }.toMutableList()
        getPedidos(lista)
        if (lista.isNotEmpty()) {
            print("Elija el pedido a completar por índice:")
            val indice = readln().toIntOrNull() ?: -1

            if (indice < 0 || lista.size < indice) {
                terminal.println(red("❌Indice de pedido incorrecto"))
            } else {
                val pedido = lista[indice]
                loggedEmployee?.orderList?.remove(pedido.id)
                pedido.state = Status.TERMINADO
                pedido.finalDate = LocalDate.now()
                orderController.updateOrder(pedido)
                Data.completeOrders.add(pedido)
                withContext(Dispatchers.IO) {
                    launch { employeeController.updateEmployee(loggedEmployee!!) }
                }
            }
        }
    }


    /**
     * Asignarle al trabajador un pedido.
     * Dependiendo de si ha llegado a su tope en el turno y si elige correctamente el pedido.
     */
    private suspend fun asignarPedidoTrabajador() {
        if (loggedEmployee?.orderList?.size!! < 2) {
            when (val result = orderController.getOrders()) {
                is OrderError -> terminal.println(red("❌${result.code}: ${result.message}"))
                is OrderSuccess -> {
                    val lista = result.data.toList().filter { it.state == Status.RECIBIDO }
                    getPedidos(lista)
                    print("Elija el pedido a realizar por índice:")
                    val indice = readln().toIntOrNull() ?: -1
                    if (indice < 0 || indice >= lista.size) {
                        terminal.println(red("❌Indice de pedido incorrecto"))
                    } else {
                        val pedido = lista[indice]
                        Data.pendingOrders.remove(pedido)

                        withContext(Dispatchers.IO) {
                            launch {
                                pedido.state = Status.EN_PROCESO

                                pedido.tasks.forEach { it.idEmployee = loggedEmployee?.id}

                                orderController.updateOrder(pedido)
                            }
                            launch {
                                val list = loggedEmployee?.orderList?.toMutableList()
                                list?.add(pedido.id)
                                loggedEmployee?.orderList = list
                                employeeController.updateEmployee(loggedEmployee!!)
                            }

                            launch {
                                val assignament = StringerAssignments(pedido.id, loggedEmployee!!)
                                Data.assignments.add(assignament)
                            }
                        }
                    }
                }
            }
        } else {
            terminal.println(red("❌Trabajador ${loggedEmployee?.name} ha llegado a el tope de pedidos en un turno"))
        }
    }


    private suspend fun cancelarPedidoBucle() {
        terminal.println("Introduce el pedido que quieres eliminar (selecciona con el indice): ")
        var lista: List<Order> = listOf()
        when (val result = orderController.getOrders()) {
            is OrderError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is OrderSuccess -> {
                lista = result.data.toList().filter { it.client == loggedCustomer }
            }
        }

        getPedidos(lista)
        val indice = readln().toIntOrNull() ?: -1
        if (indice < 0 || indice > lista.size) {
            terminal.println(red("INDICE DE PEDIDO INCORRECTO"))
        } else {
            orderController.deleteOrder(lista[indice])
        }
    }


    private suspend fun comprobarPedidosBucle() {
        when (val result = orderController.getOrders()) {
            is OrderError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is OrderSuccess -> {
                val lista = result.data.toList().filter { it.client == loggedCustomer }
                getPedidos(lista)
            }
        }
    }

    private suspend fun realizarPedidoBucle() {
        var opcion: Int
        //var pedido: Order? = null
        terminal.println(brightBlue("------ Realización de pedido ------"))

        do {
            terminal.println(brightBlue("------ Tipo de tarea ------"))
            do {
                terminal.println(
                    "1- Encordado \n" +
                            "2- Personalizado \n" +
                            "3- Adquisicion \n" +
                            "4- Confirmar pedido\n" +
                            "0- Salir"
                )
                opcion = readln().toIntOrNull() ?: -1
            } while (opcion < 0 || opcion > 4)
            opcionesBucleTarea(opcion)
        } while (opcion != 0 && opcion != 4)
    }

    private fun creacionPedido(tareas: MutableList<Task>): Order {
        val pedido: Order?
        var precio = 0F
        tareas.forEach { precio += it.price }
        println(precio)
        pedido = Order(
            state = Status.RECIBIDO,
            entryDate = LocalDate.now(),
            exitDate = LocalDate.parse("2023-12-31"),
            finalDate = null,
            totalPrice = precio,
            maxDate = LocalDate.now().plusDays(15),
            client = loggedCustomer!!,
            tasks = tareas as ArrayList<Task>
        )
        Data.tasksCreated.clear()

        return pedido
    }


    //----------------------------------------------- TAREAS ---------------------------------------------------------


    suspend fun creacionTareaEncordado(): Task {
        val tarea: Task?

        terminal.println("Indica la tensión vertical: ")
        val tV = readln().toIntOrNull() ?: -1

        terminal.println("Indica la tensión horizontal: ")
        val tH = readln().toIntOrNull() ?: -1


        var id : Int
        val result = productController.getAllProducts()
        var products: List<Product> = mutableListOf()
        when (result) {
            is ProductError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is ProductSuccess -> products = products + result.data.toList().filter { it.type == TypeProduct.CORDAJE }

        }
        do {
            terminal.println("Indica el cordaje vertical (escribe el indice): ")
            getCordajes()
            terminal.println("ID: ")
            id = readln().toIntOrNull() ?: 0
        } while (id !in (products.indices))
        val cV = products[id]

        do {
            terminal.println("Indica el cordaje horizontal (escribe el indice): ")
            getCordajes()
            terminal.println("ID: ")
            id = readln().toIntOrNull() ?: 0
        } while (id !in (products.indices))
        val cH = products[id]

        terminal.println("Numero de nudos que quieres (2 o 4): ")
        val nudos = readln().toIntOrNull() ?: 2
        val json: Json = Json
        val precio = cH.price + cV.price + 15L
        terminal.println("Selecciona una raqueta: ")
        getRackets()
        val racket = readln().toIntOrNull() ?: 0
        val descripcion = json.encodeToString(
            Stringing(
                hTension = tH,
                vTension = tV,
                vString = cV,
                hString = cH,
                nKnots = nudos,
                price = precio.toLong(),
                racketId = loggedCustomer!!.tennisRacketsList[racket]
            )
        )

        tarea = Task(
            idEmployee = null, idStringer = null, idCustomizer = null,
            price = precio,
            description = descripcion,
            taskType = TypeTask.ENCORDADO,
            available = true
        )
        tarea.let {
            taskController.addTask(tarea)
        }
        return tarea
    }

    suspend fun creacionTareaPersonalizado(): Task {
        val tarea: Task?

        terminal.println("Indica el nuevo peso de la raqueta: ")
        val peso = readln().toIntOrNull() ?: -1

        terminal.println("Indica el nuevo balance de la raqueta: ")
        val balance = readln().toFloatOrNull() ?: -1F

        terminal.println("Indica la nueva rigidez de la raqueta: ")
        val rigidez = readln().toIntOrNull() ?: -1

        terminal.println("Selecciona una raqueta: ")
        getRackets()
        val racket = readln().toIntOrNull() ?: 0
        val json: Json = Json
        val precio = 10L

        val descripcion = json.encodeToString(
            Customization(
                weight = peso,
                balance = balance, stiffness = rigidez,
                racket_id = loggedCustomer!!.tennisRacketsList[racket],
                price = precio
            )
        )

        tarea = Task(
            idEmployee = null, idStringer = null, idCustomizer = null,
            price = precio.toFloat(),
            description = descripcion,
            taskType = TypeTask.PERSONALIZADO,
            available = true
        )
        tarea.let {
            taskController.addTask(tarea)
        }
        return tarea
    }


    private suspend fun createTareaAdquisicion(): Task {
        val tarea: Task?
        val productos = mutableListOf<Product>()
        var repeat: Boolean
        do {
            repeat = false
            terminal.println("Selecciona el producto que vas a comprar (marca el indice):")
            var index: Int
            getProductos()
            index = readln().toIntOrNull() ?: -1
            while (index == -1) {
                terminal.println(red("INDICE INCORRECTO. VUELVE A INTENTARLO"))
                getProductos()
                index = readln().toIntOrNull() ?: -1
            }
            terminal.println("Añadiendo producto al carrito")
            when (val result = productController.getAllProducts()) {
                is ProductError -> terminal.println(red("❌${result.code}: ${result.message}"))
                is ProductSuccess -> {
                    val list = result.data.toList()
                    productos.add(list[index])
                }
            }
            terminal.println("¿Quieres añadir otro producto? (S/N)")
            val opt = readln()
            if (opt.equals("s", ignoreCase = true))
                repeat = true
        } while (repeat)
        var precio = 0F
        productos.forEach { precio += it.price }
        val json: Json = Json
        val descripcion = json.encodeToString(Purchase(price = precio, product = productos))

        tarea = Task(
            idEmployee = null,
            idStringer = null,
            idCustomizer = null,
            description = descripcion,
            price = precio,
            taskType = TypeTask.ADQUISICION,
            available = true
        )
        return tarea
    }

    private fun getPedidos(lista: List<Order>) {
        var indice = 0
        if (lista.isEmpty()) {
            println("Lista vacía")
        } else {
            terminal.println(table {

                align = TextAlign.CENTER

                header {
                    style(blue, bold = true)
                    row(
                        "INDICE",
                        "ID",
                        "FECHA ENTRADA",
                        "FECHA SALIDA",
                        "FECHA FINAL",
                        "PRECIO",
                        "TOPE ENTREGA",
                        "ESTADO"

                    )
                }
                for (pedido in lista) {
                    body {
                        rowStyles(cyan, brightCyan)
                        row(
                            indice,
                            pedido.id,
                            pedido.entryDate,
                            pedido.exitDate,
                            pedido.finalDate,
                            pedido.totalPrice,
                            pedido.maxDate,
                            pedido.state
                        )
                    }
                    indice++
                }
            })

        }
    }

    /**
     * Bucle del login para el logeo del trabajador en su turno.
     */
    private suspend fun loginBucle() {
        var email: String

        terminal.println(brightBlue("------ Log In Trabajador ------"))
        do {
            terminal.print("Correo: ")
            email = readln()
        } while (!email.matches(Regex("[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}")))

        terminal.print("Contraseña: ")
        val password: String = readln()

        when (val result =
            employeeController.getEmployeeByEmailAndPassword(email, PasswordParser.encriptar(password))) {
            is EmployeeError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is EmployeeSuccess -> {
                val correcto = result.data
                loggedEmployee = correcto
                if (correcto.isAdmin && correcto.available) {
                    administradorBucle()
                } else if (!correcto.isAdmin && correcto.available) {
                    encordadorBucle()
                }
            }
        }

    }


    /**
     * Bucle vista si el usuario es un encordador.
     */
    private suspend fun encordadorBucle() {
        loggedEmployee?.entryTime = LocalDateTime.now()
        administradorBuclePedidos()
        loggedEmployee?.departureTime = LocalDateTime.now()

    }

    /**
     * Bucle vista si el usuario es un administrador.
     */
    private suspend fun administradorBucle() {
        var opcion: Int
        do {
            terminal.println(brightBlue("------ Admin ------"))
            do {
                terminal.println(
                    "1- Trabajadores \n" +
                            "2- Clientes \n" +
                            "3- Maquinas \n" +
                            "4- Pedidos \n" +
                            "5- Productos \n" +
                            "0- Salir"
                )
                opcion = readln().toIntOrNull() ?: -1
            } while (opcion !in 0..5)
            opcionesBucleAdmin(opcion)
        } while (opcion != 0)

    }


    /**
     * Opciones del bucle del administrador
     */
    private suspend fun opcionesBucleAdmin(opcion: Int) {
        when (opcion) {
            1 -> administradorBucleUsuarios()
            2 -> administradorBucleClientes()
            3 -> administradorBucleMaquinas()
            4 -> administradorBuclePedidos()
            5 -> administradorBucleProductos()
            0 -> terminal.println(blue.bg("Saliendo de la sesión"))
        }
    }

//------------------------------------------- PRODUCTOS ------------------------------------------------------------

    /**
     * Bucle de administrador config productos.
     */
    private suspend fun administradorBucleProductos() {
        var opcion: Int
        do {
            terminal.println(brightBlue("------ Productos Admin ------"))
            do {
                terminal.println(
                    "1- Añadir Producto \n" +
                            "2- Actualizar Producto \n" +
                            "3- Listar Producto \n" +
                            "4- Eliminar Producto \n" +
                            "0- Salir"
                )
                opcion = readln().toIntOrNull() ?: -1
            } while (opcion < 0 || opcion > 4)
            opcionesBucleAdminProductos(opcion)
        } while (opcion != 0)
    }


    /**
     * Opciones del bucle del administrador con los productos.
     */
    private suspend fun opcionesBucleAdminProductos(opcion: Int) {
        when (opcion) {
            1 -> addProducto()
            2 -> actuProducto()
            3 -> getProductos()
            4 -> eliminarProducto()
            0 -> terminal.println(brightBlue.bg("Saliendo de la configuración de productos🛒🛒"))
        }
    }


    /**
     * Eliminar un producto.
     */
    private suspend fun eliminarProducto() {
        print("Dime el ID del producto a eliminar: ")
        val id = readln()

        when (val result = productController.getProductById(id)) {
            is ProductError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is ProductSuccess -> productController.deleteProduct(result.data)
        }
    }


    /**
     * Actualizar producto
     */
    private suspend fun actuProducto() {
        print("Dime el ID del producto a actualizar: ")
        val id = readln()

        when (val result = productController.getProductById(id)) {
            is ProductError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is ProductSuccess -> {
                val producto = creacionProductos()
                producto.id = result.data.id
                producto.uuid = result.data.uuid
                productController.updateProduct(producto)
            }
        }
    }


    /**
     * Conseguir todos los productos
     */
    private suspend fun getProductos() {
        when (val result = productController.getAllProducts()) {
            is ProductError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is ProductSuccess -> {
                val lista = result.data.toList()
                mostrarTablaProductos(lista)
            }
        }
    }

    private suspend fun getCordajes() {
        when (val result = productController.getAllProducts()) {
            is ProductError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is ProductSuccess -> {
                val lista = result.data.toList().filter { it.type == TypeProduct.CORDAJE }
                mostrarTablaProductos(lista)
            }
        }
    }


    private fun mostrarTablaProductos(lista: List<Product>) {
        var index = 0
        if (lista.isEmpty()) {
            println("Lista vacía")
        } else {
            terminal.println(table {

                align = TextAlign.CENTER
                header {
                    style(blue, bold = true)
                    row("INDEX", "ID", "TIPO", "MARCA", "MODELO", "PRECIO", "STOCK")
                }
                for (prod in lista) {
                    body {
                        rowStyles(cyan, brightCyan)

                        row(
                            index, prod.id, prod.type, prod.brand, prod.model, prod.price, prod.stock
                        )
                    }
                    index++
                }
            })
        }
    }


    /**
     * Crear el producto
     */
    private suspend fun addProducto() {
        val producto = creacionProductos()
        when (val result = productController.addProduct(producto)) {
            is ProductError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is ProductSuccess -> terminal.println("✅${result.code}: ${result.data}")
        }

    }


    /**
     * Para crear productos.
     */
    fun creacionProductos(): Product {
        val listaTipos = TypeProduct.values().map { it.toString() }.toList()

        var tipo: String
        do {
            println("Dime el tipo de producto: RAQUETA, CORDAJE, COMPLEMENTO")
            tipo = readln()
        } while (!listaTipos.contains(tipo))

        println("Dime el marca de producto: ")
        val marca: String = readln()
        println("Dime el modelo del producto: ")
        val modelo: String = readln()

        var precio: Float
        do {
            println("Dime el precio del producto: ")
            precio = readln().toFloatOrNull() ?: -1.0f
        } while (precio < 0.0f)

        var stock: Int
        do {
            println("Dime el stock del producto: ")
            stock = readln().toIntOrNull() ?: -1
        } while (stock <= 0)

        return Product(type = TypeProduct.valueOf(tipo), brand = marca, model = modelo, price = precio, stock = stock)
    }


//-------------------------------------------- CLIENTES ------------------------------------------------------------


    /**
     * Bucle de administrador config clientes.
     */
    private suspend fun administradorBucleClientes() {
        var opcion: Int
        do {
            terminal.println(brightBlue("------ Clientes Admin ------"))
            do {
                terminal.println(
                    "1- Añadir Cliente \n" +
                            "2- Actualizar Cliente \n" +
                            "3- Listar Cliente \n" +
                            "4- Eliminar Cliente \n" +
                            "0- Salir"
                )
                opcion = readln().toIntOrNull() ?: -1
            } while (opcion < 0 || opcion > 4)
            opcionesBucleAdminClientes(opcion)
        } while (opcion != 0)
    }


    /**
     * Opciones del bucle del administrador con los clientes.
     */
    private suspend fun opcionesBucleAdminClientes(opcion: Int) {
        when (opcion) {
            1 -> addCliente()
            2 -> actuCliente()
            3 -> getClientes()
            4 -> eliminarCliente()
            0 -> terminal.println(blue.bg("Saliendo de la configuración de clientes👩🏻👨🏻"))

        }
    }


    /**
     * Eliminar un cliente.
     */
    private suspend fun eliminarCliente() {
        print("Dime el ID del cliente a eliminar: ")
        val id = readln()

        when (val encontrado = customerController.getCustomerById(id)) {
            is CustomerError -> terminal.println(red("❌${encontrado.code}: ${encontrado.message}"))
            is CustomerSuccess -> customerController.deleteCustomer(encontrado.data)
        }
    }


    /**
     * Actualizar cliente
     */
    private suspend fun actuCliente() {
        print("Dime el ID del cliente a actualizar: ")
        val id = readln()

        when (val encontrado = customerController.getCustomerById(id)) {
            is CustomerError -> terminal.println(red("❌${encontrado.code}: ${encontrado.message}"))
            is CustomerSuccess -> {
                val usuario = creacionClientes()
                usuario.id = encontrado.data.id
                usuario.uuid = encontrado.data.uuid
                customerController.updateCustomer(usuario)
            }
        }
    }


    /**
     * Conseguir todos los clientes
     */
    private suspend fun getClientes() {
        when (val result = customerController.getAllCustomers()) {
            is CustomerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is CustomerSuccess -> {
                val lista = result.data.toList()
                if (lista.isEmpty()) {
                    println("Lista vacía")
                } else {

                    terminal.println(table {

                        align = TextAlign.CENTER
                        header {
                            style(blue, bold = true)
                            row("ID", "NOMBRE", "NOMBRE USUARIO", "EMAIL")
                        }
                        for (cli in lista) {
                            body {
                                rowStyles(cyan, brightCyan)
                                row(
                                    cli.id, cli.name, cli.username, cli.email
                                )
                            }
                        }
                    })
                }
            }
        }
    }


    /**
     * Crear el cliente
     */
    private suspend fun addCliente() {
        val usuario = creacionClientes()
        when (val result = customerController.addCustomer(usuario)) {
            is CustomerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is CustomerSuccess -> terminal.println("✅${result.code}: ${result.data}")
        }

    }


    /**
     * Para crear clientes.
     */
    private fun creacionClientes(): Customer {
        var nombre: String
        do {
            print("Nombre usuario: ")
            nombre = readln()
        } while (nombre.isEmpty())

        var apodo: String
        do {
            print("Apodo usuario: ")
            apodo = readln()
        } while (apodo.isEmpty())

        var email: String
        do {
            print("Correo usuario: ")
            email = readln()
        } while (!email.matches(Regex("[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}")))

        var password: String
        do {
            print("Contraseña usuario: ")
            password = readln()
        } while (password.isEmpty())


        return Customer(
            name = nombre, username = apodo, email = email, password = PasswordParser.encriptar(password),
            available = true, orderList = emptyList(), tennisRacketsList = emptyList()
        )
    }


//------------------------------------------- MAQUINAS -------------------------------------------------------------

    /**
     * Bucle para elegir que máquinas queremos ver.
     */
    private suspend fun administradorBucleMaquinas() {
        var opcion: Int
        do {
            terminal.println(brightBlue("------ Máquinas Admin ------"))
            do {
                terminal.println(
                    "1- Encordadoras \n" +
                            "2- Personalizadoras \n" +
                            "0- Salir"
                )
                opcion = readln().toIntOrNull() ?: -1
            } while (opcion < 0 || opcion > 4)
            opcionesBucleAdminMaquinas(opcion)
        } while (opcion != 0)
    }


    /**
     * Opciones del bucle de administrador.
     */
    private suspend fun opcionesBucleAdminMaquinas(opcion: Int) {
        when (opcion) {
            1 -> bucleEncordadorasAdmin()
            2 -> buclePersonalizadorasAdmin()
            0 -> terminal.println(blue.bg("Saliendo de la configuración de máquinas"))
        }

    }


//-------------------------------------- PERSONALIZADORAS ----------------------------------------------------------


    /**
     * Bucle personalizadoras.
     */
    private suspend fun buclePersonalizadorasAdmin() {
        var opcion: Int
        do {
            terminal.println(brightBlue("------ Personalizadoras Admin ------"))
            do {
                terminal.println(
                    "1- Añadir Personalizadora \n" +
                            "2- Actualizar Personalizadora \n" +
                            "3- Listar Personalizadoras \n" +
                            "4- Eliminar Personalizadora \n" +
                            "5 - Asignar personalizadora \n" +
                            "0- Salir"
                )
                opcion = readln().toIntOrNull() ?: -1
            } while (opcion < 0 || opcion > 5)
            opcionesBucleAdminPersonalizadoras(opcion)
        } while (opcion != 0)
    }

    private suspend fun opcionesBucleAdminPersonalizadoras(opcion: Int) {
        when (opcion) {
            1 -> addPersonalizadora()
            2 -> actuPersonalizadora()
            3 -> getPersonalizadoras()
            4 -> eliminarPersonalizadora()
            5 -> asignarPersonalizadora()
            0 -> terminal.println(blue.bg("Saliendo de la configuración de personalizadoras🎾🎾"))
        }
    }

    private suspend fun asignarPersonalizadora() {
        val result = machineController.getAllCustomizers()
        when (result) {
            is CustomizerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is CustomizerSuccess -> {
                if (loggedEmployee?.machine == null) {
                    terminal.println("Selecciona una personalizadora para utilizar")
                    var opt : Int
                    do {
                        getPersonalizadoras()
                        opt = readln().toIntOrNull() ?: 0
                    } while (opt < 0 || opt >= result.data.toList().size)

                    loggedEmployee?.machine = result.data.toList()[opt].id
                    terminal.println("Maquina asignada")
                    terminal.println(blue(loggedEmployee?.name!!) + "->" + green(loggedEmployee?.machine!!))
                }
            }
        }
    }


    /**
     * Eliminar una personalizadora
     */
    private suspend fun eliminarPersonalizadora() {
        print("Dime el ID de la personalizadora a eliminar: ")
        val leer = readln()

        when (val result = machineController.getCustomizerById(leer)) {
            is CustomizerError -> println(red("❌${result.code}: ${result.message}"))
            is CustomizerSuccess -> machineController.deleteCustomizer(result.data)
        }
    }


    /**
     * Ver todas las personalizadoras.
     */
    private suspend fun getPersonalizadoras() {
        when (val result = machineController.getAllCustomizers()) {
            is CustomizerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is CustomizerSuccess -> {
                val lista = result.data.toList()
                if (lista.isEmpty()) {
                    println("Lista vacía")
                } else {
                    terminal.println(table {
                        align = TextAlign.CENTER
                        header {
                            style(blue, bold = true)
                            row(
                                "INDICE",
                                "ID",
                                "MODELO",
                                "MARCA",
                                "FECHA ADQUISICION",
                                "MANIOBRABILIDAD",
                                "BALANCE",
                                "RIGIDEZ"
                            )
                        }
                        var indice = 0
                        for (pers in lista) {

                            body {
                                rowStyles(cyan, brightCyan)
                                row(
                                    indice,
                                    pers.id,
                                    pers.model,
                                    pers.brand,
                                    pers.acquisitionDate,
                                    pers.maneuverability,
                                    pers.balance,
                                    pers.rigidity
                                )
                            }
                            indice++
                        }
                    })
                }
            }
        }
    }


    /**
     * Actualizar una personalizadora.
     */
    private suspend fun actuPersonalizadora() {
        print("Dime el UUID de la personalizadora a actualizar: ")
        val linea = readln()

        when (val result = machineController.getCustomizerById(linea)) {
            is CustomizerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is CustomizerSuccess -> {
                val customizer = creacionPersonalizadora()
                customizer.id = result.data.id
                customizer.uuid = result.data.uuid
                machineController.updateCustomizer(customizer)
            }
        }
    }

    /**
     * Añadir una personalizadora.
     */
    private suspend fun addPersonalizadora() {
        val personalizadora = creacionPersonalizadora()
        when (val result = machineController.addCustomizer(personalizadora)) {
            is CustomizerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is CustomizerSuccess -> terminal.println("✅${result.code}: ${result.data}")
        }
    }


    /**
     * Creacion de una personalizadora preguntando al usuario.
     */
    private fun creacionPersonalizadora(): Customizer {
        var marca: String
        do {
            print("Marca personalizadora: ")
            marca = readln()
        } while (marca.isEmpty())
        var modelo: String
        do {
            print("Modelo personalizadora: ")
            modelo = readln()
        } while (modelo.isEmpty())
        var fecha: String
        do {
            print("Fecha adquisición personalizadora dd-MM-yyyy: ")
            fecha = readln()
        } while (!fecha.matches(Regex("^([0-2][0-9]|3[0-1])(-)(0[1-9]|1[0-2])\\2(\\d{4})\$")))
        var maniobrabilidad: String
        do {
            print("Maniobrabilidad personalizadora SI/NO: ")
            maniobrabilidad = readln()
        } while (maniobrabilidad != "SI" && maniobrabilidad != "NO")
        var balance: String
        do {
            print("Balance personalizadora SI/NO: ")
            balance = readln()
        } while (balance != "SI" && balance != "NO")
        var rigidez: String
        do {
            print("Rigidez personalizadora SI/NO: ")
            rigidez = readln()
        } while (rigidez != "SI" && rigidez != "NO")


        val campos = fecha.split("-")
        return Customizer(
            brand = marca,
            model = modelo,
            acquisitionDate = LocalDate.of(campos[2].toInt(), campos[1].toInt(), campos[0].toInt()),
            available = true,
            maneuverability = (maniobrabilidad == "SI"),
            balance = (balance == "SI"),
            rigidity = (rigidez == "SI")
        )
    }


//--------------------------------------------- ENCORDADORAS -------------------------------------------------------


    /**
     * Bucle encordadoras.
     */
    private suspend fun bucleEncordadorasAdmin() {
        var opcion: Int
        do {
            terminal.println(brightBlue("------ Encordadoras Admin ------"))
            do {
                terminal.println(
                    "1- Añadir Encordadora \n" +
                            "2- Actualizar Encordadora \n" +
                            "3- Listar Encordadoras \n" +
                            "4- Eliminar Encordadora \n" +
                            "5 - Asignar encordadora \n" +
                            "0- Salir"
                )
                opcion = readln().toIntOrNull() ?: -1
            } while (opcion < 0 || opcion > 5)
            opcionesBucleAdminEncordadoras(opcion)
        } while (opcion != 0)
    }

    private suspend fun opcionesBucleAdminEncordadoras(opcion: Int) {
        when (opcion) {
            1 -> addEncordadora()
            2 -> actuEncordadora()
            3 -> getEncordadoras()
            4 -> eliminarEncordadora()
            5 -> asignarEncordadora()
            0 -> terminal.println(blue.bg("Saliendo de la configuración de encordadoras🎾🎾"))
        }
    }

    private suspend fun asignarEncordadora() {
        val result = machineController.getAllStringers()
        when (result) {
            is StringerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is StringerSuccess -> {
                if (loggedEmployee?.machine == null) {
                    terminal.println("Selecciona una encordadora para utilizar")
                    var opt : Int
                    do {
                        getEncordadoras()
                        opt = readln().toIntOrNull() ?: 0
                    } while (opt < 0 || opt >= result.data.toList().size)

                    loggedEmployee?.machine = result.data.toList()[opt].id
                    terminal.println("Maquina asignada")
                    terminal.println(blue(loggedEmployee?.name!!) + "->" + green(loggedEmployee?.machine!!))
                } else {
                    terminal.println(red("Ya tienes una maquian asignada"))
                }
            }
        }
    }

    /**
     * Eliminar una encordadora
     */
    private suspend fun eliminarEncordadora() {
        print("Dime el ID de la encordadora a eliminar: ")
        val leer = readln()

        when (val result = machineController.getStringerById(leer)) {
            is StringerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is StringerSuccess -> machineController.deleteStringer(result.data)
        }
    }


    /**
     * Ver todas las encordadoras.
     */
    private suspend fun getEncordadoras() {
        when (val result = machineController.getAllStringers()) {
            is StringerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is StringerSuccess -> {
                val lista = result.data.toList()
                if (lista.isEmpty()) {
                    println("Lista vacía")
                } else {
                    terminal.println(table {
                        align = TextAlign.CENTER

                        header {
                            style(blue, bold = true)
                            row(
                                "INDICE",
                                "ID",
                                "MODELO",
                                "MARCA",
                                "FECHA ADQUISICION",
                                "AUTOMATICA",
                                "TENSION MAXIMA",
                                "TENSION MINIMA"
                            )
                        }
                        var indice = 0
                        for (trab in lista) {
                            body {
                                rowStyles(cyan, brightCyan)

                                row(
                                    indice,
                                    trab.id,
                                    trab.model,
                                    trab.brand,
                                    trab.acquisitionDate,
                                    if (trab.automatic == TypeMachine.AUTOMATICA) "✅" else "❌",
                                    trab.maximumTension,
                                    trab.minimumTension,
                                )
                            }

                            indice++
                        }
                    })
                }
            }
        }
    }


    /**
     * Actualizar una encordadora.
     */
    private suspend fun actuEncordadora() {
        print("Dime el ID de la encordadora a actualizar: ")
        val id = readln()

        when (val result = machineController.getStringerById(id)) {
            is StringerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is StringerSuccess -> {
                val encordadora = creacionEncordadora()
                encordadora.id = result.data.id
                encordadora.uuid = result.data.uuid
                machineController.updateStringer(encordadora)
            }
        }

    }


    /**
     * Añadir una encordadora.
     */
    private suspend fun addEncordadora() {
        val encordadora = creacionEncordadora()
        when (val result = machineController.addStringer(encordadora)) {
            is StringerError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is StringerSuccess -> terminal.println("✅${result.code}: ${result.data}")
        }

    }


    /**
     * Creacion de una encordadora preguntando al usuario.
     */
    private fun creacionEncordadora(): Stringer {
        var marca: String
        do {
            print("Marca encordadora: ")
            marca = readln()
        } while (marca.isEmpty())
        var modelo: String
        do {
            print("Modelo encordadora: ")
            modelo = readln()
        } while (modelo.isEmpty())
        var fecha: String
        do {
            print("Fecha adquisición encordadora dd-MM-yyyy: ")
            fecha = readln()
        } while (!fecha.matches(Regex("^([0-2][0-9]|3[0-1])(-)(0[1-9]|1[0-2])\\2(\\d{4})\$")))
        var tipo: String
        do {
            print("Tipo de automaticidad AUTOMATICA/MANUAL: ")
            tipo = readln()
        } while (tipo != "AUTOMATICA" && tipo != "MANUAL")
        var tensionMin: Int
        do {
            print("Tensión mínima encordadora: ")
            tensionMin = readln().toIntOrNull() ?: 0
        } while (tensionMin <= 0)
        var tensionMax: Int
        do {
            print("Tensión máxima encordadora: ")
            tensionMax = readln().toIntOrNull() ?: 0
        } while (tensionMax <= 0)

        val campos = fecha.split("-")
        return Stringer(
            brand = marca,
            model = modelo,
            acquisitionDate = LocalDate.of(campos[2].toInt(), campos[1].toInt(), campos[0].toInt()),
            available = true,
            automatic = TypeMachine.valueOf(tipo),
            maximumTension = tensionMax,
            minimumTension = tensionMin
        )
    }


//----------------------------------------- TRABAJADORES -----------------------------------------------------------


    /**
     * Bucle vista si el usuario es un administrador y selecciona los trabajadores.
     */
    private suspend fun administradorBucleUsuarios() {
        var opcion: Int
        do {
            terminal.println(brightBlue("------ Trabajadores Admin ------"))
            do {
                terminal.println(
                    "1- Añadir Trabajador \n" +
                            "2- Actualizar Trabajador \n" +
                            "3- Listar Trabajadores \n" +
                            "4- Eliminar trabajador \n" +
                            "0- Salir"
                )
                opcion = readln().toIntOrNull() ?: -1
            } while (opcion < 0 || opcion > 4)
            opcionesBucleAdminUsuarios(opcion)
        } while (opcion != 0)
    }


    /**
     * Opciones bucle de conf de trabajadores del administrador.
     */
    private suspend fun opcionesBucleAdminUsuarios(opcion: Int) {
        when (opcion) {
            1 -> addTrabajador()
            2 -> actuTrabajador()
            3 -> getTrabajadores()
            4 -> eliminarTrabajador()
            0 -> terminal.println(blue.bg("Saliendo de la configuración de trabajadores👩🏻👨🏻"))
        }
    }


    /**
     * Eliminar un trabajador.
     */
    private suspend fun eliminarTrabajador() {
        print("Dime el ID del trabajador a eliminar: ")
        val id = readln()
        when (val encontrado = employeeController.getEmployeeById(id)) {
            is EmployeeError -> terminal.println(red("❌${encontrado.code}: ${encontrado.message}"))
            is EmployeeSuccess -> {
                employeeController.deleteEmployee(encontrado.data)
            }
        }
    }


    /**
     * Actualizar trabajador
     */
    private suspend fun actuTrabajador() {
        print("Dime el ID del trabajador a actualizar: ")
        val id = readln()

        when (val encontrado = employeeController.getEmployeeById(id)) {
            is EmployeeError -> terminal.println(red("❌${encontrado.code}: ${encontrado.message}"))
            is EmployeeSuccess -> {
                val usuario = creacionTrabajadores()
                usuario.id = encontrado.data.id
                usuario.uuid = encontrado.data.uuid
                employeeController.updateEmployee(usuario)
            }
        }

    }


    /**
     * Conseguir todos los trabajadores
     */
    private suspend fun getTrabajadores() {
        when (val result = employeeController.getAllEmployees()) {
            is EmployeeError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is EmployeeSuccess -> {
                val lista = result.data.toList()
                if (lista.isEmpty()) {
                    println("Lista vacía")
                } else {
                    terminal.println(table {
                        align = TextAlign.CENTER
                        header {
                            style(blue, bold = true)
                            row("ID", "NOMBRE", "APELLIDO", "EMAIL", "ADMINISTRADOR")
                        }
                        for (trab in lista) {
                            body {
                                rowStyles(cyan, brightCyan)

                                row(
                                    trab.id, trab.name, trab.surname, trab.email,
                                    if (trab.isAdmin) "✅" else "❌"
                                )
                            }
                        }
                    })
                }
            }
        }
    }


    /**
     * Crear el trabajador
     */
    private suspend fun addTrabajador() {
        val usuario = creacionTrabajadores()
        when (val save = employeeController.addEmployee(usuario)) {
            is EmployeeError -> terminal.println(red("❌${save.code}: ${save.message}"))
            is EmployeeSuccess -> terminal.println("✅${save.code}: ${save.data}")
        }
    }


    /**
     * Para crear trabajadores.
     */
    fun creacionTrabajadores(): Employee {
        var nombre: String
        do {
            print("Nombre usuario: ")
            nombre = readln()
        } while (nombre.isEmpty())

        var apellido: String
        do {
            print("Apodo usuario: ")
            apellido = readln()
        } while (apellido.isEmpty())

        var email: String
        do {
            print("Correo usuario: ")
            email = readln()
        } while (!email.matches(Regex("[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}")))

        var password: String
        do {
            print("Contraseña usuario: ")
            password = readln()
        } while (password.isEmpty())

        var respuesta: String
        val admin: Boolean
        do {
            print("Administrador (S/N)")
            respuesta = readln()
        } while (respuesta != "S" && respuesta != "N")
        admin = respuesta == "S"


        return Employee(
            name = nombre,
            surname = apellido,
            email = email,
            password = PasswordParser.encriptar(password),
            available = true,
            isAdmin = admin
        )
    }


//------------------------------------------ RAQUETAS ------------------------------------------

    /**
     * Actualizar raqueta
     */
    private suspend fun actuRaqueta() {
        print("Dime el ID de la raqueta a actualizar: ")
        val id = readln()

        when (val result = racketController.findById(id)) {
            is RacketError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is RacketSuccess -> {
                val raqueta = creacionRaquetas()
                raqueta.id = result.data.id
                raqueta.uuid = result.data.uuid
                racketController.updateRacket(raqueta)
            }
        }
    }


    /**
     * Eliminar raqueta
     */
    private suspend fun deleteRaqueta() {
        print("Dime el ID de la raqueta a eliminar: ")
        val id = readln()
        when (val result = racketController.findById(id)) {
            is RacketError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is RacketSuccess -> {
                racketController.deleteRacket(result.data)
            }
        }
    }


    /**
     * Creamos y añadimos la raqueta al repo de raquetas y actualizamos a el cliente.
     */
    private suspend fun addRaqueta() {
        val raqueta = creacionRaquetas()
        when (val result = racketController.saveRacket(raqueta)) {
            is RacketError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is RacketSuccess -> {
                terminal.println("✅${result.code}: ${result.data}")
                val list = loggedCustomer?.tennisRacketsList?.toMutableList()
                list?.add(result.data.id)
                loggedCustomer?.tennisRacketsList = list!!
                customerController.updateCustomer(loggedCustomer!!)
            }
        }
    }


    /**
     * Crear raquetas.
     * @return raqueta creada
     */
    private fun creacionRaquetas(): Racket {
        var marca: String
        do {
            print("Marca raqueta: ")
            marca = readln()
        } while (marca.isEmpty())

        var modelo: String
        do {
            print("Modelo raqueta: ")
            modelo = readln()
        } while (modelo.isEmpty())

        var maniobrabilidad: Float
        do {
            print("Maniobrabilidad raqueta: ")
            maniobrabilidad = readln().toFloatOrNull() ?: -1.0f
        } while (maniobrabilidad <= 0.0f)

        var balance: Float
        do {
            print("Balance raqueta: ")
            balance = readln().toFloatOrNull() ?: -1.0f
        } while (balance <= 0.0f)

        var rigidez: Float
        do {
            print("Rigidez raqueta: ")
            rigidez = readln().toFloatOrNull() ?: -1.0f
        } while (rigidez <= 0.0f)

        return Racket(
            brand = marca,
            model = modelo,
            maneuverability = maniobrabilidad,
            balance = balance,
            rigidity = rigidez
        )
    }


    /**
     * Encontrar las raquetas del cliente.
     */
    private suspend fun getRackets() {
        val list = mutableListOf<Racket>()
        val listRacketsCustomer = loggedCustomer?.tennisRacketsList
        when (val result = racketController.getAllRackets()) {
            is RacketError -> terminal.println(red("❌${result.code}: ${result.message}"))
            is RacketSuccess -> {
                listRacketsCustomer?.forEach {
                    when (val find = racketController.findById(it)) {
                        is RacketError -> TODO()
                        is RacketSuccess -> list.add(find.data)
                    }
                }
            }
        }

        printRackets(list)
    }


    /**
     * Imprimir las raquetas
     */
    private fun printRackets(list: MutableList<Racket>) {
        if (list.isEmpty()) {
            println("Lista vacía")
        } else {
            terminal.println(table {
                align = TextAlign.CENTER
                header {
                    style(blue, bold = true)
                    row("ID", "MARCA", "MODELO", "MANIOBRABILIDAD", "BALANCE", "RIGIDEZ")
                }
                for (racket in list) {
                    body {
                        rowStyles(cyan, brightCyan)
                        row(
                            racket.id,
                            racket.brand,
                            racket.model,
                            racket.maneuverability,
                            racket.balance,
                            racket.rigidity
                        )
                    }
                }
            })
        }
    }


}



