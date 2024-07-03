package page.clab.api.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.port.in.RetrieveDeletedCommentsUseCase;
import page.clab.api.domain.comment.application.port.out.RetrieveDeletedCommentsPort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.response.DeletedCommentResponseDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeletedCommentsRetrievalService implements RetrieveDeletedCommentsUseCase {

    private final RetrieveDeletedCommentsPort retrieveDeletedCommentsPort;
    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<DeletedCommentResponseDto> retrieve(Long boardId, Pageable pageable) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Page<Comment> comments = retrieveDeletedCommentsPort.findAllByIsDeletedTrueAndBoardId(boardId, pageable);
        List<DeletedCommentResponseDto> deletedCommentDtos = comments.stream()
                .map(comment -> {
                    MemberDetailedInfoDto memberInfo = retrieveMemberInfoUseCase.getMemberDetailedInfoById(comment.getWriterId());
                    return DeletedCommentResponseDto.toDto(comment, memberInfo, comment.isOwner(currentMemberId));
                })
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(deletedCommentDtos, pageable, comments.getTotalElements()));
    }
}
