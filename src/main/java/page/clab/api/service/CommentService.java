package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.CommentRepository;
import page.clab.api.type.dto.CommentRequestDto;
import page.clab.api.type.dto.CommentResponseDto;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Comment;
import page.clab.api.type.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardService boardService;

    private final MemberService memberService;

    public void createComment(Long boardId, CommentRequestDto commentRequestDto) {
        Board board = boardService.getBoardById(boardId);
        Comment comment = Comment.of(commentRequestDto);
        comment.setWriter(memberService.getCurrentMember());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setBoard(board);
        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, CommentRequestDto commentRequestDto) throws PermissionDeniedException {
        Comment comment = getCommentByIdOrThrow(commentId);
        if (!Objects.equals(memberService.getCurrentMember().getId(), comment.getWriter().getId())){
            throw new PermissionDeniedException("댓글 작성자만 수정할 수 있습니다.");
        }
        comment.setContent(commentRequestDto.getContent());
        comment.setUpdateTime(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) throws PermissionDeniedException{
        Member member = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        if (!Objects.equals(comment.getWriter().getId(), member.getId()) || !memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("댓글 작성자만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }

    public List<CommentResponseDto> getComments(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        return comments.stream()
                .map(CommentResponseDto::of)
                .collect(Collectors.toList());
    }

    public Comment getCommentByIdOrThrow(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }

}
