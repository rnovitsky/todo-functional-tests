package com.bhft.todo.domain.client

import com.bhft.todo.core.http.commonHttpClient
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*

val todoClient = commonHttpClient("http://localhost:8080")

val todoClientWithAuth =
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
