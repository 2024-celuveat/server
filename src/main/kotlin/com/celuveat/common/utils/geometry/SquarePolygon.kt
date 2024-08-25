package com.celuveat.common.utils.geometry

import com.celuveat.common.utils.throwWhen
import kotlin.math.cos

class SquarePolygon private constructor(
    val lowLongitude: Double,
    val highLongitude: Double,
    val lowLatitude: Double,
    val highLatitude: Double,
) {

    companion object {
        fun ofNullable(
            lowLongitude: Double?,
            highLongitude: Double?,
            lowLatitude: Double?,
            highLatitude: Double?,
        ): SquarePolygon? = if (listOf(lowLongitude, highLongitude, lowLatitude, highLatitude).all { it != null }) {
            SquarePolygon(lowLongitude!!, highLongitude!!, lowLatitude!!, highLatitude!!)
        } else {
            null
        }

        fun fromCenter(centerLatitude: Double, centerLongitude: Double, squareLength: Double = 2.0): SquarePolygon {
            throwWhen(squareLength < 1) { IllegalArgumentException("SquareLength must be greater than 1") }

            // 위도와 경도를 라디안으로 변환
            val latRad = Math.toRadians(centerLatitude)

            // 위도와 경도 이동 거리 계산 (1km 이동에 대한 라디안 차이)
            val deltaLat = (squareLength / 2) / 6371.0
            val deltaLon = (squareLength / 2) / (6371.0 * cos(latRad))

            // 경계 좌표 계산
            val lowLatitude = centerLatitude - Math.toDegrees(deltaLat)
            val highLatitude = centerLatitude + Math.toDegrees(deltaLat)
            val lowLongitude = centerLongitude - Math.toDegrees(deltaLon)
            val highLongitude = centerLongitude + Math.toDegrees(deltaLon)

            return SquarePolygon(lowLongitude, highLongitude, lowLatitude, highLatitude)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SquarePolygon

        if (lowLongitude != other.lowLongitude) return false
        if (highLongitude != other.highLongitude) return false
        if (lowLatitude != other.lowLatitude) return false
        if (highLatitude != other.highLatitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lowLongitude.hashCode()
        result = 31 * result + highLongitude.hashCode()
        result = 31 * result + lowLatitude.hashCode()
        result = 31 * result + highLatitude.hashCode()
        return result
    }
}
