package page.clab.api.domain.jobPosting.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.jobPosting.application.JobPostingRemoveUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/job-postings")
@RequiredArgsConstructor
@Tag(name = "JobPosting", description = "채용 공고")
public class JobPostingRemoveController {

    private final JobPostingRemoveUseCase jobPostingRemoveUseCase;

    @Operation(summary = "[A] 채용 공고 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{jobPostingId}")
    public ApiResponse<Long> removeJobPosting(
            @PathVariable(name = "jobPostingId") Long jobPostingId
    ) {
        Long id = jobPostingRemoveUseCase.remove(jobPostingId);
        return ApiResponse.success(id);
    }
}