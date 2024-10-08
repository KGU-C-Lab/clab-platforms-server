package page.clab.api.domain.members.blog.application.dto.mapper;

import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.blog.application.dto.request.BlogRequestDto;
import page.clab.api.domain.members.blog.application.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.members.blog.application.dto.response.BlogResponseDto;
import page.clab.api.domain.members.blog.domain.Blog;

public class BlogDtoMapper {

    public static Blog toBlog(BlogRequestDto requestDto, String memberId) {
        return Blog.builder()
                .memberId(memberId)
                .title(requestDto.getTitle())
                .subTitle(requestDto.getSubTitle())
                .content(requestDto.getContent())
                .hyperlink(requestDto.getHyperlink())
                .imageUrl(requestDto.getImageUrl())
                .isDeleted(false)
                .build();
    }

    public static BlogDetailsResponseDto toBlogDetailsResponseDto(Blog blog, MemberBasicInfoDto memberInfo, boolean isOwner) {
        return BlogDetailsResponseDto.builder()
                .id(blog.getId())
                .memberId(memberInfo.getMemberId())
                .name(memberInfo.getMemberName())
                .title(blog.getTitle())
                .subTitle(blog.getSubTitle())
                .content(blog.getContent())
                .imageUrl(blog.getImageUrl())
                .hyperlink(blog.getHyperlink())
                .isOwner(isOwner)
                .createdAt(blog.getCreatedAt())
                .build();
    }

    public static BlogResponseDto toBlogResponseDto(Blog blog) {
        return BlogResponseDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .subTitle(blog.getSubTitle())
                .imageUrl(blog.getImageUrl())
                .hyperlink(blog.getHyperlink())
                .createdAt(blog.getCreatedAt())
                .build();
    }
}
