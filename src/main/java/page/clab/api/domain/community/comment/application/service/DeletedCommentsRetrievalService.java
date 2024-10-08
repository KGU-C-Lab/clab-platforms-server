package page.clab.api.domain.community.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.comment.application.dto.mapper.CommentDtoMapper;
import page.clab.api.domain.community.comment.application.dto.response.DeletedCommentResponseDto;
import page.clab.api.domain.community.comment.application.port.in.RetrieveDeletedCommentsUseCase;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeletedCommentsRetrievalService implements RetrieveDeletedCommentsUseCase {

    private final RetrieveCommentPort retrieveCommentPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<DeletedCommentResponseDto> retrieveDeletedComments(Long boardId, Pageable pageable) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Page<Comment> comments = retrieveCommentPort.findAllByIsDeletedTrueAndBoardId(boardId, pageable);
        List<DeletedCommentResponseDto> deletedCommentDtos = comments.stream()
                .map(comment -> {
                    MemberDetailedInfoDto memberInfo = externalRetrieveMemberUseCase.getMemberDetailedInfoById(comment.getWriterId());
                    return CommentDtoMapper.toDeletedCommentResponseDto(comment, memberInfo, comment.isOwner(currentMemberId));
                })
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(deletedCommentDtos, pageable, comments.getTotalElements()));
    }
}
