package model.lists

import kotlinx.serialization.Serializable

/**
 * Listado de asinaciones a encordadores por fecha
 */
@Serializable
data class StringersAssignmentsList(
    val list: List<StringersAssignments>
)
