package com.example.todo.todoapi.dto.request;

import com.example.todo.todoapi.entity.Todo;
import com.example.todo.userapi.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class TodoCreateRequestDTO {

    @NotBlank
    @Size(min = 2, max = 30)
    private String title; // 요창post라 josn 형태로 넘어옴

    // dto를 엔터티로 변환
    public Todo toEntity(User user){
        return Todo.builder()
                .title(this.title)
                .user(user)
                .build();
    }

}
