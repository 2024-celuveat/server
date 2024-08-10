package com.celuveat.review.adapter.out.persistence.entity

import com.celuveat.common.annotation.Mapper
import com.celuveat.member.adapter.out.persistence.entity.MemberPersistenceMapper
import com.celuveat.review.domain.HelpfulReview

@Mapper
class HelpfulReviewPersistenceMapper(
    private val reviewPersistenceMapper: ReviewPersistenceMapper,
    private val memberPersistenceMapper: MemberPersistenceMapper,
) {
    fun toEntity(helpfulReview: HelpfulReview): HelpfulReviewJpaEntity {
        return HelpfulReviewJpaEntity(
            review = reviewPersistenceMapper.toEntity(helpfulReview.review),
            member = memberPersistenceMapper.toEntity(helpfulReview.clicker),
        )
    }

    fun toDomain(entity: HelpfulReviewJpaEntity): HelpfulReview {
        return HelpfulReview(
            id = entity.id,
            review = reviewPersistenceMapper.toDomain(entity.review),
            clicker = memberPersistenceMapper.toDomain(entity.member),
        )
    }
}
