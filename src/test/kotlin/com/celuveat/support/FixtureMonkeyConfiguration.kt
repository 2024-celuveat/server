package com.celuveat.support

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin

val sut: FixtureMonkey = FixtureMonkey.builder()
    .plugin(KotlinPlugin())
    .build()
