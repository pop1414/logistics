package com.logistics.rbacbackend.controller;

import com.logistics.rbacbackend.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-13:53
 * @description com.logistics.rbacbackend.controller
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    // 1) 成功返回：看返回体是不是 {code:200,msg:"ok",data:{...}}
    @GetMapping("/ok")
    public ApiResponse<Map<String, Object>> ok() {
        return ApiResponse.ok(Map.of(
                "hello", "world",
                "n", 123
                                    ));
    }

    // 2) 故意抛异常：看能否被 GlobalExceptionHandler 捕获并返回统一 JSON
    @GetMapping("/error")
    public ApiResponse<Void> error() {
        throw new IllegalArgumentException("测试异常：RBAC统一返回体是否生效");
    }
}
