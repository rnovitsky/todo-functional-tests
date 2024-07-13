package com.bhft.todo

import com.bhft.todo.domain.data.TodoGenerator
import io.ktor.http.*
import io.qameta.allure.Description
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("DELETE /todos")
class DeleteTodoTest : BaseTest() {
    @Test
    @DisplayName("Should not delete item if not authorized")
    @Description("DELETE should forbid deleting an item if authorization failed")
    fun shouldNotDeleteTodoIfNotAuthorized() {
        val todoForDelete =
            step("Generate new item") { TodoGenerator.createTodo() }

        val deleteTodoResponse =
            step("Delete created item without authorization") { todoController.deleteTodo(todoForDelete.id) }

        step("Check that item is not deleted") {
            val todoList = todoController.getTodoList().body

            assertThat(deleteTodoResponse.status).isEqualTo(HttpStatusCode.Unauthorized)
            assertThat(todoList).contains(todoForDelete)
        }
    }

    @Test
    @DisplayName("Should delete item if authorized")
    @Description("DELETE should delete an item if authorized")
    fun shouldDeleteTodoIfAuthorized() {
        val todoForDelete =
            step("Generate new item") { TodoGenerator.createTodo() }

        val deleteTodoResponse =
            step("Delete created item with authorization") { todoControllerWithAuth.deleteTodo(todoForDelete.id) }

        step("Check that item is deleted") {
            val todoList = todoController.getTodoList().body

            assertThat(deleteTodoResponse.status).isEqualTo(HttpStatusCode.NoContent)
            assertThat(todoList).doesNotContain(todoForDelete)
        }
    }
}
