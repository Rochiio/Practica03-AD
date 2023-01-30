package exception

sealed interface OrderResult<Order>

class OrderSuccess<T : Any>(val code: Int, val data: T) : OrderResult<T>
/*
private suspend fun getClientes() {
    when (val result = customerController.getAllCustomers()) {
        is CustomerError -> terminal.println(TextColors.red("❌${result.code}: ${result.message}"))
        is CustomerSuccess -> {
            val lista = result.data.toList()
            if (lista.isEmpty()) {
                println("Lista vacía")
            } else {
*/

abstract class OrderError<Nothing>(val code: Int, open val message: String?) : OrderResult<Nothing>
class OrderErrorNotFound<Nothing>(message: String?) : OrderError<Nothing>(404, message)
class OrderErrorBadRequest<Nothing>(message: String?) : OrderError<Nothing>(400, message)
class OrderInternalException<Nothing>(message: String?) : OrderError<Nothing>(500, message)
class OrderErrorExists<Nothing>(message: String?) : OrderError<Nothing>(403, message)


