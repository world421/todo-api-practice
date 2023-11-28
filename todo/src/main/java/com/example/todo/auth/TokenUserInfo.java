package com.example.todo.auth;

import com.example.todo.userapi.entity.Role;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class TokenUserInfo {

    private String userId;
    private String email;
    private Role role;
}
