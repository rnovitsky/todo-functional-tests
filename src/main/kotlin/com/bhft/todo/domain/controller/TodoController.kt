package com.bhft.todo.domain.controller

import com.bhft.todo.domain.controller.dto.TodoItem
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

class TodoController(private val todoClient: HttpClient) {
    fun getTodoList(offset: Int? = null, limit: Int? = null): HttpResponse =
        runBlocking {
            todoClient.get("/todos") {
                parameter("offset", offset)
                parameter("limit", limit)
            }
        }

    fun createTodo(todo: TodoItem): HttpResponse =
        runBlocking {
            todoClient.post("/todos") {
                setBody(todo)
            }
        }

    fun deleteTodo(todoId: Long) =
        runBlocking {
            todoClient.delete("/todos/$todoId")
        }
}
