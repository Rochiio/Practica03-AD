package exception

/**
 * Errores personalizados para las raquetas.
 */
sealed interface RacketResult<Racket>

class RacketSuccess<T : Any>(val code: Int, val data: T) : RacketResult<T>

abstract class RacketError<Nothing>(val code: Int, open val message: String?) : RacketResult<Nothing>
class RacketErrorNotFound<Nothing>(message: String?) : RacketError<Nothing>(404, message)
class RacketErrorBadRequest<Nothing>(message: String?) : RacketError<Nothing>(400, message)
class RacketInternalException<Nothing>(message: String?) : RacketError<Nothing>(500, message)
class RacketErrorExists<Nothing>(message: String?) : RacketError<Nothing>(403, message)


