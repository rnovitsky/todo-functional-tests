package com.bhft.todo.core.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

fun commonHttpClient(baseUrl: String) =
    HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            url(baseUrl)
        }
    }
