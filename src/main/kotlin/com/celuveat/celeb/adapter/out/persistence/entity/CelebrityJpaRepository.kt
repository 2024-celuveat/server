package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.celeb.exceptions.NotFoundCelebrityException
import com.celuveat.common.utils.findByIdOrThrow
import org.springframework.data.jpa.repository.JpaRepository

interface CelebrityJpaRepository : JpaRepository<CelebrityJpaEntity, Long> {
    override fun getById(id: Long): CelebrityJpaEntity {
        return this.findByIdOrThrow(id) { NotFoundCelebrityException }
    }
}
