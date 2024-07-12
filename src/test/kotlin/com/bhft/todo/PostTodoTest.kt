package com.bhft.todo

import com.bhft.todo.domain.controller.dto.TodoItem
import com.bhft.todo.domain.data.TodoGenerator
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import kotlin.test.*

@DisplayName("POST /todos")
class PostTodoTest : BaseTest() {
    private val todoItem = TodoItem(id = 1, text = "created from autotest", completed = true)

    @Test
    @DisplayName("Should create new todo item")
    fun shouldCreateNewTodo() {
        val createTodoResponse = todoController.createTodo(todoItem)

        val todoListResponse = todoController.getTodoList()
        val todoList = runBlocking { todoListResponse.body<List<TodoItem>>() }

        assertEquals(HttpStatusCode.Created, createTodoResponse.status)
        assertContains(todoList, todoItem)
    }

    @Test
    @DisplayName("Should not create todo with same id")
    fun shouldNotCreateTodoWithSameId() {
        val firstTodo = TodoGenerator.createTodo()

        val createSameTodoResponse = todoController.createTodo(firstTodo!!)

        val todoListResponse = todoController.getTodoList()
        val todoList = runBlocking { todoListResponse.body<List<TodoItem>>() }

        assertEquals(HttpStatusCode.BadRequest, createSameTodoResponse.status)
        assertEquals(1, todoList.count { it.id == firstTodo.id })
    }

    @AfterTest
    fun deleteManuallyCreatedItems() {
        todoControllerWithAuth.deleteTodo(todoItem.id)
    }
}
