package com.celuveat.sample.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.sample.adapter.out.persistence.entity.SampleJpaRepository
import com.celuveat.sample.adapter.out.persistence.entity.SamplePersistenceMapper
import com.celuveat.sample.application.port.out.SaveSamplePort
import com.celuveat.sample.domain.Sample

@Adapter
class SamplePersistenceAdapter(
    private val mapper: SamplePersistenceMapper,
    private val sampleJpaRepository: SampleJpaRepository,
): SaveSamplePort {

    override fun save(sample: Sample): Sample {
        val sampleEntity = mapper.toEntity(sample)
        val saved = sampleJpaRepository.save(sampleEntity)
        return mapper.toDomain(saved)
    }
}
