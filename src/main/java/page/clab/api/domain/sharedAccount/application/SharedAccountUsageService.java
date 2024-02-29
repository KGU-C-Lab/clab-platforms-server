package page.clab.api.domain.sharedAccount.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.sharedAccount.dao.SharedAccountUsageRepository;
import page.clab.api.domain.sharedAccount.domain.SharedAccount;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsage;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsageStatus;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountUsageRequestDto;
import page.clab.api.domain.sharedAccount.dto.response.SharedAccountUsageResponseDto;
import page.clab.api.domain.sharedAccount.exception.SharedAccountUsageStateException;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SharedAccountUsageService {

    private final SharedAccountService sharedAccountService;

    private final MemberService memberService;

    private final SharedAccountUsageRepository sharedAccountUsageRepository;

    @Transactional
    public Long requestSharedAccountUsage(SharedAccountUsageRequestDto sharedAccountUsageRequestDto) throws CustomOptimisticLockingFailureException {
        Long sharedAccountId = sharedAccountUsageRequestDto.getSharedAccountId();
        SharedAccount sharedAccount = sharedAccountService.getSharedAccountByIdOrThrow(sharedAccountId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startTime = sharedAccountUsageRequestDto.getStartTime();
        LocalDateTime endTime = sharedAccountUsageRequestDto.getEndTime();
        if (startTime.isBefore(currentDateTime)) {
            throw new IllegalArgumentException("이용 시작 시간은 현재 시간 이후여야 합니다.");
        }
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("이용 종료 시간은 시작 시간 이후여야 합니다.");
        }
        try {
            String memberId = memberService.getCurrentMember().getId();
            SharedAccountUsage sharedAccountUsage = SharedAccountUsage.of(sharedAccountUsageRequestDto);
            sharedAccountUsage.setId(null);
            sharedAccountUsage.setSharedAccount(sharedAccount);
            sharedAccountUsage.setMemberId(memberId);
            List<SharedAccountUsage> reservedSharedAccountUsages = sharedAccountUsageRepository
                    .findBySharedAccountIdAndStatusAndStartTimeBeforeAndEndTimeAfter(
                            sharedAccountId, SharedAccountUsageStatus.RESERVED, endTime, startTime);
            if (!reservedSharedAccountUsages.isEmpty()) {
                throw new SharedAccountUsageStateException("해당 시간대와 겹치는 예약 내역이 있습니다. 다른 시간대를 선택해주세요.");
            }
            List<SharedAccountUsage> inUseSharedAccountUsages = sharedAccountUsageRepository
                    .findBySharedAccountIdAndStatusAndStartTimeBeforeAndEndTimeAfter(
                            sharedAccountId, SharedAccountUsageStatus.IN_USE, endTime, startTime);
            if (!inUseSharedAccountUsages.isEmpty()) {
                throw new SharedAccountUsageStateException("해당 시간대에 이미 이용 중인 공유 계정이 있습니다. 다른 시간대를 선택해주세요.");
            }
            if (currentDateTime.isAfter(startTime) && currentDateTime.isBefore(endTime)) {
                sharedAccountUsage.setStatus(SharedAccountUsageStatus.IN_USE);
                sharedAccount.setInUse(true);
            } else {
                sharedAccountUsage.setStatus(SharedAccountUsageStatus.RESERVED);
            }
            sharedAccountUsageRepository.save(sharedAccountUsage);
            return sharedAccountService.save(sharedAccount).getId();
        } catch (ObjectOptimisticLockingFailureException e) {
              throw new CustomOptimisticLockingFailureException("공유 계정 이용 요청에 실패했습니다. 다시 시도해주세요.");
        }
    }

    public PagedResponseDto<SharedAccountUsageResponseDto> getSharedAccountUsages(Pageable pageable) {
        Page<SharedAccountUsage> sharedAccountUsages = sharedAccountUsageRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(sharedAccountUsages.map(SharedAccountUsageResponseDto::of));
    }

    @Transactional
    public Long updateSharedAccountUsage(Long usageId, SharedAccountUsageStatus status) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        SharedAccountUsage sharedAccountUsage = getSharedAccountUsageByIdOrThrow(usageId);
        if (!(sharedAccountUsage.getMemberId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("공유 계정 이용 상태 변경 권한이 없습니다.");
        }
        if (!sharedAccountUsage.getStatus().equals(SharedAccountUsageStatus.IN_USE)) {
            throw new SharedAccountUsageStateException("이용 중인 공유 계정만 상태 변경이 가능합니다.");
        }
        if (!status.equals(SharedAccountUsageStatus.IN_USE)) {
            SharedAccount sharedAccount = sharedAccountUsage.getSharedAccount();
            sharedAccount.setInUse(false);
            sharedAccountUsage.setStatus(status);
            sharedAccountUsage.setEndTime(LocalDateTime.now());
            sharedAccountService.save(sharedAccount);
            return sharedAccountUsageRepository.save(sharedAccountUsage).getId();
        }
        return sharedAccountUsage.getId();
    }

    @Scheduled(fixedRate = 60000)
    public void updateSharedAccountUsageStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<SharedAccountUsage> completedSharedAccountUsages = sharedAccountUsageRepository
                .findByStatusAndEndTimeBefore(SharedAccountUsageStatus.IN_USE, now);
        completedSharedAccountUsages.forEach(sharedAccountUsage -> {
            SharedAccount sharedAccount = sharedAccountUsage.getSharedAccount();
            sharedAccount.setInUse(false);
            sharedAccountUsage.setStatus(SharedAccountUsageStatus.COMPLETED);
            sharedAccountService.save(sharedAccount);
            sharedAccountUsageRepository.save(sharedAccountUsage);
        });
        List<SharedAccountUsage> reservedSharedAccountUsages = sharedAccountUsageRepository
                .findByStatusAndEndTimeBefore(SharedAccountUsageStatus.RESERVED, now);
        reservedSharedAccountUsages.forEach(sharedAccountUsage -> {
            SharedAccount sharedAccount = sharedAccountUsage.getSharedAccount();
            sharedAccount.setInUse(true);
            sharedAccountUsage.setStatus(SharedAccountUsageStatus.IN_USE);
            sharedAccountService.save(sharedAccount);
            sharedAccountUsageRepository.save(sharedAccountUsage);
        });
    }

    public SharedAccountUsage getSharedAccountUsageByIdOrThrow(Long usageId) {
        return sharedAccountUsageRepository.findById(usageId)
                .orElseThrow(() -> new NotFoundException("공유 계정 이용 내역을 찾을 수 없습니다."));
    }

}
