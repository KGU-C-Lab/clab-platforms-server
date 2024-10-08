package page.clab.api.domain.community.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.comment.application.dto.mapper.CommentDtoMapper;
import page.clab.api.domain.community.comment.application.dto.response.CommentResponseDto;
import page.clab.api.domain.community.comment.application.port.in.RetrieveCommentUseCase;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentRetrievalService implements RetrieveCommentUseCase {

    private final RetrieveCommentPort retrieveCommentPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<CommentResponseDto> retrieveComments(Long boardId, Pageable pageable) {
        return getAllComments(boardId, pageable);
    }

    @Override
    public Comment getById(Long commentId) {
        return retrieveCommentPort.getById(commentId);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<CommentResponseDto> getAllComments(Long boardId, Pageable pageable) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Page<Comment> comments = retrieveCommentPort.findAllByBoardIdAndParentIsNull(boardId, pageable);
        List<CommentResponseDto> commentDtos = comments.stream()
                .map(comment -> toCommentResponseDtoWithMemberInfo(comment, currentMemberId))
                .toList();
        Long totalElements = retrieveCommentPort.countByBoardId(boardId);
        Page<CommentResponseDto> commentDtoPage = new PageImpl<>(commentDtos, pageable, comments.getTotalElements());
        return new PagedResponseDto<>(commentDtoPage, totalElements, getNumberOfCurrentPageComments(comments, commentDtos));
    }

    private CommentResponseDto toCommentResponseDtoWithMemberInfo(Comment comment, String currentMemberId) {
        MemberDetailedInfoDto memberInfo = externalRetrieveMemberUseCase.getMemberDetailedInfoById(comment.getWriterId());
        List<CommentResponseDto> childrenDtos = comment.getChildren().stream()
                .map(child -> toCommentResponseDtoWithMemberInfo(child, currentMemberId))
                .toList();
        boolean isOwner = comment.isOwner(currentMemberId);
        return CommentDtoMapper.toCommentResponseDto(comment, memberInfo, isOwner, childrenDtos);
    }

    private int getNumberOfCurrentPageComments(Page<Comment> comments, List<CommentResponseDto> commentDtos) {
        return comments.getNumberOfElements() +
                commentDtos.stream()
                        .mapToInt(comment -> comment.getChildren().size())
                        .sum();
    }
}
