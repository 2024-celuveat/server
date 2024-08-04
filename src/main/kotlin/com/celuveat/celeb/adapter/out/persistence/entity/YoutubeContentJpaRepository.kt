package com.celuveat.celeb.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository

interface YoutubeContentJpaRepository : JpaRepository<YoutubeContentJpaEntity, Long> {
    fun findAllByCelebrityIdIn(ids: List<Long>): List<YoutubeContentJpaEntity>
}
