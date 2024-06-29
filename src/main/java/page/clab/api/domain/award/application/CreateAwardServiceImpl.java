package page.clab.api.domain.award.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.dao.AwardRepository;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.request.AwardRequestDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CreateAwardServiceImpl implements CreateAwardService {

    private final MemberLookupService memberLookupService;
    private final ValidationService validationService;
    private final AwardRepository awardRepository;

    @Transactional
    @Override
    public Long execute(AwardRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Award award = AwardRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(award);
        return awardRepository.save(award).getId();
    }
}