package page.clab.api.domain.memberManagement.cloud.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.cloud.application.dto.mapper.CloudDtoMapper;
import page.clab.api.domain.memberManagement.cloud.application.dto.response.CloudUsageInfo;
import page.clab.api.domain.memberManagement.cloud.application.port.in.RetrieveAllCloudUsageUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.FileSystemUtil;

import java.io.File;

@Service
@RequiredArgsConstructor
public class CloudUsageRetrievalAllService implements RetrieveAllCloudUsageUseCase {

    private final RetrieveMemberPort retrieveMemberPort;

    @Value("${resource.file.path}")
    private String filePath;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<CloudUsageInfo> retrieveAllCloudUsages(Pageable pageable) {
        Page<Member> members = retrieveMemberPort.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(members.map(this::getCloudUsageForMember));
    }

    private CloudUsageInfo getCloudUsageForMember(Member member) {
        File directory = getMemberDirectory(member.getId());
        long usage = FileSystemUtil.calculateDirectorySize(directory);
        return CloudDtoMapper.toCloudUsageInfo(member.getId(), usage);
    }

    private File getMemberDirectory(String memberId) {
        return new File(filePath + "/members/" + memberId);
    }
}
