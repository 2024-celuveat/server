package com.celuveat.celeb.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface CelebrityYoutubeContentJpaRepository : JpaRepository<CelebrityYoutubeContentJpaEntity, Long> {
    @EntityGraph(attributePaths = ["youtubeContent"])
    fun findByCelebrityIdIn(celebrityId: List<Long>): List<CelebrityYoutubeContentJpaEntity>
}
