package page.clab.api.domain.community.board.application.service;

import com.drew.lang.annotations.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.mapper.BoardDtoMapper;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveDeletedBoardsUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedBoardsRetrievalService implements RetrieveDeletedBoardsUseCase {

    private final RetrieveBoardPort retrieveBoardPort;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BoardListResponseDto> retrieveDeletedBoards(Pageable pageable) {
        Page<Board> boards = retrieveBoardPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(boards.map(board ->
                mapToBoardListResponseDto(board, getMemberDetailedInfoByBoard(board))));
    }

    private MemberDetailedInfoDto getMemberDetailedInfoByBoard(Board board) {
        return externalRetrieveMemberUseCase.getMemberDetailedInfoById(board.getMemberId());
    }

    @NotNull
    private BoardListResponseDto mapToBoardListResponseDto(Board board, MemberDetailedInfoDto memberInfo) {
        Long commentCount = externalRetrieveCommentUseCase.countByBoardId(board.getId());
        return BoardDtoMapper.toBoardListResponseDto(board, memberInfo, commentCount);
    }
}
