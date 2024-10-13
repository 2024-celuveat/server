package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "celebrity_youtube_content")
@Entity
class CelebrityYoutubeContentJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celebrity_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val celebrity: CelebrityJpaEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "youtube_content_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val youtubeContent: YoutubeContentJpaEntity,
) : RootEntity<Long>() {
    override fun readId(): Long {
        return this.id
    }
}
