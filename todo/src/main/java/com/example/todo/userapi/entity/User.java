package com.example.todo.userapi.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.naming.Name;
import javax.persistence.*;
import java.time.LocalDateTime;

import static com.example.todo.userapi.entity.Role.COMMON;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id; // 계정명이 아니라 식별 코드
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @CreationTimestamp
    private LocalDateTime joinDate;

    @Enumerated(EnumType.STRING)
    //@ColumnDefault("'COMMON'")// insert 시 기본값
    @Builder.Default
    private Role role = Role.COMMON;// 유저 권한

    //등급 수정 메서드
    public void changeRole(Role role){
        this.role = role;
    }
}
