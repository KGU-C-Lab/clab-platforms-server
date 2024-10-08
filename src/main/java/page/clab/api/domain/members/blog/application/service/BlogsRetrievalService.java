package page.clab.api.domain.members.blog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.blog.application.dto.mapper.BlogDtoMapper;
import page.clab.api.domain.members.blog.application.dto.response.BlogResponseDto;
import page.clab.api.domain.members.blog.application.port.in.RetrieveBlogsUseCase;
import page.clab.api.domain.members.blog.application.port.out.RetrieveBlogPort;
import page.clab.api.domain.members.blog.domain.Blog;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class BlogsRetrievalService implements RetrieveBlogsUseCase {

    private final RetrieveBlogPort retrieveBlogPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BlogResponseDto> retrieveBlogs(String title, String memberName, Pageable pageable) {
        Page<Blog> blogs = retrieveBlogPort.findByConditions(title, memberName, pageable);
        return new PagedResponseDto<>(blogs.map(BlogDtoMapper::toBlogResponseDto));
    }
}
