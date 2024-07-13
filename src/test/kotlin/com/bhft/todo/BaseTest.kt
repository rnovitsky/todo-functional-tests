package com.bhft.todo

import com.bhft.todo.core.logger.WithLogger
import com.bhft.todo.domain.client.todoClient
import com.bhft.todo.domain.client.todoClientWithAuth
import com.bhft.todo.domain.controller.TodoController
import com.bhft.todo.domain.data.TodoGenerator
import io.qameta.allure.Allure
import org.junit.jupiter.api.AfterEach

open class BaseTest : WithLogger {
    protected val todoController = TodoController(todoClient)
    protected val todoControllerWithAuth = TodoController(todoClientWithAuth)

    protected fun <T> step(name: String, body: Allure.ThrowableRunnable<T>): T {
        logger.info(name)
        return Allure.step(name, body)
    }

    @AfterEach
    fun deleteCreatedItems() {
        TodoGenerator.deleteAllGeneratedTodos()
    }
}
