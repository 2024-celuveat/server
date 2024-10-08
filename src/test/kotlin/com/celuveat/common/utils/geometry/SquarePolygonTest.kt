package com.celuveat.common.utils.geometry

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SquarePolygonTest : StringSpec({
    "SquarePolygon 생성" {
        val squarePolygon = SquarePolygon.ofNullable(
            lowLongitude = 1.0,
            highLongitude = 2.0,
            lowLatitude = 3.0,
            highLatitude = 4.0,
        )
        squarePolygon shouldNotBe null
    }

    "좌표가 하나라도 null이면 SquarePolygon은 생성 되지 않는다" {
        val squarePolygon = SquarePolygon.ofNullable(
            lowLongitude = 1.0,
            highLongitude = 2.0,
            lowLatitude = 3.0,
            highLatitude = null,
        )
        squarePolygon shouldBe null
    }
})
