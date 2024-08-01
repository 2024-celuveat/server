package com.celuveat.celeb.adapter.out.persistence.entity

import org.springframework.data.jpa.repository.JpaRepository

interface YoutubeChannelJpaRepository : JpaRepository<YoutubeChannelJpaEntity, Long> {
    fun findAllByCelebrityIdIn(ids: List<Long>): List<YoutubeChannelJpaEntity>
}
