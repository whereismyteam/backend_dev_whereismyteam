package backend.whereIsMyTeam.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogoutRequestDto {

    @NotBlank(message = "유저 인덱스를 입력해주세요.")
    private long userIdx;
    @NotBlank(message = "accessToken을 입력해주세요.")
    private String accessToken;
    @NotBlank(message = "refreshToken을 입력해주세요.")
    private String refreshToken;
}
