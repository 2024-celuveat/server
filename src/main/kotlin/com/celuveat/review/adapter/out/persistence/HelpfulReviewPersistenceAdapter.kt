package com.celuveat.review.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.review.adapter.out.persistence.entity.HelpfulReviewJpaEntityRepository
import com.celuveat.review.adapter.out.persistence.entity.HelpfulReviewPersistenceMapper
import com.celuveat.review.adapter.out.persistence.entity.ReviewImageJpaEntityRepository
import com.celuveat.review.application.port.out.DeleteHelpfulReviewPort
import com.celuveat.review.application.port.out.FindHelpfulReviewPort
import com.celuveat.review.application.port.out.SaveHelpfulReviewPort
import com.celuveat.review.domain.HelpfulReview

// TODO test
@Adapter
class HelpfulReviewPersistenceAdapter(
    private val helpfulReviewJpaEntityRepository: HelpfulReviewJpaEntityRepository,
    private val helpfulReviewPersistenceMapper: HelpfulReviewPersistenceMapper,
    private val reviewImageJpaEntityRepository: ReviewImageJpaEntityRepository,
) : SaveHelpfulReviewPort, FindHelpfulReviewPort, DeleteHelpfulReviewPort {
    override fun save(helpfulReview: HelpfulReview): HelpfulReview {
        val entity = helpfulReviewPersistenceMapper.toEntity(helpfulReview)
        val saved = helpfulReviewJpaEntityRepository.save(entity)
        val images = reviewImageJpaEntityRepository.findAllByReview(saved.review)
        return helpfulReviewPersistenceMapper.toDomain(saved, images)
    }

    override fun getByReviewAndMember(
        reviewId: Long,
        memberId: Long,
    ): HelpfulReview {
        val entity = helpfulReviewJpaEntityRepository.getByMemberIdAndReviewId(reviewId, memberId)
        val images = reviewImageJpaEntityRepository.findAllByReview(entity.review)
        return helpfulReviewPersistenceMapper.toDomain(entity, images)
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
