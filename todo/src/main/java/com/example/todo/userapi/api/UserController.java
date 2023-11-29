package com.example.todo.userapi.api;

import com.example.todo.auth.TokenProvider;
import com.example.todo.auth.TokenUserInfo;
import com.example.todo.exception.NoRegisteredArgumentsException;
import com.example.todo.userapi.dto.request.LoginRequestDTO;
import com.example.todo.userapi.dto.request.UserRequestSignUpDTO;
import com.example.todo.userapi.dto.response.LoginResponseDTO;
import com.example.todo.userapi.dto.response.UserSingUpResponseDTO;
import com.example.todo.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin
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

    // 로그인 요청처리
    @PostMapping("/signin")
    public ResponseEntity<?> singIn(
            @Validated @RequestBody LoginRequestDTO dto
    ){
        try {
            LoginResponseDTO responseDTO = userService.authenticate(dto);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // 일반 회원을 프리미엄 회원으로 승격하는 요청 처리
    @PutMapping("/promote")
    // 권한 검사 ( 해당 권한이 아니라면 인가처리 거부  -> 403 코드리턴)
    // 메서드 호출 전에 권한 검사 -> 요청 당시 토큰에 있는 user 정보가
    // ROLE_COMMON 이라는 권한을 가지고 있는지 검사.
    @PreAuthorize("hasRole('ROLE_COMMON')")
    public ResponseEntity<?> promote(
            @AuthenticationPrincipal TokenUserInfo userInfo
            ){
        log.info("api/auth/promote PUT");

        try {
            LoginResponseDTO responseDTO =  userService.promoteToPremium(userInfo);
            return ResponseEntity.ok()
                    .body(responseDTO);
        } catch (NoRegisteredArgumentsException| IllegalArgumentException e) {
            // 예상 가능한 예외 (직접 생성하는예외처리)
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }catch (Exception e){
            // 예상하지 못한 예외처리
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }

}

