package com.bhft.todo.domain.controller.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(
    val id: Long,
    val text: String,
    val completed: Boolean
)
