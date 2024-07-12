package com.celuveat.common.application

import com.celuveat.common.port.out.MockDependencies
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.clearAllMocks
import io.mockk.unmockkAll

abstract class ServiceTest : BehaviorSpec() {
    val mocks = MockDependencies()
    val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        clearAllMocks()
        unmockkAll()
    }
}
