package com.celuveat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class CeluveatApplication

fun main(args: Array<String>) {
    runApplication<CeluveatApplication>(*args)
}
