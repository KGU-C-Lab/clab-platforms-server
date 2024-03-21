package page.clab.api.domain.comment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.dto.request.CommentRequestDto;
import page.clab.api.domain.comment.dto.request.CommentUpdateRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.util.ModelMapperUtil;
import page.clab.api.global.util.RandomNicknameUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member writer;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Size(min = 1, max = 1000, message = "{size.comment.content}")
    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties("children")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @JsonIgnoreProperties("parent")
    private List<Comment> children = new ArrayList<>();

    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean wantAnonymous;

    private Long likes;

    public static Comment create(CommentRequestDto commentRequestDto, Board board, Member member, Comment parent) {
        Comment comment = ModelMapperUtil.getModelMapper().map(commentRequestDto, Comment.class);
        comment.setBoard(board);
        comment.setWriter(member);
        comment.setNickname(RandomNicknameUtil.makeRandomNickname());
        comment.setLikes(0L);
        comment.parent = parent;
        return comment;
    }

    public void update(CommentUpdateRequestDto commentUpdateRequestDto) {
        Optional.ofNullable(commentUpdateRequestDto.getContent()).ifPresent(this::setContent);
        Optional.of(commentUpdateRequestDto.isWantAnonymous()).ifPresent(this::setWantAnonymous);
        this.setUpdatedAt(LocalDateTime.now());
    }

    public void addChildComment(Comment child) {
        this.children.add(child);
        child.setParent(this);
    }

    public String getWriterName() {
        return this.wantAnonymous ? this.nickname : this.writer.getName();
    }

    public boolean isOwner(Member member) {
        return this.writer.isSameMember(member);
    }

    public void validateAccessPermission(Member member) throws PermissionDeniedException {
        if (!isOwner(member) && !member.isAdminRole()) {
            throw new PermissionDeniedException("해당 댓글을 수정/삭제할 권한이 없습니다.");
        }
    }

    public void incrementLikes() {
        this.likes++;
    }

    public void decrementLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }

}