package com.celuveat.sample.application

import com.celuveat.sample.application.port.`in`.SaveSampleUseCase
import com.celuveat.sample.application.port.`in`.command.SaveSampleCommand
import com.celuveat.sample.application.port.out.SaveSamplePort
import com.celuveat.sample.domain.Sample
import org.springframework.stereotype.Service

@Service
class SampleService(
    private val saveSamplePort: SaveSamplePort,
) : SaveSampleUseCase {

    override fun saveSample(command: SaveSampleCommand): Sample {
        return saveSamplePort.save(command.toSample())
    }
}
