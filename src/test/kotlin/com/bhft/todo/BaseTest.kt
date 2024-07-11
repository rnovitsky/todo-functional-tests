package com.bhft.todo

import com.bhft.todo.core.http.commonHttpClient
import com.bhft.todo.domain.controller.TodoController
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*

open class BaseTest {
    private val todoClient = commonHttpClient("http://localhost:8080")
    private val todoClientWithAuth =
        todoClient.config {
            Auth {
                basic {
                    credentials {
                        sendWithoutRequest { true }
                        BasicAuthCredentials(username = "admin", password = "admin")
                    }
                }
            }
        }

    protected val todoController = TodoController(todoClient)
    protected val todoControllerWithAuth = TodoController(todoClientWithAuth)
}
