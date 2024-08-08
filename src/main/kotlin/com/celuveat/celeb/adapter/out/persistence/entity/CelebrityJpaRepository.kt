package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.celeb.exceptions.NotFoundCelebrityException
import com.celuveat.common.utils.findByIdOrThrow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CelebrityJpaRepository : JpaRepository<CelebrityJpaEntity, Long> {
    override fun getById(id: Long): CelebrityJpaEntity {
        return this.findByIdOrThrow(id) { NotFoundCelebrityException }
    }

    @Query(
        """
            SELECT DISTINCT (c), yc.subscriberCount
            FROM CelebrityYoutubeContentJpaEntity cyc
            JOIN cyc.youtubeContent yc
            JOIN cyc.celebrity c
            ORDER BY yc.subscriberCount DESC LIMIT 15
        """
    )
    fun findAllBySubscriberCountDesc(): Set<CelebrityJpaEntity>
}
