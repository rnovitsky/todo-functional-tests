package com.bhft.todo.domain.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.bhft.todo.domain.config.dto.Config

val todoConfig =
    ConfigLoaderBuilder.default()
        .addResourceSource("/configuration.yaml")
        .build()
        .loadConfigOrThrow<Config>()
