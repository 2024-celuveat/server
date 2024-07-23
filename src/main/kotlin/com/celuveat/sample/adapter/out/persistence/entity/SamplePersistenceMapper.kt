package com.celuveat.sample.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.sample.domain.Sample

@Mapper
class SamplePersistenceMapper {
    fun toEntity(sample: Sample): SampleJpaEntity {
        return SampleJpaEntity(
            id = sample.id,
            name = sample.name,
        )
    }

    fun toDomain(sampleJpaEntity: SampleJpaEntity): Sample {
        return Sample(
            id = sampleJpaEntity.id,
            name = sampleJpaEntity.name,
        )
    }
}
