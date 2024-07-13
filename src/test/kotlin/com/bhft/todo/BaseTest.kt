package com.bhft.todo

import com.bhft.todo.core.logger.WithLogger
import com.bhft.todo.domain.client.todoClient
import com.bhft.todo.domain.client.todoClientWithAuth
import com.bhft.todo.domain.controller.TodoController
import com.bhft.todo.domain.data.TodoGenerator
import org.junit.jupiter.api.AfterEach

open class BaseTest : WithLogger {
    protected val todoController = TodoController(todoClient)
    protected val todoControllerWithAuth = TodoController(todoClientWithAuth)

    @AfterEach
    fun deleteCreatedItems() {
        TodoGenerator.deleteAllGeneratedTodos()
    }
}
