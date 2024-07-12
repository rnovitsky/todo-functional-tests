package com.bhft.todo

import com.bhft.todo.domain.data.TodoGenerator
import io.ktor.http.*
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

        assertEquals(HttpStatusCode.OK, todoListResponse.status)
        assertContentEquals(generatedTodos, todoListResponse.body)
    }
}
