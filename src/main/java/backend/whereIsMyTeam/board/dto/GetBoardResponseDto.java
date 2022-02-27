package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.domain.*;
import backend.whereIsMyTeam.board.repository.CommentRepository;
import backend.whereIsMyTeam.board.repository.PostLikeCustomRepository;
import backend.whereIsMyTeam.board.repository.PostLikeCustomRepositoryImpl;
import backend.whereIsMyTeam.board.service.PostLikeService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetBoardResponseDto {


    PostLikeService postLikeService;
    CommentRepository commentRepository;
    PostLikeCustomRepositoryImpl postLikeRepository;

    private Long boardIdx;
    private String category; //프로젝트, 스터디, 대회 중 1
    List<TechStackBoard> stackList; //모집하는 스택 기술들 리스트
    private String title;
    private postDetailDto detail;
    private String postText;
    private List<BoardStatus> boardStatus; //게시물 상태(삭제,임시저장,모집중,모집완료)
    private postUserDto writer; //게시물 작성자 정보
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private Long watch;
    private Long heart; //찜 총 수
    private String isHeart; //해당 유저가 찜 눌렀는지 안눌렀는지
    List<postCommentDto> commentList; //댓글 부분 작성자 인덱스와 comment 인덱스도


    public GetBoardResponseDto(Board post){
        this.boardIdx=post.getBoardIdx();
        this.category=post.getCategory().getCategoryName();
        this.stackList=post.getTechstacks();
        this.title=post.getTitle();
        this.detail=new postDetailDto(post);
        this.postText=post.getContent();
        this.boardStatus=post.getBoardStatuses();
        this.writer= new postUserDto(post.getWriter()); //userImg 구현중
        this.createdAt=post.getCreateAt();
        this.watch=post.getCnt();
        //for debug
        System.out.println("1");
        this.heart=postLikeRepository.findPostLikeNum(boardIdx);
        //for debug
        System.out.println("2");
        this.isHeart=postLikeService.checkPushedLikeString(post.getWriter().getUserIdx(),post.getBoardIdx());
        //for debug
        System.out.println("3");
        this.commentList=postCommentDto.toDtoList(commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(post.getBoardIdx()));
    }

    public GetBoardResponseDto(int notUser,Board post){
        this.boardIdx=post.getBoardIdx();
        this.category=post.getCategory().getCategoryName();
        this.stackList=post.getTechstacks();
        this.title=post.getTitle();
        this.detail=new postDetailDto(post);
        this.postText=post.getContent();
        this.boardStatus=post.getBoardStatuses();
        this.writer= new postUserDto(post.getWriter()); //userImg 구현중
        this.createdAt=post.getCreateAt();
        this.watch=post.getCnt();
        //for debug
        System.out.println("1");
        this.heart=postLikeRepository.findPostLikeNum(post.getBoardIdx());
        //for debug
        System.out.println("2");
        //this.isHeart=postLikeCustomRepository.exist(post.getWriter().getUserIdx(),post.getBoardIdx()).isPresent();
        this.commentList=postCommentDto.toDtoList(commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(post.getBoardIdx()));
        //for debug
        System.out.println("3");
    }
}
