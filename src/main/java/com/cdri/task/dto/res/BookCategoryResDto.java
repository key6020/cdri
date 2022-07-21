package com.cdri.task.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class BookCategoryResDto {
    private Long bookId;
    private List<String> categoryList;
}
