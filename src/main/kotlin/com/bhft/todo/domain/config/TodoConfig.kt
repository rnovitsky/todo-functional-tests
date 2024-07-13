package com.bhft.todo.domain.config

import com.bhft.todo.domain.config.dto.Config
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource

/**
 * Test application configuration.
 *
 * Note: when using Testcontainers URLs are not taken from config, but generated at runtime
 */
val todoConfig =
    ConfigLoaderBuilder.default()
        .addResourceSource("/configuration.yaml")
        .build()
        .loadConfigOrThrow<Config>()
