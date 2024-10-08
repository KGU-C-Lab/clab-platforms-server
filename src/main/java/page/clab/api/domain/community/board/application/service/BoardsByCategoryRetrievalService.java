package page.clab.api.domain.community.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.mapper.BoardDtoMapper;
import page.clab.api.domain.community.board.application.dto.response.BoardCategoryResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardsByCategoryUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.BoardCategory;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class BoardsByCategoryRetrievalService implements RetrieveBoardsByCategoryUseCase {

    private final RetrieveBoardPort retrieveBoardPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;

    @Transactional
    @Override
    public PagedResponseDto<BoardCategoryResponseDto> retrieveBoardsByCategory(BoardCategory category, Pageable pageable) {
        Page<Board> boards = retrieveBoardPort.findAllByCategory(category, pageable);
        return new PagedResponseDto<>(boards.map(board -> {
            long commentCount = externalRetrieveCommentUseCase.countByBoardId(board.getId());
            return  BoardDtoMapper.toBoardCategoryResponseDto(board, getMemberDetailedInfoByBoard(board), commentCount);
        }));
    }

    private MemberDetailedInfoDto getMemberDetailedInfoByBoard(Board board) {
        return externalRetrieveMemberUseCase.getMemberDetailedInfoById(board.getMemberId());
    }
}
