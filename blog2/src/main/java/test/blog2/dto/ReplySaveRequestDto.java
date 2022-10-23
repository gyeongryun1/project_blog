package test.blog2.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReplySaveRequestDto {

    int userId;
    private Long memberId;
    private Long boardId;
    @NotBlank
    private String content;
}
