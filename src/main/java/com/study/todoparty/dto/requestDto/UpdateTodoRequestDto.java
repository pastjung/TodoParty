package com.study.todoparty.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTodoRequestDto {
    private Long id;
    private String title;
    private String content;
}
