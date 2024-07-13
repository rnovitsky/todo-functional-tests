package com.bhft.todo

import com.bhft.todo.domain.controller.dto.TodoItem
import io.ktor.http.*
import io.qameta.allure.Description
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("DELETE /todos")
class DeleteTodoTest : BaseTest() {
    private lateinit var todoForDelete: TodoItem

    @BeforeEach
    fun generateTodoForDelete() {
        todoForDelete =
            step("Generate new item") { todoGenerator.createTodo() }
    }

    @Test
    @DisplayName("Should not delete item if not authorized")
    @Description("DELETE should forbid deleting an item if authorization failed")
    fun shouldNotDeleteTodoIfNotAuthorized() {
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
        val deleteTodoResponse =
            step("Delete created item with authorization") { todoControllerWithAuth.deleteTodo(todoForDelete.id) }

        step("Check that item is deleted") {
            val todoList = todoController.getTodoList().body

            assertThat(deleteTodoResponse.status).isEqualTo(HttpStatusCode.NoContent)
            assertThat(todoList).doesNotContain(todoForDelete)
        }
    }

    @Test
    @DisplayName("Should get error for non-existing")
    @Description("DELETE should should return error on attempt to delete non-existing item")
    fun shouldGetErrorOnDeleteNonExistingItem() {
        val fakeId = 123L

        val deleteTodoResponse =
            step("Delete non-existing item with authorization") { todoControllerWithAuth.deleteTodo(fakeId) }

        step("Check that error is returned") {
            assertThat(deleteTodoResponse.status).isEqualTo(HttpStatusCode.NotFound)
        }
    }
}
