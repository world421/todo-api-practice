package com.example.todo.userapi.service;

import antlr.Token;
import com.example.todo.auth.TokenProvider;
import com.example.todo.userapi.dto.request.LoginRequestDTO;
import com.example.todo.userapi.dto.request.UserRequestSignUpDTO;
import com.example.todo.userapi.dto.response.UserSingUpResponseDTO;
import com.example.todo.userapi.entity.User;
import com.example.todo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 회원 가입 처리
    public UserSingUpResponseDTO create(final UserRequestSignUpDTO dto){

        String email = dto.getEmail();

        if(isDuplicate(email)){// email 존재 하는지 중복검사 !
            log.warn("이메일이 중복되었습니다 - {}",email);
            throw new RuntimeException("중복된 이메일 입니다.");
        }

        // 패스워드 인코딩
        String encoded = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encoded);

        // dto 를 유저 엔터디로 변환해서 저장

        User saved = userRepository.save(dto.toEntity());
        log.info("회원 가입 정상 수행됨 -saved user {}", saved);

        return new UserSingUpResponseDTO(saved);



    }


    public boolean isDuplicate(String email) {
        return userRepository.existsByEmail(email); // 이
    }
    //회원 인증
    public void authenticate(final LoginRequestDTO dto ){

        // 이메일을 통해 회원 정보 조회
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(
                        () -> new RuntimeException("가입된 회원이 아닙니다")
                );
        // 패스워드 검증
        String rawPassword = dto.getPassword(); // 입력한 비번
        String encodedPassword = user.getPassword(); // DB에 저장된 암호화된 비번
        if(!passwordEncoder.matches(rawPassword, encodedPassword)){
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
        log.info("{}님 로그인 성공", user.getUserName());

        // 로그인 성공 후에 클라이언트에게 뭘 리턴할 것인가  ? !
        // JWT를 클라이언트에게 발급해주어야한다.!




    }
}
