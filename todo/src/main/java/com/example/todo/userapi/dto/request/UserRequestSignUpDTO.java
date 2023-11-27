package com.example.todo.userapi.dto.request;

import com.example.todo.userapi.entity.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "email")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestSignUpDTO {

    @NotBlank
    @Email // 이메일의 형태임을 검증해줌 ( @ 잘 들어갔는지 등)
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Size(min = 2, max = 10)
    private String userName;

    // dto 를 Entity 로 변경하는 매서드
    public User toEntity(){
        return User.builder()
                .email(this.email)
                .password(this.password)
                .userName(this.userName)
                .build();
    }
}
