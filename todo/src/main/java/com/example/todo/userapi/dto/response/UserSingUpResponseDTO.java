package com.example.todo.userapi.dto.response;

import com.example.todo.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "email")
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserSingUpResponseDTO {

    private String email;

    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate;
    public UserSingUpResponseDTO(User saved) {
        this.email = saved.getEmail();
        this.userName = saved.getUserName();
        this.joinDate = saved.getJoinDate();
    }
}
