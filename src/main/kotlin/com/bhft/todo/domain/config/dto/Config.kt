package com.bhft.todo.domain.config.dto

data class Config(
    val service: ServiceConfig,
    val image: ImageConfig,
    val testcontainers: TestcontainersConfig
)
