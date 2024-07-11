package com.bhft.todo


import com.bhft.todo.domain.controller.dto.TodoItem
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import kotlin.test.*

@DisplayName("DELETE /todos")
class DeleteTodoTest : BaseTest() {
    private val todoItem = TodoItem(id = 1, text = "to be deleted", completed = false)

    @Test
    @DisplayName("Should not delete item if not authorized")
    fun shouldNotDeleteTodoIfNotAuthorized() {
        todoController.createTodo(todoItem)
        val deleteTodoResponse = todoController.deleteTodo(todoItem.id)

        val todoListResponse = todoController.getTodoList()
        val todoList = runBlocking { todoListResponse.body<List<TodoItem>>() }

        assertEquals(HttpStatusCode.Unauthorized, deleteTodoResponse.status)
        assertContains(todoList, todoItem)
    }

    @Test
    @DisplayName("Should delete item if authorized")
    fun shouldDeleteTodoIfAuthorized() {
        todoController.createTodo(todoItem)
        val deleteTodoResponse = todoControllerWithAuth.deleteTodo(todoItem.id)

        val todoListResponse = todoController.getTodoList()
        val todoList = runBlocking { todoListResponse.body<List<TodoItem>>() }

        assertEquals(HttpStatusCode.NoContent, deleteTodoResponse.status)
        assertFalse(todoList.map { it.id }.contains(1))
    }

    @AfterTest
    fun deleteCreatedItems() {
        todoControllerWithAuth.deleteTodo(todoItem.id)
    }
}
