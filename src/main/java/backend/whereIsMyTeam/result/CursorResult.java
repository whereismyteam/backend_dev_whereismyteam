package backend.whereIsMyTeam.result;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CursorResult<T> extends Result {
    private List<T> values;
    private Boolean hasNext;
    private T data;

    public CursorResult(List<T> values, Boolean hasNext) {
        this.values = values;
        this.hasNext = hasNext;
    }

    public void setValues(List<T> values){
        this.values = values;
    }

    public Boolean getHasNext(){
        return hasNext;
    }
    public void setHasNext(Boolean hasNext){
        this.hasNext = hasNext;
    }
}
