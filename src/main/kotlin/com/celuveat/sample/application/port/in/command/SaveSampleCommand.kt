package com.celuveat.sample.application.port.`in`.command

import com.celuveat.sample.domain.Sample

data class SaveSampleCommand(
    val name: String,
) {
    fun toSample(): Sample {
        return Sample(name)
    }
}
