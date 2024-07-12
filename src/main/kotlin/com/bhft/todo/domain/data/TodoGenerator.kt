package com.bhft.todo.domain.data

import com.bhft.todo.core.utils.RandomUtils.getRandomInt
import com.bhft.todo.domain.client.todoClientWithAuth
import com.bhft.todo.domain.controller.TodoController
import com.bhft.todo.domain.controller.dto.TodoItem
import io.ktor.http.*

object TodoGenerator {
    private val controller = TodoController(todoClientWithAuth)

    private val generatedTodos = mutableSetOf<TodoItem>()

    private fun generateTodo(): TodoItem =
        getRandomInt(0, 1000000).run {
            TodoItem(
                id = this.toLong(),
                text = "Generated TODO item, id: $this",
                completed = true
            )
        }

    fun createTodo(): TodoItem? =
        generateTodo().run {
            val createResponse = controller.createTodo(this)

            this
                .takeIf { createResponse.status == HttpStatusCode.Created }
                ?.let {
                    generatedTodos.add(it)
                    it
                }
        }

    fun createTodos(count: Int): MutableList<TodoItem> {
        val createdItems = mutableListOf<TodoItem>()

        repeat(count) {
            createTodo().also { it?.let { createdItems.add(it) } }
        }

        return createdItems
    }

    fun deleteAllGeneratedTodos() {
        generatedTodos.forEach { controller.deleteTodo(it.id) }
    }
}
