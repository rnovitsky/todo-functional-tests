package com.bhft.todo.domain.controller

import com.bhft.todo.core.http.HttpResponseWrapper
import com.bhft.todo.core.http.wrap
import com.bhft.todo.domain.controller.dto.TodoItem
import com.bhft.todo.domain.controller.dto.TodoItemInvalidTypes
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking

/**
 * Controller to use API of the tested app.
 * Dependency injection allows to have several controllers using different clients.
 */
class TodoController(private val todoClient: HttpClient) {
    fun getTodoList(offset: Int? = null, limit: Int? = null): HttpResponseWrapper<List<TodoItem>> =
        runBlocking {
            todoClient.get(TODOS_ENDPOINT) {
                parameter("offset", offset)
                parameter("limit", limit)
            }.wrap<List<TodoItem>>()
        }

    fun createTodo(todo: TodoItem) =
        runBlocking {
            todoClient.post(TODOS_ENDPOINT) {
                setBody(todo)
            }.wrap<String>()
        }

    /**
     * Separate method used only for negative tests
     * (does not follow a contract provided by tested app).
     */
    fun createTodoInvalidTypes(todo: TodoItemInvalidTypes) =
        runBlocking {
            todoClient.post(TODOS_ENDPOINT) {
                setBody(todo)
            }.wrap<String>()
        }

    fun updateTodo(idToUpdate: Long, todo: TodoItem) =
        runBlocking {
            todoClient.put("$TODOS_ENDPOINT/$idToUpdate") {
                setBody(todo)
            }.wrap<String>()
        }

    /**
     * Separate method used only for negative tests
     * (does not follow a contract provided by tested app).
     */
    fun updateTodoInvalidTypes(idToUpdate: Long, todo: TodoItemInvalidTypes) =
        runBlocking {
            todoClient.put("$TODOS_ENDPOINT/$idToUpdate") {
                setBody(todo)
            }.wrap<String>()
        }

    fun deleteTodo(todoId: Long) =
        runBlocking {
            todoClient.delete("$TODOS_ENDPOINT/$todoId")
        }
}

private const val TODOS_ENDPOINT = "/todos"
