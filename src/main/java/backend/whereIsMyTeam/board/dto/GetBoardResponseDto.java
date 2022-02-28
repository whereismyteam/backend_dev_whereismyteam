package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.domain.*;
import backend.whereIsMyTeam.board.repository.CommentRepository;
import backend.whereIsMyTeam.board.repository.PostLikeCustomRepository;
import backend.whereIsMyTeam.board.repository.PostLikeCustomRepositoryImpl;
import backend.whereIsMyTeam.board.service.PostLikeService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

    private List<Long> stackList= new ArrayList<>();; //모집하는 스택 기술들 리스트
    //private List<String> stacks; //모집하는 스택 기술들 리스트
    //private List<TechStack> stackList= new ArrayList<>();

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
    private List<postCommentDto> commentList= new ArrayList<>();; //댓글 부분 작성자 인덱스와 comment 인덱스도


    public GetBoardResponseDto(Board post,Long heart, String isHeart,List<postCommentDto> c){
        this.boardIdx=post.getBoardIdx();
        this.category=post.getCategory().getCategoryName();

        //this.stackList=post.getTechstacks().stream().toArray().;
        //this.stacks=new postStackDto(post.getTechstacks().getClass());
        //this.stackList=post.getTechstacks();
        /*for(int i=0;i< post.getTechstacks().size();++i){
            this.stacks.add(i, post.getTechstacks().get(i).getTechStack().getStackName());
        }*/

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

    public GetBoardResponseDto(Board post,Long heart,List<postCommentDto> c){
        this.boardIdx=post.getBoardIdx();
        this.category=post.getCategory().getCategoryName();
        //this.stackList=post.getTechstacks();
        this.title=post.getTitle();
        this.detail=new postDetailDto(post);
        this.postText=post.getContent();
        this.boardStatus=post.getBoardStatuses();
        this.writer= new postUserDto(post.getWriter()); //userImg 구현중
        this.createdAt=post.getCreateAt();
        this.watch=post.getCnt();
        this.heart=heart;
        this.isHeart="not exist";
        this.commentList=c;
    }


}
