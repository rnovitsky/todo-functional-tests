package com.bhft.todo


import com.bhft.todo.domain.data.TodoGenerator
import io.ktor.http.*
import org.junit.jupiter.api.DisplayName
import kotlin.test.*

@DisplayName("DELETE /todos")
class DeleteTodoTest : BaseTest() {
    @Test
    @DisplayName("Should not delete item if not authorized")
    fun shouldNotDeleteTodoIfNotAuthorized() {
        val todoForDelete = TodoGenerator.createTodo()

        val deleteTodoResponse = todoController.deleteTodo(todoForDelete.id)

        val todoList = todoController.getTodoList().body

        assertEquals(HttpStatusCode.Unauthorized, deleteTodoResponse.status)
        assertContains(todoList, todoForDelete)
    }

    @Test
    @DisplayName("Should delete item if authorized")
    fun shouldDeleteTodoIfAuthorized() {
        val todoForDelete = TodoGenerator.createTodo()

        val deleteTodoResponse = todoControllerWithAuth.deleteTodo(todoForDelete.id)

        val todoList = todoController.getTodoList().body

        assertEquals(HttpStatusCode.NoContent, deleteTodoResponse.status)
        assertFalse(todoList.map { it.id }.contains(todoForDelete.id))
    }
}
