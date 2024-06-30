package com.celuveat.sample.application.port.`in`

import com.celuveat.sample.application.port.`in`.command.SaveSampleCommand
import com.celuveat.sample.domain.Sample

interface SaveSampleUseCase {

    fun saveSample(command: SaveSampleCommand): Sample
}
