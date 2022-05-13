package capston.cau.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends Result{
    private T data;
}
