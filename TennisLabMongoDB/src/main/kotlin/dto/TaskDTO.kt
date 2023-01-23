package dto

data class TaskDTO(
    val completed: Boolean,
    val id: Int,
    val title: String,
    val userId: Int
)