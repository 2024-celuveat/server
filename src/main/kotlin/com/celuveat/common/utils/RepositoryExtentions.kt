package com.celuveat.common.utils

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

inline fun <reified T, ID> CrudRepository<T, ID>.findByIdOrThrow(
    id: ID,
    exceptionSupplier: () -> RuntimeException = { throw IllegalArgumentException("Entity not found") },
): T = findByIdOrNull(id) ?: throw exceptionSupplier()
