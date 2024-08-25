package com.celuveat.common.utils.geometry

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
