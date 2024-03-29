package com.cdri.task.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class BookReqDto {
    @NotBlank(message = "저자 정보가 필요합니다.")
    private String writer;

    @NotBlank(message = "도서 제목이 필요합니다.")
    private String title;

    @NotNull(message = "최소 하나 이상의 카테고리가 필요합니다.")
    private List<String> categoryList;
}
