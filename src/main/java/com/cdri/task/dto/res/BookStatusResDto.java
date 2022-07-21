package com.cdri.task.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookStatusResDto {
    private Long bookId;
    private String status;
    private boolean isAvailable;
}
