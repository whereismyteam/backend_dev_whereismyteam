package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.domain.Board;
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
public class MainBoardListResponseDto {


    /**
     * 메인에 띄워야 하는 것들.
     * 게시글 idx, 카테고리 idx,boardStatus(게시글 상태),
     * 찜 갯수, 댓글 갯수,찜의 여부(인증 회원일시),
     *
     **/


    private Long boardIdx;
    private Long categoryIdx; //프로젝트, 스터디, 대회 중 1
    private List<String> stackList= new ArrayList<>();
    private String title;
    private postDetailDto detail;
    //private String postText;
    private String boardStatus; //게시물 상태(삭제,임시저장,모집중,모집완료)
    private postUserDto writer; //게시물 작성자 정보
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private Long watch;
    private Long heart; //찜 총 수
    private Long totalComent; //댓글 갯수
    private String isHeart; //해당 유저가 찜 눌렀는지 안눌렀는지
    //private List<postCommentDto> commentList= new ArrayList<>();


    //회원일때
    public MainBoardListResponseDto(Board post,
                                    List<String> stacks,
                                    Long commentNum,
                                    Long heart,
                                    String isHeart){
        this.boardIdx=post.getBoardIdx();
        this.categoryIdx=post.getCategory().getIdx();
        //외래키로 연결이 안된다면 ->service 단으로 옮기자
        this.stackList= stacks;
        this.title=post.getTitle();
        this.detail=new postDetailDto(post);
        this.boardStatus=post.getBoardStatuses().get(0).getStatus();
        this.writer= new postUserDto(post.getWriter());
        this.createdAt=post.getCreateAt();
        this.watch=post.getCnt();
        this.totalComent = commentNum; //service에서 받아옴
        this.heart=heart;
        this.isHeart=isHeart;
    }

    public MainBoardListResponseDto(Board post,
                                    List<String> stacks,
                                    Long heart,
                                    Long commentNum){
        this.boardIdx=post.getBoardIdx();
        this.categoryIdx=post.getCategory().getIdx();
        this.stackList=stacks;
        this.title=post.getTitle();
        this.detail=new postDetailDto(post);
        this.boardStatus=post.getBoardStatuses().get(0).getStatus();
        this.writer= new postUserDto(post.getWriter());
        this.createdAt=post.getCreateAt();
        this.watch=post.getCnt();
        this.totalComent = commentNum; //service에서 받아옴
        this.heart=heart;
        this.isHeart="not exist";
    }



}
