package com.bhft.todo.core.http

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*

data class HttpResponseWrapper<T>(val status: HttpStatusCode, val body: T)

suspend inline fun <reified T> HttpResponse.wrap(): HttpResponseWrapper<T> =
    HttpResponseWrapper(
        status = this.status,
        body = this.body<T>()
    )
