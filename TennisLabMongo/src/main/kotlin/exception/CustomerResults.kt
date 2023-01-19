package exception

/**
 * Errores personalizados para los clientes.
 */
sealed interface CustomerResult<Customer>

class CustomerSuccess<T : Any>(val code: Int, val data: T) : CustomerResult<T>

abstract class CustomerError<Nothing>(val code: Int, open val message: String?) : CustomerResult<Nothing>
class CustomerErrorNotFound<Nothing>(message: String?) : CustomerError<Nothing>(404, message)
class CustomerErrorBadRequest<Nothing>(message: String?) : CustomerError<Nothing>(400, message)
class CustomerInternalException<Nothing>(message: String?) : CustomerError<Nothing>(500, message)

