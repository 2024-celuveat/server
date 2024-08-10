package com.celuveat.review.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.review.adapter.out.persistence.entity.HelpfulReviewJpaEntityRepository
import com.celuveat.review.adapter.out.persistence.entity.HelpfulReviewPersistenceMapper
import com.celuveat.review.application.port.out.DeleteHelpfulReviewPort
import com.celuveat.review.application.port.out.FindHelpfulReviewPort
import com.celuveat.review.application.port.out.SaveHelpfulReviewPort
import com.celuveat.review.domain.HelpfulReview

@Adapter
class HelpfulReviewPersistenceAdapter(
    private val helpfulReviewJpaEntityRepository: HelpfulReviewJpaEntityRepository,
    private val helpfulReviewPersistenceMapper: HelpfulReviewPersistenceMapper,
) : SaveHelpfulReviewPort, FindHelpfulReviewPort, DeleteHelpfulReviewPort {
    override fun save(helpfulReview: HelpfulReview): HelpfulReview {
        val entity = helpfulReviewPersistenceMapper.toEntity(helpfulReview)
        val saved = helpfulReviewJpaEntityRepository.save(entity)
        return helpfulReviewPersistenceMapper.toDomain(saved)
    }

    override fun getByReviewAndMember(
        reviewId: Long,
        memberId: Long,
    ): HelpfulReview {
        val entity = helpfulReviewJpaEntityRepository.getByMemberIdAndReviewId(reviewId, memberId)
        return helpfulReviewPersistenceMapper.toDomain(entity)
    }

    override fun existsByReviewAndMember(
        reviewId: Long,
        memberId: Long,
    ): Boolean {
        return helpfulReviewJpaEntityRepository.existsByReviewIdAndMemberId(reviewId, memberId)
    }

    override fun deleteHelpfulReview(helpfulReview: HelpfulReview) {
        val entity = helpfulReviewPersistenceMapper.toEntity(helpfulReview)
        helpfulReviewJpaEntityRepository.delete(entity)
    }
}
