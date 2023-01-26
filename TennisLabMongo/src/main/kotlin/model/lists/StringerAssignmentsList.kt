package model.lists

import kotlinx.serialization.Serializable

/**
 * Listado de asinaciones a encordadores por fecha
 */
@Serializable
data class StringerAssignmentsList(
    val list: List<StringerAssignments>
) {
}