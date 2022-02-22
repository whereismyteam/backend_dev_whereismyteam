package backend.whereIsMyTeam.board.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostLikeResponseDto {


    private Long likeIdx;
//    private Long postLikeNum;
//    private boolean check;

    @Builder
    public PostLikeResponseDto(Long postLikeIdx){
        this.likeIdx=postLikeIdx;

    }
}
