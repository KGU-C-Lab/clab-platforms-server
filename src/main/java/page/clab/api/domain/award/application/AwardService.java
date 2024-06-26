package page.clab.api.domain.award.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.dao.AwardRepository;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.request.AwardRequestDto;
import page.clab.api.domain.award.dto.request.AwardUpdateRequestDto;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final MemberLookupService memberLookupService;

    private final ValidationService validationService;

    private final AwardRepository awardRepository;

    @Transactional
    public Long createAward(AwardRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Award award = AwardRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(award);
        return awardRepository.save(award).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AwardResponseDto> getAwardsByConditions(String memberId, Long year, Pageable pageable) {
        Page<Award> awards = awardRepository.findByConditions(memberId, year, pageable);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AwardResponseDto> getMyAwards(Pageable pageable) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Page<Award> awards = getAwardByMemberId(pageable, currentMemberId);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::toDto));
    }

    @Transactional
    public Long updateAward(Long awardId, AwardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Award award = getAwardByIdOrThrow(awardId);
        award.validateAccessPermission(currentMemberInfo);
        award.update(requestDto);
        validationService.checkValid(award);
        return awardRepository.save(award).getId();
    }

    @Transactional
    public Long deleteAward(Long awardId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Award award = getAwardByIdOrThrow(awardId);
        award.validateAccessPermission(currentMemberInfo);
        award.delete();
        awardRepository.save(award);
        return award.getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AwardResponseDto> getDeletedAwards(Pageable pageable) {
        Page<Award> awards = awardRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::toDto));
    }

    private Award getAwardByIdOrThrow(Long awardId) {
        return awardRepository.findById(awardId)
                .orElseThrow(() -> new NotFoundException("해당 수상 이력이 존재하지 않습니다."));
    }

    private Page<Award> getAwardByMemberId(Pageable pageable, String memberId) {
        return awardRepository.findByMemberId(memberId, pageable);
    }

}