package page.clab.api.domain.award.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.dao.AwardRepository;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchAwardsServiceImpl implements FetchAwardsService {

    private final AwardRepository awardRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AwardResponseDto> execute(String memberId, Long year, Pageable pageable) {
        Page<Award> awards = awardRepository.findByConditions(memberId, year, pageable);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::toDto));
    }
}