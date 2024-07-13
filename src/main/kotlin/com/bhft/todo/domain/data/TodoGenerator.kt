package com.bhft.todo.domain.data

import com.bhft.todo.core.logger.WithLogger
import com.bhft.todo.domain.controller.TodoController
import com.bhft.todo.domain.controller.dto.TodoItem
import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*

/**
 * Helper class to manipulate test data.
 * Contains a global state of all generated objects which allows to clean up test data easily.
 *
 * Note: current implementation is not thread-safe, should be redesigned for using in parallel tests.
 */
class TodoGenerator(client: HttpClient) : WithLogger {
    private val controller =
        TodoController(
            client.config { Logging { level = LogLevel.NONE } }
        )

    private val availableIds = (0..Long.MAX_VALUE).iterator()
    private val generatedTodos = mutableSetOf<TodoItem>()

    fun generateTodo(): TodoItem =
        availableIds.nextLong().run {
            TodoItem(
                id = this,
                text = "Generated TODO item, id: $this",
                completed = true
            ).also {
                generatedTodos.add(it)
                logger.debug("Generated random TODO item with id $this")
            }
        }

    fun createTodo(): TodoItem {
        generateTodo().run {
            val createResponse = controller.createTodo(this)

            if (createResponse.status == HttpStatusCode.Created) {
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
        generatedTodos.clear()
    }
}
