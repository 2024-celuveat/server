package com.celuveat.review.adapter.out.persistence

import com.celuveat.common.annotation.Adapter
import com.celuveat.common.application.port.`in`.result.SliceResult
import com.celuveat.review.adapter.out.persistence.entity.ReviewJpaEntityRepository
import com.celuveat.review.adapter.out.persistence.entity.ReviewPersistenceMapper
import com.celuveat.review.application.port.out.DeleteReviewPort
import com.celuveat.review.application.port.out.FindReviewPort
import com.celuveat.review.application.port.out.SaveReviewPort
import com.celuveat.review.domain.Review
import org.springframework.data.domain.Pageable

@Adapter
class ReviewPersistenceAdapter(
    private val reviewJpaEntityRepository: ReviewJpaEntityRepository,
    private val reviewPersistenceMapper: ReviewPersistenceMapper
) : SaveReviewPort, DeleteReviewPort, FindReviewPort {

    override fun save(review: Review): Review {
        val entity = reviewPersistenceMapper.toEntity(review)
        val saved = reviewJpaEntityRepository.save(entity)
        return reviewPersistenceMapper.toDomain(saved)
    }

    override fun delete(review: Review) {
        val entity = reviewPersistenceMapper.toEntity(review)
        reviewJpaEntityRepository.delete(entity)
    }

    override fun getById(reviewId: Long): Review {
        val review = reviewJpaEntityRepository.getById(reviewId)
        return reviewPersistenceMapper.toDomain(review)
    }

    override fun findAllByRestaurantId(restaurantsId: Long, page: Pageable): SliceResult<Review> {
        val reviews = reviewJpaEntityRepository.findAllByRestaurantId(restaurantsId, page)
            .map { reviewPersistenceMapper.toDomain(it) }
        return SliceResult.from(reviews)
    }
}
