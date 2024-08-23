package com.celuveat.common.utils.geometry

class SquarePolygon private constructor(
    private val _lowLongitude: Double?,
    private val _highLongitude: Double?,
    private val _lowLatitude: Double?,
    private val _highLatitude: Double?,
) {
    private val isAvailableBox: Boolean =
        listOf(_lowLongitude, _highLongitude, _lowLatitude, _highLatitude).all { it != null }

    val lowLongitude: Double
        get() = if (isAvailableBox) _lowLongitude!! else throw IllegalStateException()

    val highLongitude: Double
        get() = if (isAvailableBox) _highLongitude!! else throw IllegalStateException()

    val lowLatitude: Double
        get() = if (isAvailableBox) _lowLatitude!! else throw IllegalStateException()

    val highLatitude: Double
        get() = if (isAvailableBox) _highLatitude!! else throw IllegalStateException()

    companion object {
        fun ofNullable(
            lowLongitude: Double?,
            highLongitude: Double?,
            lowLatitude: Double?,
            highLatitude: Double?,
        ): SquarePolygon? = if (listOf(lowLongitude, highLongitude, lowLatitude, highLatitude).all { it != null }) {
            SquarePolygon(lowLongitude, highLongitude, lowLatitude, highLatitude)
        } else {
            null
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SquarePolygon

        if (_lowLongitude != other._lowLongitude) return false
        if (_highLongitude != other._highLongitude) return false
        if (_lowLatitude != other._lowLatitude) return false
        if (_highLatitude != other._highLatitude) return false
        if (isAvailableBox != other.isAvailableBox) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _lowLongitude?.hashCode() ?: 0
        result = 31 * result + (_highLongitude?.hashCode() ?: 0)
        result = 31 * result + (_lowLatitude?.hashCode() ?: 0)
        result = 31 * result + (_highLatitude?.hashCode() ?: 0)
        result = 31 * result + isAvailableBox.hashCode()
        return result
    }

}
