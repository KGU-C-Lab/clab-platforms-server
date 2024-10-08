package page.clab.api.external.community.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.mapper.BoardDtoMapper;
import page.clab.api.domain.community.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.application.service.BoardRetrievalService;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.external.community.board.application.port.ExternalRetrieveBoardUseCase;

@Service
@RequiredArgsConstructor
public class ExternalBoardRetrievalService implements ExternalRetrieveBoardUseCase {

    private final BoardRetrievalService boardRetrievalService;
    private final RetrieveBoardPort retrieveBoardPort;

    @Transactional(readOnly = true)
    @Override
    public Board getById(Long targetId) {
        return boardRetrievalService.getById(targetId);
    }

    @Transactional(readOnly = true)
    @Override
    public BoardCommentInfoDto getBoardCommentInfoById(Long boardId) {
        Board board = retrieveBoardPort.findByIdRegardlessOfDeletion(boardId);
        return BoardDtoMapper.toBoardCommentInfoDto(board);
    }
}
