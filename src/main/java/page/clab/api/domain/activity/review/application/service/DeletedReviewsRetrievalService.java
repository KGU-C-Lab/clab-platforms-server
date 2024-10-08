package page.clab.api.domain.activity.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activity.review.application.dto.mapper.ReviewDtoMapper;
import page.clab.api.domain.activity.review.application.dto.response.ReviewResponseDto;
import page.clab.api.domain.activity.review.application.port.in.RetrieveDeletedReviewsUseCase;
import page.clab.api.domain.activity.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.activity.review.domain.Review;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberReviewInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedReviewsRetrievalService implements RetrieveDeletedReviewsUseCase {

    private final RetrieveReviewPort retrieveReviewPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ReviewResponseDto> retrieveDeletedReviews(Pageable pageable) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Page<Review> reviews = retrieveReviewPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(reviews.map(review -> {
            MemberReviewInfoDto reviewer = externalRetrieveMemberUseCase.getMemberReviewInfoById(review.getMemberId());
            return ReviewDtoMapper.toReviewResponseDto(review, reviewer, review.isOwner(currentMemberId));
        }));
    }
}
