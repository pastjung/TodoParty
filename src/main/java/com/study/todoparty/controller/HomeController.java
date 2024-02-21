package com.study.todoparty.controller;

import com.study.todoparty.config.jwt.JwtUtil;
import com.study.todoparty.dto.requestDto.LoginRequestDto;
import com.study.todoparty.dto.requestDto.SignupRequestDto;
import com.study.todoparty.dto.responseDto.CommonResponseDto;
import com.study.todoparty.entity.UserRoleEnum;
import com.study.todoparty.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // ResponseBody + Controller
@RequestMapping("/users")    // URL 줄이기
@RequiredArgsConstructor
public class HomeController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    // 회원가입 : 인가(Authorization)
    // POST : http://localhost:8080/users/signup
    /*
    {
        "username": "test12345",
        "password": "12345Abcde"
    }
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto){
        try {
            userService.signup(requestDto);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(new CommonResponseDto("중복된 username 입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new CommonResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
    }

    // 로그인 : 인증(Authentication)
    // GET : http://localhost:8080/users/login
    /*
    {
        "username": "test12345",
        "password": "12345Abcde"
    }
     */
    @GetMapping("/login")
    public ResponseEntity<CommonResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        try {
            // 로그인하는 유저의 권한
            UserRoleEnum userRole = userService.login(requestDto);

            // 토큰 생성
            String token = jwtUtil.createToken(requestDto.getUsername(), userRole);

            // 토큰 헤더 설정 및 반환
            return ResponseEntity.ok()
                    .header(jwtUtil.AUTHORIZATION_HEADER, token)
                    .body(new CommonResponseDto("로그인 성공", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}