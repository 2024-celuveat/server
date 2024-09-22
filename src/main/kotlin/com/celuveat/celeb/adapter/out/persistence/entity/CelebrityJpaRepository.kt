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
            ORDER BY yc.subscriberCount DESC LIMIT 10
        """,
    )
    fun findAllBySubscriberCountDescTop10(): Set<CelebrityJpaEntity>

    @Query(value = "SELECT c FROM CelebrityJpaEntity c WHERE c.name LIKE %:name%")
    fun readByNameContains(name: String): List<CelebrityJpaEntity>
}
