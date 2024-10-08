package page.clab.api.domain.members.blog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.blog.application.dto.mapper.BlogDtoMapper;
import page.clab.api.domain.members.blog.application.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.members.blog.application.port.in.RetrieveDeletedBlogsUseCase;
import page.clab.api.domain.members.blog.application.port.out.RetrieveBlogPort;
import page.clab.api.domain.members.blog.domain.Blog;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedBlogsRetrievalService implements RetrieveDeletedBlogsUseCase {

    private final RetrieveBlogPort retrieveBlogPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BlogDetailsResponseDto> retrieveDeletedBlogs(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberBasicInfo();
        Page<Blog> blogs = retrieveBlogPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(blogs.map(blog -> BlogDtoMapper.toBlogDetailsResponseDto(blog, currentMemberInfo, blog.isOwner(currentMemberInfo.getMemberId()))));
    }
}
