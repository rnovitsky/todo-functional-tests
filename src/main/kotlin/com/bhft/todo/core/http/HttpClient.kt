package com.bhft.todo.core.http

import com.bhft.todo.core.http.logger.Log4jLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
fun commonHttpClient(baseUrl: String) =
    HttpClient(CIO) {
        install(Logging) {
            logger = Log4jLogger()
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(
                Json { explicitNulls = false }
            )
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            url(baseUrl)
        }
    }
