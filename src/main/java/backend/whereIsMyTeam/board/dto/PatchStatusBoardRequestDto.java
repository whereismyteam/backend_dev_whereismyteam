package backend.whereIsMyTeam.board.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatchStatusBoardRequestDto {

    @NotNull(message = "유저 인덱스를 입력해주세요.")
    Long userIdx;

    @NotNull(message = "상태를 입력해주세요.")
    String status;
}
