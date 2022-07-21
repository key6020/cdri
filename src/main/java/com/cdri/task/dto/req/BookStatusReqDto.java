package com.cdri.task.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
public class BookStatusReqDto {
    @NotBlank(message = "상태를 입력해주세요. 정상 || 훼손 || 분실")
    private String status;
}
