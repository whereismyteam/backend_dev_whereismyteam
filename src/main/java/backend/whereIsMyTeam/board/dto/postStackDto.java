package backend.whereIsMyTeam.board.dto;

import backend.whereIsMyTeam.board.NestedConvertHelper;
import backend.whereIsMyTeam.board.domain.Comment;
import backend.whereIsMyTeam.board.domain.TechStackBoard;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class postStackDto {
    private String stacks;

    public <E> postStackDto(String stackName, ArrayList<E> es) {
        this.stacks=stackName;
        es.add(stacks);
    }


    public static List<postStackDto> toDtoList(List<TechStackBoard> techStackBoards) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                techStackBoards,
                c -> new postStackDto(c.getTechStack().getStackName(),new ArrayList<>());
        return helper.convert();
    }
}
