package com.celuveat.common.utils

inline fun throwWhen(condition: Boolean, exceptionSupplier: () -> RuntimeException) {
    if (condition) throw exceptionSupplier()
}
