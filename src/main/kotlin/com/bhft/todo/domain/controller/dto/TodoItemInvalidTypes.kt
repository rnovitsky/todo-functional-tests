package com.bhft.todo.domain.controller.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoItemInvalidTypes(
    val id: Long? = null,
    val text: String? = null,
    val completed: Boolean? = null
)
