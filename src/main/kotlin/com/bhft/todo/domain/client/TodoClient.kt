package com.bhft.todo.domain.client

import com.bhft.todo.core.http.commonHttpClient
import com.bhft.todo.domain.config.todoConfig
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*

val todoClient = commonHttpClient(todoConfig.service.url)

val todoClientWithAuth =
    todoClient.config {
        Auth {
            basic {
                sendWithoutRequest { true }
                credentials {
                    with(todoConfig.service.credentials) {
                        BasicAuthCredentials(username, password)
                    }
                }
            }
        }
    }
