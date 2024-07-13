package com.bhft.todo

import com.bhft.todo.core.logger.WithLogger
import com.bhft.todo.domain.client.TodoClientFactory
import com.bhft.todo.domain.config.todoConfig
import com.bhft.todo.domain.controller.TodoController
import com.bhft.todo.domain.data.TodoGenerator
import io.qameta.allure.Allure
import org.junit.jupiter.api.AfterEach
import org.testcontainers.containers.GenericContainer

abstract class BaseTest : WithLogger {
    private val todoAppContainerUrl: String

    init {
        if (!todoConfig.testcontainers.enabled) {
            todoAppContainerUrl = todoConfig.service.url
        } else {
            // Singleton container realization by Testcontainers
            // Container is launched once before all tests and shuts down automatically
            todoAppContainer.start()
            todoAppContainerUrl = with(todoAppContainer) { "http://$host:$firstMappedPort" }
        }
    }

    private val todoClient = TodoClientFactory.todoClient(todoAppContainerUrl)
    private val todoClientWithAuth = TodoClientFactory.todoClientWithAuth(todoAppContainerUrl)

    protected val todoController = TodoController(todoClient)
    protected val todoControllerWithAuth = TodoController(todoClientWithAuth)

    protected val todoGenerator = TodoGenerator(todoClientWithAuth)

    protected fun todoItem() = todoGenerator.generateTodo()

    protected fun <T> step(name: String, body: Allure.ThrowableRunnable<T>): T {
        logger.info(name)
        return Allure.step(name, body)
    }

    @AfterEach
    fun deleteCreatedItems() {
        todoGenerator.deleteAllGeneratedTodos()
    }

    companion object {
        val todoAppContainer: GenericContainer<*> =
            with(todoConfig.image) {
                GenericContainer(name).withExposedPorts(port)
            }
    }
}
