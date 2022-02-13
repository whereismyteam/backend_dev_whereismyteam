package backend.whereIsMyTeam.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReIssueRequestDto {

    @NotNull(message = "유저 인덱스를 입력해주세요.")
    Long userIdx;

}
