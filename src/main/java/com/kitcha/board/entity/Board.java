package com.kitcha.board.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kitcha.board.dto.BoardDetail;
import com.kitcha.board.dto.BoardList;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table(name = "board")
@Data
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "nickname", columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    private String nickname;

    @Column(name = "board_title", nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    private String boardTitle;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    private String content;

    @Column(name = "hit_cnt", nullable = false)
    private int hitCnt = 0;

    @Column(name = "news_title", nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    private String newsTitle;

    @Column(name = "long_summary", nullable = false, length = 3000, columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    private String longSummary;

    @Column(name = "news_url", nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    private String newsUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_yn", nullable = false)
    private boolean deletedYn = false;

    @Column(name = "user_id")
    private Long userId;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "file_id") // file 테이블의 file_id를 FK로 참조
//    private File file;

    public Board(Long boardId, String nickname, String boardTitle, String content, String newsTitle, String longSummary, String newsUrl, Long userId) {
        this.boardId = boardId;
        this.nickname = nickname;
        this.boardTitle = boardTitle;
        this.content = content;
        this.newsTitle = newsTitle;
        this.longSummary = longSummary;
        this.newsUrl = newsUrl;
        this.userId = userId;
    }

    public void updateHitCnt() {
        hitCnt++;
    }

    public BoardDetail toDetail(boolean isOwner, boolean isAdmin) {
        return new BoardDetail(boardId, boardTitle, hitCnt, nickname, createdAt.toString(), content, longSummary, newsUrl, isOwner, isAdmin);
    }

    public BoardList toList() {
        return new BoardList(boardId, boardTitle, hitCnt, nickname, createdAt.toString());
    }

    public void update(String boardTitle, String content) {
        this.boardTitle = boardTitle;
        this.content = content;
    }
}
