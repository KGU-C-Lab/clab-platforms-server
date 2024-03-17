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
import page.clab.api.domain.sharedAccount.exception.InvalidUsageTimeException;
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
        try {
            Long sharedAccountId = sharedAccountUsageRequestDto.getSharedAccountId();
            SharedAccount sharedAccount = sharedAccountService.getSharedAccountByIdOrThrow(sharedAccountId);
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime startTime = sharedAccountUsageRequestDto.getStartTime() != null ? sharedAccountUsageRequestDto.getStartTime() : currentDateTime;
            LocalDateTime endTime = sharedAccountUsageRequestDto.getEndTime();

            if (endTime.isBefore(currentDateTime)) {
                throw new InvalidUsageTimeException("이용 종료 시간은 현재 시간 이후여야 합니다.");
            }
            if (endTime.isBefore(startTime)) {
                throw new InvalidUsageTimeException("이용 종료 시간은 시작 시간 이후여야 합니다.");
            }

            String memberId = memberService.getCurrentMember().getId();
            SharedAccountUsage sharedAccountUsage = SharedAccountUsage.builder()
                    .sharedAccount(sharedAccount)
                    .memberId(memberId)
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();

            validateSharedAccountUsage(sharedAccountId, startTime, endTime);

            if (!currentDateTime.isBefore(startTime) && currentDateTime.isBefore(endTime)) {
                sharedAccountUsage.setStatus(SharedAccountUsageStatus.IN_USE);
                sharedAccount.setInUse(true);
            } else if (currentDateTime.isAfter(endTime)) {
                sharedAccountUsage.setStatus(SharedAccountUsageStatus.COMPLETED);
            } else if (currentDateTime.isBefore(startTime)) {
                sharedAccountUsage.setStatus(SharedAccountUsageStatus.RESERVED);
            }
            sharedAccountUsageRepository.save(sharedAccountUsage);
            sharedAccountService.save(sharedAccount);
            return sharedAccountUsage.getId();
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomOptimisticLockingFailureException("공유 계정 이용 요청에 실패했습니다. 다시 시도해주세요.");
        }
    }

    private void validateSharedAccountUsage(Long sharedAccountId, LocalDateTime startTime, LocalDateTime endTime) throws SharedAccountUsageStateException {
        List<SharedAccountUsage> reservedUsages = sharedAccountUsageRepository
                .findBySharedAccountIdAndStatusAndStartTimeBeforeAndEndTimeAfter(
                        sharedAccountId, SharedAccountUsageStatus.RESERVED, endTime, startTime);
        if (!reservedUsages.isEmpty()) {
            throw new SharedAccountUsageStateException("해당 시간대와 겹치는 예약 내역이 있습니다. 다른 시간대를 선택해주세요.");
        }
        List<SharedAccountUsage> inUseUsages = sharedAccountUsageRepository
                .findBySharedAccountIdAndStatusAndStartTimeBeforeAndEndTimeAfter(
                        sharedAccountId, SharedAccountUsageStatus.IN_USE, endTime, startTime);
        if (!inUseUsages.isEmpty()) {
            throw new SharedAccountUsageStateException("해당 시간대에 이미 이용 중인 공유 계정이 있습니다. 다른 시간대를 선택해주세요.");
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
        if (!(sharedAccountUsage.getMemberId().equals(member.getId()) || member.isAdminRole())) {
            throw new PermissionDeniedException("공유 계정 이용 상태 변경 권한이 없습니다.");
        }
        if (SharedAccountUsageStatus.IN_USE.equals(sharedAccountUsage.getStatus())) {
            if (SharedAccountUsageStatus.CANCELED.equals(status) || SharedAccountUsageStatus.COMPLETED.equals(status)) {
                SharedAccount sharedAccount = sharedAccountUsage.getSharedAccount();
                sharedAccount.setInUse(false);
                sharedAccountUsage.setStatus(status);
                sharedAccountService.save(sharedAccount);
                sharedAccountUsageRepository.save(sharedAccountUsage);
                return sharedAccount.getId();
            }
        } else if (SharedAccountUsageStatus.RESERVED.equals(sharedAccountUsage.getStatus())) {
            if (SharedAccountUsageStatus.CANCELED.equals(status)) {
                sharedAccountUsage.setStatus(status);
                sharedAccountUsageRepository.save(sharedAccountUsage);
                return sharedAccountUsage.getId();
            }
            throw new SharedAccountUsageStateException("예약된 공유 계정은 취소만 가능합니다.");
        }
        throw new SharedAccountUsageStateException("이용 중 취소/완료, 예약 취소만 가능합니다.");
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void updateSharedAccountUsageStatus() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        updateOverdueReservedUsages(currentDateTime);
        updateStartableReservedUsages(currentDateTime);
        updateCompletedInUseUsages(currentDateTime);
    }

    private void updateOverdueReservedUsages(LocalDateTime currentDateTime) {
        // 이미 종료 시간이 지난 예약된 상태의 사용 내역을 완료로 업데이트
        List<SharedAccountUsage> overdueReservedUsages = sharedAccountUsageRepository
                .findByStatusAndEndTimeBefore(SharedAccountUsageStatus.RESERVED, currentDateTime);
        overdueReservedUsages.forEach(this::updateUsageStatusToCompleted);
    }

    private void updateStartableReservedUsages(LocalDateTime currentDateTime) {
        // 현재 시간이 시작 시간과 종료 시간 사이인 예약된 상태의 사용 내역을 이용 중으로 업데이트
        List<SharedAccountUsage> startableReservedUsages = sharedAccountUsageRepository
                .findByStatusAndStartTimeBeforeAndEndTimeAfter(SharedAccountUsageStatus.RESERVED, currentDateTime, currentDateTime);
        startableReservedUsages.forEach(this::updateUsageStatusToInUse);
    }

    private void updateCompletedInUseUsages(LocalDateTime currentDateTime) {
        // 이용 중 상태이고, 현재 시간보다 종료 시간이 이전인 경우 완료로 업데이트
        List<SharedAccountUsage> completedInUseUsages = sharedAccountUsageRepository
                .findByStatusAndEndTimeBefore(SharedAccountUsageStatus.IN_USE, currentDateTime);
        completedInUseUsages.forEach(usage -> {
            updateUsageStatusToCompleted(usage);
            checkAndUpdateSharedAccountInUseStatus(usage.getSharedAccount());
        });
    }

    private void updateUsageStatusToInUse(SharedAccountUsage usage) {
        log.info("{}: 공유 계정 이용 내역을 이용 중으로 변경합니다.", usage.getSharedAccount().getUsername());
        usage.setStatus(SharedAccountUsageStatus.IN_USE);
        usage.getSharedAccount().setInUse(true);
        sharedAccountUsageRepository.save(usage);
    }

    private void updateUsageStatusToCompleted(SharedAccountUsage usage) {
        log.info("{}: 공유 계정 이용 내역을 완료로 변경합니다.", usage.getSharedAccount().getUsername());
        usage.setStatus(SharedAccountUsageStatus.COMPLETED);
        usage.getSharedAccount().setInUse(false);
        sharedAccountUsageRepository.save(usage);
    }

    private void checkAndUpdateSharedAccountInUseStatus(SharedAccount sharedAccount) {
        // 현재 시간을 기준으로 공유 계정이 다른 사용 내역에 의해 이용 중인지 확인
        boolean isInUse = sharedAccountUsageRepository.existsBySharedAccountAndStatusAndEndTimeAfter(sharedAccount, SharedAccountUsageStatus.IN_USE, LocalDateTime.now());
        // 다른 사용 내역에 의해 이용 중이면 isInUse 상태를 true로 유지
        if (isInUse) {
            sharedAccount.setInUse(true);
            sharedAccountService.save(sharedAccount);
        }
    }

    public SharedAccountUsage getSharedAccountUsageByIdOrThrow(Long usageId) {
        return sharedAccountUsageRepository.findById(usageId)
                .orElseThrow(() -> new NotFoundException("공유 계정 이용 내역을 찾을 수 없습니다."));
    }

}
