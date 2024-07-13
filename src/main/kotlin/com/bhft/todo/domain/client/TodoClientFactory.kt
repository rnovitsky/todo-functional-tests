package com.bhft.todo.domain.client

import com.bhft.todo.core.http.commonHttpClient
import com.bhft.todo.domain.config.todoConfig
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*

/**
 * Factory class for instantiating different HTTP clients.
 *
 * Ktor doesn't allow inheritance, so each client should copy configuration of the base client and extend it.
 * [Documentation](https://api.ktor.io/ktor-client/ktor-client-core/io.ktor.client/-http-client/config.html)
 */
object TodoClientFactory {
    fun todoClient(url: String) = commonHttpClient(url)

    fun todoClientWithAuth(url: String) =
        todoClient(url).config {
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
}
