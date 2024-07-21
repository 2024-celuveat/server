package com.celuveat.common.utils

inline fun <reified T> Set<T>.doesNotContain(element: T): Boolean {
    return !contains(element)
}
