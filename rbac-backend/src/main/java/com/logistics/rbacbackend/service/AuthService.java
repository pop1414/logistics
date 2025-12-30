package com.logistics.rbacbackend.service;

import com.logistics.rbacbackend.dto.LoginDTO;
import com.logistics.rbacbackend.vo.LoginVO;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-22:23
 * @description com.logistics.rbacbackend.service
 */
public interface AuthService {
    LoginVO login(LoginDTO dto);
}
