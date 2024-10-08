package page.clab.api.domain.members.blog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.blog.application.dto.mapper.BlogDtoMapper;
import page.clab.api.domain.members.blog.application.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.members.blog.application.port.in.RetrieveBlogDetailsUseCase;
import page.clab.api.domain.members.blog.application.port.out.RetrieveBlogPort;
import page.clab.api.domain.members.blog.domain.Blog;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class BlogDetailsRetrievalService implements RetrieveBlogDetailsUseCase {

    private final RetrieveBlogPort retrieveBlogPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public BlogDetailsResponseDto retrieveBlogDetails(Long blogId) {
        MemberBasicInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberBasicInfo();
        Blog blog = retrieveBlogPort.getById(blogId);
        boolean isOwner = blog.isOwner(currentMemberInfo.getMemberId());
        return BlogDtoMapper.toBlogDetailsResponseDto(blog, currentMemberInfo, isOwner);
    }
}
