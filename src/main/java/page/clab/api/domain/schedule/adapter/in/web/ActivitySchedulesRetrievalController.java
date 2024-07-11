package page.clab.api.domain.schedule.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.schedule.application.port.in.RetrieveActivitySchedulesUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "일정")
public class ActivitySchedulesRetrievalController {

    private final RetrieveActivitySchedulesUseCase retrieveActivitySchedulesUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[U] 내 활동 일정 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, endDateTime, startDateTime, memberId")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @GetMapping("/activity")
    public ApiResponse<PagedResponseDto<ScheduleResponseDto>> retrieveActivitySchedules(
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "startDateTime") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, ScheduleResponseDto.class);
        PagedResponseDto<ScheduleResponseDto> schedules =
                retrieveActivitySchedulesUseCase.retrieveActivitySchedules(startDate, endDate, pageable);
        return ApiResponse.success(schedules);
    }
}
