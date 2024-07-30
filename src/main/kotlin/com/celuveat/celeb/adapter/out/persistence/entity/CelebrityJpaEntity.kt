package com.celuveat.celeb.adapter.out.persistence.entity

import com.celuveat.common.adapter.out.persistence.entity.RootEntity
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany

@Entity
class CelebrityJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val profileImageUrl: String,
    val introduction: String,

    @OneToMany
    @JoinColumn(name = "celebrity_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val youtubeChannels: List<YoutubeChannelJpaEntity>,
) : RootEntity<Long>() {
    override fun id(): Long {
        return this.id
    }
}
