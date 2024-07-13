package com.bhft.todo.core.http

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*

data class HttpResponseWrapper<T>(
    val status: HttpStatusCode,
    val body: T? = null,
    val error: String? = null,
    val originalResponse: HttpResponse
)

suspend inline fun <reified T> HttpResponse.wrap(): HttpResponseWrapper<T> =
    try {
        HttpResponseWrapper(
            status = this.status,
            body = this.body<T>(),
            originalResponse = this
        )
    } catch (e: NoTransformationFoundException) {
        HttpResponseWrapper(
            status = this.status,
            error = this.body<String>(),
            originalResponse = this
        )
    }
