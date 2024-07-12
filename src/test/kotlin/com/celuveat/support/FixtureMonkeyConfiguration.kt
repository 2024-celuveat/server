package com.celuveat.support

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin

val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
    .plugin(KotlinPlugin())
    .build()
