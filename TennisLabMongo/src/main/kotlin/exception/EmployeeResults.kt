package exception

/**
 * Errores personalizados para los empleados.
 */
sealed interface EmployeeResult<Employee>

class EmployeeSuccess<T : Any>(val code: Int, val data: T) : EmployeeResult<T>

abstract class EmployeeError<Nothing>(val code: Int, open val message: String?) : EmployeeResult<Nothing>
class EmployeeErrorNotFound<Nothing>(message: String?) : EmployeeError<Nothing>(404, message)
class EmployeeErrorBadRequest<Nothing>(message: String?) : EmployeeError<Nothing>(400, message)
class EmployeeInternalException<Nothing>(message: String?) : EmployeeError<Nothing>(500, message)
class EmployeeErrorExists<Nothing>(message: String?) : EmployeeError<Nothing>(403, message)
