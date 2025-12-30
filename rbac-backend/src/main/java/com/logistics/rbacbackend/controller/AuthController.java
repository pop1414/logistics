package com.logistics.rbacbackend.controller;

import com.logistics.rbacbackend.common.ApiResponse;
import com.logistics.rbacbackend.dto.LoginDTO;
import com.logistics.rbacbackend.service.AuthService;
import com.logistics.rbacbackend.vo.LoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-22:25
 * @description com.logistics.rbacbackend.controller
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@RequestBody LoginDTO dto) {
        return ApiResponse.ok(authService.login(dto));
    }
}
