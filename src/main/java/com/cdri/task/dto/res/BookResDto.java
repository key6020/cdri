package com.cdri.task.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class BookResDto {
    private Long bookId;

    private String writer;

    private String title;

    private List<String> categoryList;
}
