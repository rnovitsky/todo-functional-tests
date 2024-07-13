package com.bhft.todo

import com.bhft.todo.data.*
import io.ktor.http.*
import jdk.jfr.Description
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("POST /todos")
class PostTodoTest : BaseTest() {
    @Test
    @DisplayName("Should create new todo item")
    @Description("POST should allow to create new item")
    fun shouldCreateNewTodo() {
        val todoItem = todoItem()

        val createTodoResponse =
            step("Create new item") { todoController.createTodo(todoItem) }

        step("Check that new item is created") {
            val todoList = todoController.getTodoList().body

            assertThat(createTodoResponse.status).isEqualTo(HttpStatusCode.Created)
            assertThat(todoList).contains(todoItem)
        }
    }

    @Test
    @DisplayName("Should not create todo with same id")
    @Description("POST should not create a new item if item with the same id exists")
    fun shouldNotCreateTodoWithSameId() {
        val firstTodo =
            step("Create first item") { todoGenerator.createTodo() }

        val createSameTodoResponse =
            step("Create second item with same id") { todoController.createTodo(firstTodo) }

        step("Check that only one item is created") {
            val todoList = todoController.getTodoList().body

            assertThat(createSameTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(todoList).filteredOn { it.id == firstTodo.id }.hasSize(1)
        }
    }

    @Test
    @DisplayName("Should not create todo with negative id")
    @Description("POST should not create a new item if negative id is passed")
    fun shouldReturnErrorOnNegativeId() {
        val createTodoResponse =
            step("Create new item with negative id") { todoController.createTodoInvalidTypes(todoItemNegativeId) }

        step("Check that new item is not created and error returned") {
            val todoList = todoController.getTodoList().body

            assertThat(createTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(createTodoResponse.error).contains(INVALID_VALUE_ERROR)
            assertThat(todoList).extracting("id").doesNotContain(todoItemNegativeId.id)
        }
    }

    @Test
    @DisplayName("Should not create todo without id")
    @Description("POST should not create a new item if id is missing")
    fun shouldReturnErrorOnMissingId() {
        val createTodoResponse =
            step("Create new item without id") { todoController.createTodoInvalidTypes(todoItemMissingId) }

        step("Check that new item is not created and error returned") {
            val todoList = todoController.getTodoList().body

            assertThat(createTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(createTodoResponse.error).contains(MISSING_FIELD_ERROR.format("id"))
            assertThat(todoList).extracting("text").doesNotContain(todoItemMissingId.text)
        }
    }

    @Test
    @DisplayName("Should not create todo without text")
    @Description("POST should not create a new item if text field is missing")
    fun shouldReturnErrorOnMissingText() {
        val createTodoResponse =
            step("Create new item without text field") { todoController.createTodoInvalidTypes(todoItemMissingText) }

        step("Check that new item is not created and error returned") {
            val todoList = todoController.getTodoList().body

            assertThat(createTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(createTodoResponse.error).contains(MISSING_FIELD_ERROR.format("text"))
            assertThat(todoList).extracting("id").doesNotContain(todoItemMissingText.id)
        }
    }

    @Test
    @DisplayName("Should not create todo without completed")
    @Description("POST should not create a new item if completed field is missing")
    fun shouldReturnErrorOnMissingCompleted() {
        val createTodoResponse =
            step("Create new item without completed field") { todoController.createTodoInvalidTypes(todoItemMissingCompleted) }

        step("Check that new item is not created and error returned") {
            val todoList = todoController.getTodoList().body

            assertThat(createTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(createTodoResponse.error).contains(MISSING_FIELD_ERROR.format("completed"))
            assertThat(todoList).extracting("id").doesNotContain(todoItemMissingCompleted.id)
        }
    }
}
