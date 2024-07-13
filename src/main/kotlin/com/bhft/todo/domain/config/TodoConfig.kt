package com.bhft.todo.domain.config

import com.bhft.todo.domain.config.dto.Config
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource

val todoConfig =
    ConfigLoaderBuilder.default()
        .addResourceSource("/configuration.yaml")
        .build()
        .loadConfigOrThrow<Config>()
