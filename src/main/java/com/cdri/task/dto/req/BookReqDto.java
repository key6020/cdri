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
    @NotBlank
    private String writer;

    @NotBlank
    private String title;

    @NotNull
    private List<String> categoryList;
}
