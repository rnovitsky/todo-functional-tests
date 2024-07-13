package com.bhft.todo.core.http

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.OK

/**
 * Wrapper class for converting Ktor response object into a testable response object.
 * Allows to use response object in tests in synchronous manner (not dealing with coroutines).
 *
 * @param body parsed response body (only for successful response codes, otherwise - `null`)
 * @param error error if response was not successful, otherwise - `null`
 * @param originalResponse original Ktor response
 */
data class HttpResponseWrapper<T>(
    val status: HttpStatusCode,
    val body: T? = null,
    val error: String? = null,
    val originalResponse: HttpResponse
)

suspend inline fun <reified T> HttpResponse.wrap(): HttpResponseWrapper<T> =
    if (this.status in successStatuses) {
        HttpResponseWrapper(
            status = this.status,
            body = this.body<T>(),
            originalResponse = this
        )
    } else {
        HttpResponseWrapper(
            status = this.status,
            error = this.body<String>(),
            originalResponse = this
        )
    }

val successStatuses =
    listOf(
        OK,
        Created,
        NoContent
    )
