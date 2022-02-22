package backend.whereIsMyTeam.board.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostLikeNumResponseDto {

    //private Long likeIdx;
    private Long postLikeNum;
    //private boolean check;

    @Builder
    public PostLikeNumResponseDto(long likeNum){
        //this.likeIdx=likeIdx;
        this.postLikeNum = likeNum;
        //this.check = check;

    }




}
