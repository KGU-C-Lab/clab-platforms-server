package page.clab.api.domain.members.blog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.blog.application.dto.mapper.BlogDtoMapper;
import page.clab.api.domain.members.blog.application.dto.request.BlogRequestDto;
import page.clab.api.domain.members.blog.application.port.in.RegisterBlogUseCase;
import page.clab.api.domain.members.blog.application.port.out.RegisterBlogPort;
import page.clab.api.domain.members.blog.domain.Blog;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class BlogRegisterService implements RegisterBlogUseCase {

    private final RegisterBlogPort registerBlogPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long registerBlog(BlogRequestDto requestDto) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Blog blog = BlogDtoMapper.toBlog(requestDto, currentMemberId);
        return registerBlogPort.save(blog).getId();
    }
}
