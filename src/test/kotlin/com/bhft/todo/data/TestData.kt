package com.bhft.todo.data

import com.bhft.todo.domain.controller.dto.TodoItemInvalidTypes
import com.bhft.todo.domain.data.TodoGenerator

fun todoItem() = TodoGenerator.generateTodo()

val todoItemInvalidTypes = TodoItemInvalidTypes(id = 1234567, text = "TODO with invalid types", completed = true)
val todoItemNegativeId = todoItemInvalidTypes.copy(id = -1)
val todoItemMissingId = todoItemInvalidTypes.copy(id = null)
val todoItemMissingText = todoItemInvalidTypes.copy(text = null)
val todoItemMissingCompleted = todoItemInvalidTypes.copy(completed = null)
