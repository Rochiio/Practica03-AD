package com.example.tennislabspring.model.lists

import com.example.tennislabspring.model.lists.StringerAssignments
import kotlinx.serialization.Serializable

/**
 * Listado de asinaciones a encordadores por fecha
 */
@Serializable
data class StringerAssignmentsList(
    val list: List<StringerAssignments>
) {
}