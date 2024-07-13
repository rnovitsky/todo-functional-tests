package com.bhft.todo

import com.bhft.todo.domain.data.TodoGenerator
import io.ktor.http.*
import io.qameta.allure.Description
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GET /todos")
class GetTodoTest : BaseTest() {
    @Test
    @DisplayName("Should return full list of items")
    @Description("GET should return all items if offset and limit are not passed")
    fun shouldGetTodoList() {
        val generatedTodos =
            step("Generate 5 items") { TodoGenerator.createTodos(5) }

        val todoListResponse =
            step("Retrieve list of items") { todoController.getTodoList() }

        step("Check that all items are returned") {
            assertThat(todoListResponse.status).isEqualTo(HttpStatusCode.OK)
            assertThat(todoListResponse.body).isEqualTo(generatedTodos)
        }
    }
}
