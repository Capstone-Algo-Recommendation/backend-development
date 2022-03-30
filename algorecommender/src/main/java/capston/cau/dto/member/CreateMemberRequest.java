package capston.cau.dto.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateMemberRequest {
    @NotNull(message = "Please enter member id")
    private String name;
}
