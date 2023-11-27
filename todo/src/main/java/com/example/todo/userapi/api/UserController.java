package com.example.todo.userapi.api;

import com.example.todo.userapi.dto.UserRequestSignUpDTO;
import com.example.todo.userapi.dto.UserSingUpResponseDTO;
import com.example.todo.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    // 이메일 중복 화인 요청 처리
    // GET : /api/auth/check? email=zzzz@xxx.com
    @GetMapping("/check")
    public ResponseEntity<?> check(String email){
        if(email.trim().isEmpty()){
            return ResponseEntity.badRequest().body("이메일이 없습니다");
        }
        boolean resultFlag = userService.isDuplicate(email);

        log.info("{} 중복 ?  = {} ", email, resultFlag);

        return  ResponseEntity.ok().body(resultFlag);

    }
    // 회원 가입 요청 처리
    // Post : /api/auth
    @PostMapping
    public ResponseEntity<?> signUp(
            @Validated @RequestBody UserRequestSignUpDTO dto,
            BindingResult result
    ){

        log.info("/api/auth/- {} ", dto);

        if(result.hasErrors()){
            log.warn(result.toString());
            return  ResponseEntity.badRequest()
                    .body(result.getFieldError());
        }
        try {
            UserSingUpResponseDTO responseDTO = userService.create(dto);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.info("이메일 중복 ! ");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}