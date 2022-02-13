package backend.whereIsMyTeam.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserLogoutRequestDto {

    @NotNull(message = "유저 인덱스를 입력해주세요.")
    private long userIdx;
}
