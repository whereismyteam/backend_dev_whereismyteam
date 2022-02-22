package backend.whereIsMyTeam.board.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostLikeNumRequestDto {

    @NotNull(message = "게시글 인덱스를 입력하시오.")
    Long boardIdx;
}
