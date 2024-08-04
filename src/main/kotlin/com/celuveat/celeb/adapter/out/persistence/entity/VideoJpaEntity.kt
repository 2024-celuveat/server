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
import java.time.LocalDate

@Entity
class VideoJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val videoUrl: String,
    val uploadDate: LocalDate,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "youtube_content_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val youtubeContent: YoutubeContentJpaEntity,
) : RootEntity<Long>() {
    override fun id(): Long {
        return this.id
    }
}
