package com.example.tennislabspring.exception

sealed interface TaskResult<Task>

class TaskSuccess<T : Any>(val code: Int, val data: T) : TaskResult<T>

abstract class TaskError<Nothing>(val code: Int, open val message: String?) : TaskResult<Nothing>
class TaskErrorNotFound<Nothing>(message: String?) : TaskError<Nothing>(404, message)
class TaskErrorBadRequest<Nothing>(message: String?) : TaskError<Nothing>(400, message)
class TaskInternalException<Nothing>(message: String?) : TaskError<Nothing>(500, message)
class TaskErrorExists<Nothing>(message: String?) : TaskError<Nothing>(403, message)


