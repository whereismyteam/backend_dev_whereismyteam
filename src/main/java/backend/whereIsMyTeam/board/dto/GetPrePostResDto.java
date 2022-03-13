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
public class GetPrePostResDto {
    private Long boardIdx;
    private String category; //프로젝트, 스터디, 대회 중 1
    private List<String> stackList= new ArrayList<>();
    private String title;
    private postDetailDto detail;
    private String postText;
    private String boardStatus; //게시물 상태(임시저장)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public GetPrePostResDto(Board post, List<String> stacks){
        this.boardIdx=post.getBoardIdx();
        this.category=post.getCategory().getCategoryName();
        this.stackList= stacks;
        this.title=post.getTitle();
        this.detail=new postDetailDto(post);
        this.postText=post.getContent();
        this.boardStatus=post.getBoardStatuses().get(0).getStatus();
        this.createdAt=post.getCreateAt();
    }


}
