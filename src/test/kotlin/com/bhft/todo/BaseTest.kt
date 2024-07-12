package com.bhft.todo

import com.bhft.todo.domain.client.todoClient
import com.bhft.todo.domain.client.todoClientWithAuth
import com.bhft.todo.domain.controller.TodoController
import com.bhft.todo.domain.data.TodoGenerator
import kotlin.test.AfterTest

open class BaseTest {
    protected val todoController = TodoController(todoClient)
    protected val todoControllerWithAuth = TodoController(todoClientWithAuth)

    @AfterTest
    fun deleteCreatedItems() {
        TodoGenerator.deleteAllGeneratedTodos()
    }
}
