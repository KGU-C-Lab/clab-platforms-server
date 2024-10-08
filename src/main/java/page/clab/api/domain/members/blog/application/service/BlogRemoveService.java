package page.clab.api.domain.members.blog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.members.blog.application.port.in.RemoveBlogUseCase;
import page.clab.api.domain.members.blog.application.port.out.RegisterBlogPort;
import page.clab.api.domain.members.blog.application.port.out.RetrieveBlogPort;
import page.clab.api.domain.members.blog.domain.Blog;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class BlogRemoveService implements RemoveBlogUseCase {

    private final RetrieveBlogPort retrieveBlogPort;
    private final RegisterBlogPort registerBlogPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long removeBlog(Long blogId) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        Blog blog = retrieveBlogPort.getById(blogId);
        blog.validateAccessPermission(currentMember);
        blog.delete();
        return registerBlogPort.save(blog).getId();
    }
}
