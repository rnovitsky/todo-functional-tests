package com.bhft.todo

import com.bhft.todo.domain.controller.dto.TodoItem
import com.bhft.todo.domain.data.TodoGenerator
import io.ktor.http.*
import jdk.jfr.Description
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("POST /todos")
class PostTodoTest : BaseTest() {
    private val todoItem = TodoItem(id = 1, text = "created from autotest", completed = true)

    @Test
    @DisplayName("Should create new todo item")
    @Description("POST should allow to create new item")
    fun shouldCreateNewTodo() {
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
    @Description("Should not create a new item if item with the same id exists")
    fun shouldNotCreateTodoWithSameId() {
        val firstTodo =
            step("Create first item") { TodoGenerator.createTodo() }

        val createSameTodoResponse =
            step("Create second item with same id") { todoController.createTodo(firstTodo) }

        step("Check that only one item is created") {
            val todoList = todoController.getTodoList().body

            assertThat(createSameTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(todoList).filteredOn { it.id == firstTodo.id}.hasSize(1)
        }
    }

    @AfterEach
    fun deleteManuallyCreatedItems() {
        todoControllerWithAuth.deleteTodo(todoItem.id)
    }
}
