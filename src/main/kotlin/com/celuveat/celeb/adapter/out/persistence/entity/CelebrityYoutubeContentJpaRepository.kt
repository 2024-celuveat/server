package com.celuveat.celeb.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface CelebrityYoutubeContentJpaRepository : JpaRepository<CelebrityYoutubeContentJpaEntity, Long> {
    @EntityGraph(attributePaths = ["youtubeContent"])
    fun findByCelebrityIdIn(celebrityIds: List<Long>): List<CelebrityYoutubeContentJpaEntity>

    @EntityGraph(attributePaths = ["youtubeContent"])
    fun findByCelebrity(celebrity: CelebrityJpaEntity): List<CelebrityYoutubeContentJpaEntity>

    @EntityGraph(attributePaths = ["youtubeContent", "celebrity"])
    fun findByYoutubeContentIdIn(youtubeContentIds: List<Long>): List<CelebrityYoutubeContentJpaEntity>
}
