package page.clab.api.domain.workExperience.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.workExperience.application.port.in.UpdateWorkExperienceUseCase;
import page.clab.api.domain.workExperience.dto.request.WorkExperienceUpdateRequestDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/work-experiences")
@RequiredArgsConstructor
@Tag(name = "WorkExperience", description = "경력사항")
public class WorkExperienceUpdateController {

    private final UpdateWorkExperienceUseCase updateWorkExperienceUseCase;

    @Operation(summary = "[U] 경력사항 수정", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{workExperienceId}")
    public ApiResponse<Long> updateWorkExperience(
            @PathVariable(name = "workExperienceId") Long workExperienceId,
            @Valid @RequestBody WorkExperienceUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = updateWorkExperienceUseCase.update(workExperienceId, requestDto);
        return ApiResponse.success(id);
    }
}
