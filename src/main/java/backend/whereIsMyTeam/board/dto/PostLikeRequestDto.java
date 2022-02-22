package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.domain.Board;
import backend.whereIsMyTeam.board.domain.PostLike;
import backend.whereIsMyTeam.user.domain.User;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostLikeRequestDto {


    @NotNull(message = "유저 인덱스를 입력하시오.")
    Long userIdx;

    @NotNull(message = "게시글 인덱스를 입력하시오.")
    Long boardIdx;


    public PostLike toEntity(User user, Board board){
        return PostLike.builder()
                .user(user)
                .board(board)
                .build();

    }


}
