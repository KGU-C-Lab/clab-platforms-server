package page.clab.api.domain.review.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.review.application.ReviewService;
import page.clab.api.domain.review.dto.request.ReviewRequestDto;
import page.clab.api.domain.review.dto.request.ReviewUpdateRequestDto;
import page.clab.api.domain.review.dto.response.ReviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "[U] 리뷰 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createReview(
            @Valid @RequestBody ReviewRequestDto requestDto
    ) {
        Long id = reviewService.createReview(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 리뷰 목록 조회(멤버 ID, 멤버 이름, 활동 ID, 공개 여부 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "4개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "멤버 ID, 멤버 이름, 활동 ID, 공개 여부 중 하나라도 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<ReviewResponseDto>> getReviewsByConditions(
            @RequestParam(name = "memberId", required = false) String memberId,
            @RequestParam(name = "memberName", required = false) String memberName,
            @RequestParam(name = "activityId", required = false) Long activityId,
            @RequestParam(name = "isPublic", required = false) Boolean isPublic,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ReviewResponseDto> reviews = reviewService.getReviewsByConditions(memberId, memberName, activityId, isPublic, pageable);
        return ApiResponse.success(reviews);
    }

    @Operation(summary = "[U] 나의 리뷰 목록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my")
    public ApiResponse<PagedResponseDto<ReviewResponseDto>> getMyReviews(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ReviewResponseDto> myReviews = reviewService.getMyReviews(pageable);
        return ApiResponse.success(myReviews);
    }

    @Operation(summary = "[U] 리뷰 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{reviewId}")
    public ApiResponse<Long> updateReview(
            @PathVariable(name = "reviewId") Long reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = reviewService.updateReview(reviewId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 리뷰 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Long> deleteReview(
            @PathVariable(name = "reviewId") Long reviewId
    ) throws PermissionDeniedException {
        Long id = reviewService.deleteReview(reviewId);
        return ApiResponse.success(id);
    }


    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 리뷰 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<ReviewResponseDto>> getDeletedReviews(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ReviewResponseDto> reviews = reviewService.getDeletedReviews(pageable);
        return ApiResponse.success(reviews);
    }

}
