package backend.whereIsMyTeam.user.dto;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class NewNickNameRequestDto {

    @NotBlank(message ="닉네임을 입력해주세요")
    String nickName;
}
