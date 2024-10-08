package page.clab.api.domain.community.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.community.comment.application.dto.mapper.CommentDtoMapper;
import page.clab.api.domain.community.comment.application.dto.response.CommentMyResponseDto;
import page.clab.api.domain.community.comment.application.port.in.RetrieveMyCommentsUseCase;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.community.board.application.port.ExternalRetrieveBoardUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyCommentsRetrievalService implements RetrieveMyCommentsUseCase {

    private final RetrieveCommentPort retrieveCommentPort;
    private final ExternalRetrieveBoardUseCase externalRetrieveBoardUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<CommentMyResponseDto> retrieveMyComments(Pageable pageable) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Page<Comment> comments = retrieveCommentPort.findAllByWriterId(currentMemberId, pageable);
        List<CommentMyResponseDto> dtos = comments.stream()
                .map(this::toCommentMyResponseDto)
                .filter(Objects::nonNull)
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(dtos, pageable, comments.getTotalElements()));
    }

    private CommentMyResponseDto toCommentMyResponseDto(Comment comment) {
        MemberDetailedInfoDto memberInfo = externalRetrieveMemberUseCase.getMemberDetailedInfoById(comment.getWriterId());
        BoardCommentInfoDto boardInfo = externalRetrieveBoardUseCase.getBoardCommentInfoById(comment.getBoardId());
        return CommentDtoMapper.toCommentMyResponseDto(comment, memberInfo, boardInfo, false);
    }
}
