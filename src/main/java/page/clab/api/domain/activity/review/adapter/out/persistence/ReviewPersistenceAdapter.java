package page.clab.api.domain.activity.review.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.activity.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.activity.review.domain.Review;
import page.clab.api.global.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class ReviewPersistenceAdapter implements
        RegisterReviewPort,
        RetrieveReviewPort {

    private final ReviewRepository repository;
    private final ReviewMapper mapper;

    @Override
    public Review save(Review review) {
        ReviewJpaEntity entity = mapper.toEntity(review);
        ReviewJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Review getById(Long reviewId) {
        return repository.findById(reviewId)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Review] id: " + reviewId + "에 해당하는 리뷰가 존재하지 않습니다."));
    }

    @Override
    public Page<Review> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Review> findAllByMemberId(String memberId, Pageable pageable) {
        return repository.findAllByMemberId(memberId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByMemberIdAndActivityGroup(String memberId, ActivityGroup activityGroup) {
        return repository.existsByMemberIdAndActivityGroup(memberId, activityGroup);
    }

    @Override
    public Page<Review> findByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable) {
        return repository.findByConditions(memberId, memberName, activityId, isPublic, pageable)
                .map(mapper::toDomain);
    }
}
