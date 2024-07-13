package com.bhft.todo

import com.bhft.todo.domain.data.TodoGenerator
import io.ktor.http.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GET /todos")
class GetTodoTest : BaseTest() {
    @Test
    @DisplayName("Should return full list of items")
    fun shouldGetTodoList() {
        val generatedTodos = TodoGenerator.createTodos(5)

        val todoListResponse = todoController.getTodoList()

        assertThat(todoListResponse.status).isEqualTo(HttpStatusCode.OK)
        assertThat(todoListResponse.body).isEqualTo(generatedTodos)
    }
}
