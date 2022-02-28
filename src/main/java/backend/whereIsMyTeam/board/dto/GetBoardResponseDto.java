package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.domain.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetBoardResponseDto {

    private Long boardIdx;
    private String category; //프로젝트, 스터디, 대회 중 1
    private List<String> stackList= new ArrayList<>();
    private String title;
    private postDetailDto detail;
    private String postText;
    private List<BoardStatus> boardStatus=new ArrayList<>(); //게시물 상태(삭제,임시저장,모집중,모집완료)
    private postUserDto writer; //게시물 작성자 정보
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private Long watch;
    private Long heart; //찜 총 수
    private String isHeart; //해당 유저가 찜 눌렀는지 안눌렀는지
    private List<postCommentDto> commentList= new ArrayList<>();;


    public GetBoardResponseDto(Board post,List<String> stacks,Long heart, String isHeart,List<postCommentDto> c){
        this.boardIdx=post.getBoardIdx();
        this.category=post.getCategory().getCategoryName();
        //외래키로 연결이 안된다면 ->service 단으로 옮기자
        this.stackList= stacks;
        this.title=post.getTitle();
        this.detail=new postDetailDto(post);
        this.postText=post.getContent();
        this.boardStatus=post.getBoardStatuses();
        this.writer= new postUserDto(post.getWriter());
        this.createdAt=post.getCreateAt();
        this.watch=post.getCnt();
        this.heart=heart;
        this.isHeart=isHeart;
        this.commentList=c;
    }

    public GetBoardResponseDto(Board post,List<String> stacks,Long heart,List<postCommentDto> c){
        this.boardIdx=post.getBoardIdx();
        this.category=post.getCategory().getCategoryName();
        this.stackList=stacks;
        this.title=post.getTitle();
        this.detail=new postDetailDto(post);
        this.postText=post.getContent();
        this.boardStatus=post.getBoardStatuses();
        this.writer= new postUserDto(post.getWriter());
        this.createdAt=post.getCreateAt();
        this.watch=post.getCnt();
        this.heart=heart;
        this.isHeart="not exist";
        this.commentList=c;
    }


}
