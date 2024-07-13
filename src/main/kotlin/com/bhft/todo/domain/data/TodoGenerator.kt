package com.bhft.todo.domain.data

import com.bhft.todo.core.logger.WithLogger
import com.bhft.todo.domain.client.todoClientWithAuth
import com.bhft.todo.domain.controller.TodoController
import com.bhft.todo.domain.controller.dto.TodoItem
import io.ktor.client.plugins.logging.*
import io.ktor.http.*

object TodoGenerator : WithLogger {
    private val controller =
        TodoController(
            todoClientWithAuth.config { Logging { level = LogLevel.NONE } }
        )

    private val availableIds = (0..1000000).iterator()
    private val generatedTodos = mutableSetOf<TodoItem>()

    private fun generateTodo(): TodoItem =
        availableIds.nextInt().run {
            TodoItem(
                id = this.toLong(),
                text = "Generated TODO item, id: $this",
                completed = true
            ).also { logger.debug("Generated random TODO item with id $this") }
        }

    fun createTodo(): TodoItem {
        generateTodo().run {
            val createResponse = controller.createTodo(this)

            if (createResponse.status == HttpStatusCode.Created) {
                generatedTodos.add(this)
                return this
            } else {
                logger.error("Cannot create TODO item with id = ${this.id}")
                throw RuntimeException("Error while creating new TODO item")
            }
        }
    }

    fun createTodos(count: Int): MutableList<TodoItem> {
        val createdItems = mutableListOf<TodoItem>()

        repeat(count) {
            createTodo().also { createdItems.add(it) }
        }

        return createdItems
    }

    fun deleteAllGeneratedTodos() {
        generatedTodos.forEach { controller.deleteTodo(it.id) }
    }
}
