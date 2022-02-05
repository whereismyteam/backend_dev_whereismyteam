package backend.whereIsMyTeam.result;

import backend.whereIsMyTeam.result.Result;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends Result {
    private T data;
}