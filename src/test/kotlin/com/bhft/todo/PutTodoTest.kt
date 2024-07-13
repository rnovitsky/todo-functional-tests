package com.bhft.todo

import com.bhft.todo.data.*
import com.bhft.todo.domain.controller.dto.TodoItem
import com.bhft.todo.domain.data.TodoGenerator
import io.ktor.http.*
import jdk.jfr.Description
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("PUT /todos")
class PutTodoTest : BaseTest() {
    private lateinit var existingTodo: TodoItem

    @BeforeEach
    fun createTodoItem() {
        existingTodo =
            step("Create new item") { TodoGenerator.createTodo() }
    }

    @Test
    @DisplayName("Should update existing todo item")
    @Description("PUT should allow to update existing item")
    fun shouldUpdateExistingTodo() {
        val todoForUpdate = todoItem()

        val updateTodoResponse =
            step("Update existing item") { todoController.updateTodo(existingTodo.id, todoForUpdate) }

        step("Check that item is updated") {
            val todoList = todoController.getTodoList().body

            assertThat(updateTodoResponse.status).isEqualTo(HttpStatusCode.OK)
            assertThat(todoList).doesNotContain(existingTodo)
            assertThat(todoList).contains(todoForUpdate)
        }
    }

//    [BUG] update with existing id allows to create duplicates. Test is disabled for now
    @Disabled
    @Test
    @DisplayName("Should not update existing todo item to existing id")
    @Description("PUT should not update existing item if id from the payload already exists in TODO list")
    fun shouldNotUpdateExistingTodoExistingId() {
        val secondTodo =
            step("Create second item") { TodoGenerator.createTodo() }

        val updateTodoResponse =
            step("Update second item with the first item") { todoController.updateTodo(secondTodo.id, existingTodo) }

        step("Check that item is not updated and error is returned") {
            val todoList = todoController.getTodoList().body

            assertThat(updateTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(todoList).hasSize(2)
            assertThat(todoList).contains(existingTodo)
            assertThat(todoList).contains(secondTodo)
        }
    }

    @Test
    @DisplayName("Should return error for non-existing id")
    @Description("PUT should return error if updating item does not exist")
    fun shouldReturnErrorForNonExistingId() {
        val fakeId = 123L
        val todoItem = todoItem()

        val updateTodoResponse =
            step("Update non-existing item") { todoController.updateTodo(fakeId, todoItem) }

        step("Check that error is returned and item is not updated") {
            val todoList = todoController.getTodoList().body

            assertThat(updateTodoResponse.status).isEqualTo(HttpStatusCode.NotFound)
            assertThat(todoList).doesNotContain(todoItem)
            assertThat(todoList).contains(existingTodo)
        }
    }

//    [BUG] for invalid payload PUT returns 401 instead of 400. Using an authorized client is a workaround
    @Test
    @DisplayName("Should not update todo with negative id")
    @Description("PUT should not update an item if negative id is passed")
    fun shouldReturnErrorOnNegativeId() {
        val updateTodoResponse =
            step("Update item with negative id") {
                todoControllerWithAuth.updateTodoInvalidTypes(existingTodo.id, todoItemNegativeId)
            }

        step("Check that item is not updated and error returned") {
            val todoList = todoController.getTodoList().body

            assertThat(updateTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(updateTodoResponse.error).contains(INVALID_VALUE_ERROR)
            assertThat(todoList).contains(existingTodo)
            assertThat(todoList).extracting("id").doesNotContain(todoItemNegativeId.id)
        }
    }

    @Test
    @DisplayName("Should not update todo without id")
    @Description("PUT should not update an item if id is missing in payload")
    fun shouldReturnErrorOnMissingId() {
        val updateTodoResponse =
            step("Update item without id in payload") {
                todoControllerWithAuth.updateTodoInvalidTypes(existingTodo.id, todoItemMissingId)
            }

        step("Check that item is not updated and error returned") {
            val todoList = todoController.getTodoList().body

            assertThat(updateTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(updateTodoResponse.error).contains(MISSING_FIELD_ERROR.format("id"))
            assertThat(todoList).contains(existingTodo)
            assertThat(todoList).extracting("text").doesNotContain(todoItemMissingId.text)
        }
    }

    @Test
    @DisplayName("Should not update todo without text")
    @Description("PUT should not update an item if text field is missing in payload")
    fun shouldReturnErrorOnMissingText() {
        val updateTodoResponse =
            step("Update item without text field in payload") {
                todoControllerWithAuth.updateTodoInvalidTypes(existingTodo.id, todoItemMissingText)
            }

        step("Check that item is not updated and error returned") {
            val todoList = todoController.getTodoList().body

            assertThat(updateTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(updateTodoResponse.error).contains(MISSING_FIELD_ERROR.format("text"))
            assertThat(todoList).contains(existingTodo)
            assertThat(todoList).extracting("id").doesNotContain(todoItemMissingId.id)
        }
    }

    @Test
    @DisplayName("Should not update todo without completed")
    @Description("PUT should not update an item if completed field is missing in payload")
    fun shouldReturnErrorOnMissingCompleted() {
        val updateTodoResponse =
            step("Update item without completed field in payload") {
                todoControllerWithAuth.updateTodoInvalidTypes(existingTodo.id, todoItemMissingCompleted)
            }

        step("Check that item is not updated and error returned") {
            val todoList = todoController.getTodoList().body

            assertThat(updateTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(updateTodoResponse.error).contains(MISSING_FIELD_ERROR.format("completed"))
            assertThat(todoList).contains(existingTodo)
            assertThat(todoList).extracting("id").doesNotContain(todoItemMissingId.id)
        }
    }
}
