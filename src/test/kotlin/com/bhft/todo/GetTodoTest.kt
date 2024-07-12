package com.bhft.todo

import com.bhft.todo.domain.controller.dto.TodoItem
import com.bhft.todo.domain.data.TodoGenerator
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@DisplayName("GET /todos")
class GetTodoTest : BaseTest() {
    @Test
    @DisplayName("Should return full list of items")
    fun shouldGetTodoList() {
        val generatedTodos = TodoGenerator.createTodos(5)

        val todoListResponse = todoController.getTodoList()
        val todoList = runBlocking { todoListResponse.body<List<TodoItem>>() }

        assertEquals(HttpStatusCode.OK, todoListResponse.status)
        assertContentEquals(generatedTodos, todoList)
    }
}
