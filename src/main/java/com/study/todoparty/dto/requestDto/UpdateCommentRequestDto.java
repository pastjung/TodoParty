package com.study.todoparty.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentRequestDto {
    private Long id;
    private String content;
}
