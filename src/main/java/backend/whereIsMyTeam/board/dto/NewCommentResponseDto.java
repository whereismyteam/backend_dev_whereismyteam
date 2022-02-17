package backend.whereIsMyTeam.board.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewCommentResponseDto {
   private Long commentIdx;

   @Builder
   public NewCommentResponseDto(Long commentIdx) {
      this.commentIdx = commentIdx;
   }
}
