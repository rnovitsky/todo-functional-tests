package com.bhft.todo

import com.bhft.todo.domain.controller.dto.TodoItem
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
        todoController.createTodo(todoItem)
        val createSameTodoResponse = todoController.createTodo(todoItem)

        val todoListResponse = todoController.getTodoList()
        val todoList = runBlocking { todoListResponse.body<List<TodoItem>>() }

        assertEquals(HttpStatusCode.BadRequest, createSameTodoResponse.status)
        assertEquals(1, todoList.count { it.id == todoItem.id })
    }

    @AfterTest
    fun deleteCreatedItems() {
        todoControllerWithAuth.deleteTodo(todoItem.id)
    }
}
