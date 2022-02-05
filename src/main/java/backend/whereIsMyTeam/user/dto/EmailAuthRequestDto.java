package backend.whereIsMyTeam.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class EmailAuthRequestDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식을 맞춰주세요.")
    String email;
    @NotBlank(message = "토큰을 입력해주세요.")
    String authToken;
}
