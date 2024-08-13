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
            member = memberPersistenceMapper.toEntity(helpfulReview.member),
        )
    }

    fun toDomain(
        helpfulReview: HelpfulReviewJpaEntity,
        images: List<ReviewImageJpaEntity>,
    ): HelpfulReview {
        return HelpfulReview(
            id = helpfulReview.id,
            review = reviewPersistenceMapper.toDomain(helpfulReview.review, images),
            member = memberPersistenceMapper.toDomain(helpfulReview.member),
        )
    }
}
