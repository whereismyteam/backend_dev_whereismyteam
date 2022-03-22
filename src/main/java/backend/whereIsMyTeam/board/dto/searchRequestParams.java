package backend.whereIsMyTeam.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
public class searchRequestParams {

    @JsonProperty("tech_stacks")
    private final List<String> techStacks = new ArrayList<>();

//    public String getTechs() {
//        return techs;
//    }
}
