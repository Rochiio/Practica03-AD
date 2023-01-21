package exception

import model.Product

sealed interface ProductResult<Product>

class ProductSuccess<T : Any>(val code: Int, val data: T) : ProductResult<T>

abstract class ProductError<Nothing>(val code: Int, open val message: String?) : ProductResult<Nothing>
class ProductErrorNotFound<Nothing>(message: String?) : ProductError<Nothing>(404, message)
class ProductErrorBadRequest<Nothing>(message: String?) : ProductError<Nothing>(400, message)
class ProductInternalException<Nothing>(message: String?) : ProductError<Nothing>(500, message)
class ProductErrorExists<Nothing>(message: String?) : ProductError<Nothing>(403, message)


