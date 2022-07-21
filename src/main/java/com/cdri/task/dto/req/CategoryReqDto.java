package com.cdri.task.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryReqDto {
    @NotNull(message = "최소 하나 이상의 카테고리가 필요합니다.")
    List<String> categoryList;
}
