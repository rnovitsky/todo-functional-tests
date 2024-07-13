package com.bhft.todo.domain.controller

import com.bhft.todo.core.http.HttpResponseWrapper
import com.bhft.todo.core.http.wrap
import com.bhft.todo.domain.controller.dto.TodoItem
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking

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
            }
        }

    fun deleteTodo(todoId: Long) =
        runBlocking {
            todoClient.delete("$TODOS_ENDPOINT/$todoId")
        }
}

private const val TODOS_ENDPOINT = "/todos"
