package com.bhft.todo.core.http.logger

import io.ktor.client.plugins.logging.*
import org.apache.logging.log4j.LogManager

class Log4jLogger : Logger {
    private val logger = LogManager.getLogger("KtorLogger")

    override fun log(message: String) {
        val lines =
            message.split("\n").filterNot { line ->
                line.startsWith("BODY END") ||
                    line.startsWith("BODY START") ||
                    line.startsWith("COMMON HEADERS") ||
                    line.startsWith("CONTENT HEADERS")
            }

        val method =
            lines.find { it.startsWith("METHOD: HttpMethod(value=") }?.let {
                "METHOD: " + it.substringAfter("HttpMethod(value=").removeSuffix(")")
            } ?: ""

        val request =
            lines.find { it.startsWith("REQUEST: ") }?.let {
                "REQUEST: " + it.substringAfter("REQUEST: ")
            } ?: ""

        val from =
            lines.find { it.startsWith("FROM: ") }?.let {
                "FROM: " + it.substringAfter("FROM: ")
            } ?: ""

        val headers =
            lines.filter { line -> line.startsWith("-> ") }
                .filterNot { line -> line.contains("Authorization:") }
                .joinToString(", ") { it.replace("-> ", "") }
                .takeIf { it.isNotEmpty() }
                ?.let { "HEADERS: $it " } ?: ""

        val body =
            "BODY START\\s(.*)\\sBODY END".toRegex().find(message)
                ?.groupValues?.elementAt(1)
                ?.takeIf { it.isNotEmpty() }
                ?: "No body"

        val response =
            lines.find { it.startsWith("RESPONSE: ") }?.let {
                "RESPONSE: ${it.substringAfter("RESPONSE: ")} -> "
            } ?: ""

        val filteredMessage = "$response$method | $request$from | $headers | BODY: $body"

        logger.info(filteredMessage)
    }
}
