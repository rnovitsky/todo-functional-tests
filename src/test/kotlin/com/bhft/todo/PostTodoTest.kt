package com.bhft.todo

import com.bhft.todo.domain.controller.dto.TodoItem
import com.bhft.todo.domain.data.TodoGenerator
import io.ktor.http.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("POST /todos")
class PostTodoTest : BaseTest() {
    private val todoItem = TodoItem(id = 1, text = "created from autotest", completed = true)

    @Test
    @DisplayName("Should create new todo item")
    fun shouldCreateNewTodo() {
        val createTodoResponse = todoController.createTodo(todoItem)

        val todoList = todoController.getTodoList().body

        assertThat(createTodoResponse.status).isEqualTo(HttpStatusCode.Created)
        assertThat(todoList).contains(todoItem)
    }

    @Test
    @DisplayName("Should not create todo with same id")
    fun shouldNotCreateTodoWithSameId() {
        val firstTodo = TodoGenerator.createTodo()

        val createSameTodoResponse = todoController.createTodo(firstTodo)

        val todoList = todoController.getTodoList().body

        assertThat(createSameTodoResponse.status).isEqualTo(HttpStatusCode.BadRequest)
        assertThat(todoList).filteredOn { it.id == firstTodo.id}.hasSize(1)
    }

    @AfterEach
    fun deleteManuallyCreatedItems() {
        todoControllerWithAuth.deleteTodo(todoItem.id)
    }
}
