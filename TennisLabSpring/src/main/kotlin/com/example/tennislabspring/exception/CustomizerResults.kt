package com.example.tennislabspring.exception


/**
 * Errores personalizados para las máquinas personalizadoras.
 */
sealed interface CustomizerResult<Customizer>

class CustomizerSuccess<T : Any>(val code: Int, val data: T) : CustomizerResult<T>

abstract class CustomizerError<Nothing>(val code: Int, open val message: String?) : CustomizerResult<Nothing>
class CustomizerErrorNotFound<Nothing>(message: String?) : CustomizerError<Nothing>(404, message)
class CustomizerErrorBadRequest<Nothing>(message: String?) : CustomizerError<Nothing>(400, message)
class CustomizerInternalException<Nothing>(message: String?) : CustomizerError<Nothing>(500, message)
class CustomizerErrorExists<Nothing>(message: String?) : CustomizerError<Nothing>(403, message)

