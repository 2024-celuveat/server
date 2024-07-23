package com.celuveat.sample.application.port.out

import com.celuveat.sample.domain.Sample

interface SaveSamplePort {
    fun save(sample: Sample): Sample
}
