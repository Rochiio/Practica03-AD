package exception

sealed interface OrderResult<Order>

class OrderSuccess<T : Any>(val code: Int, val data: T) : OrderResult<T>

abstract class OrderError<Nothing>(val code: Int, open val message: String?) : OrderResult<Nothing>
class OrderErrorNotFound<Nothing>(message: String?) : OrderError<Nothing>(404, message)
class OrderErrorBadRequest<Nothing>(message: String?) : OrderError<Nothing>(400, message)
class OrderInternalException<Nothing>(message: String?) : OrderError<Nothing>(500, message)
class OrderErrorExists<Nothing>(message: String?) : OrderError<Nothing>(403, message)


