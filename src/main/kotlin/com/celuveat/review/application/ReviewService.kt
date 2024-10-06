package com.celuveat.review.application

import com.celuveat.common.utils.throwWhen
import com.celuveat.member.application.port.out.ReadMemberPort
import com.celuveat.restaurant.application.port.out.ReadRestaurantPort
import com.celuveat.review.application.port.`in`.ClickHelpfulReviewUseCase
import com.celuveat.review.application.port.`in`.DeleteHelpfulReviewUseCase
import com.celuveat.review.application.port.`in`.DeleteReviewUseCase
import com.celuveat.review.application.port.`in`.UpdateReviewUseCase
import com.celuveat.review.application.port.`in`.WriteReviewUseCase
import com.celuveat.review.application.port.`in`.command.UpdateReviewCommand
import com.celuveat.review.application.port.`in`.command.WriteReviewCommand
import com.celuveat.review.application.port.out.DeleteHelpfulReviewPort
import com.celuveat.review.application.port.out.DeleteReviewPort
import com.celuveat.review.application.port.out.ReadHelpfulReviewPort
import com.celuveat.review.application.port.out.ReadReviewPort
import com.celuveat.review.application.port.out.SaveHelpfulReviewPort
import com.celuveat.review.application.port.out.SaveReviewPort
import com.celuveat.review.domain.ReviewImage
import com.celuveat.review.exception.AlreadyClickHelpfulReviewException
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val readMemberPort: ReadMemberPort,
    private val readRestaurantPort: ReadRestaurantPort,
    private val readReviewPort: ReadReviewPort,
    private val readHelpfulReviewPort: ReadHelpfulReviewPort,
    private val saveHelpfulReviewPort: SaveHelpfulReviewPort,
    private val saveReviewPort: SaveReviewPort,
    private val deleteReviewPort: DeleteReviewPort,
    private val deleteHelpfulReviewPort: DeleteHelpfulReviewPort,
) : WriteReviewUseCase,
    UpdateReviewUseCase,
    DeleteReviewUseCase,
    ClickHelpfulReviewUseCase,
    DeleteHelpfulReviewUseCase {
    override fun write(command: WriteReviewCommand): Long {
        val member = readMemberPort.readById(command.memberId)
        val restaurant = readRestaurantPort.readById(command.restaurantId)
        val review = command.toReview(member, restaurant)
        return saveReviewPort.save(review).id
    }

    override fun update(command: UpdateReviewCommand) {
        val member = readMemberPort.readById(command.memberId)
        val review = readReviewPort.readById(command.reviewId)
        review.validateWriter(member)
        review.update(command.content, command.star, command.images.map { ReviewImage(imageUrl = it) })
        saveReviewPort.save(review)
    }

    override fun delete(
        memberId: Long,
        reviewId: Long,
    ) {
        val member = readMemberPort.readById(memberId)
        val review = readReviewPort.readById(reviewId)
        review.validateWriter(member)
        deleteReviewPort.delete(review)
    }

    override fun clickHelpfulReview(
        memberId: Long,
        reviewId: Long,
    ) {
        throwWhen(
            readHelpfulReviewPort.existsByReviewAndMember(
                reviewId = reviewId,
                memberId = memberId,
            ),
        ) { throw AlreadyClickHelpfulReviewException }
        val member = readMemberPort.readById(memberId)
        val review = readReviewPort.readById(reviewId)
        val helpfulReview = review.clickHelpful(member)
        saveReviewPort.save(review)
        saveHelpfulReviewPort.save(helpfulReview)
    }

    override fun deleteHelpfulReview(
        memberId: Long,
        reviewId: Long,
    ) {
        val helpfulReview = readHelpfulReviewPort.readByReviewAndMember(reviewId, memberId)
        helpfulReview.unClick()
        saveReviewPort.save(helpfulReview.review)
        deleteHelpfulReviewPort.deleteHelpfulReview(helpfulReview)
    }
}
