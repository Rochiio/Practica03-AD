package com.example.tennislabspring.exception

/**
 * Errores personalizados para las máquinas encordadoras.
 */
sealed interface StringerResult<Stringer>

class StringerSuccess<T : Any>(val code: Int, val data: T) : StringerResult<T>

abstract class StringerError<Nothing>(val code: Int, open val message: String?) : StringerResult<Nothing>
class StringerErrorNotFound<Nothing>(message: String?) : StringerError<Nothing>(404, message)
class StringerErrorBadRequest<Nothing>(message: String?) : StringerError<Nothing>(400, message)
class StringerInternalException<Nothing>(message: String?) : StringerError<Nothing>(500, message)
class StringerErrorExists<Nothing>(message: String?) : StringerError<Nothing>(403, message)


