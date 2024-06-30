package com.celuveat.sample.domain

class Sample(
    val id: Long = 0,
    val name: String,
) {
    constructor(name: String) : this(0, name)
}
