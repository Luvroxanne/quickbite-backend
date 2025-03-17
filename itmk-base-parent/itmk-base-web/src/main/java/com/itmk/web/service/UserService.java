package com.itmk.web.service;

import com.itmk.dto.LoginRequestDTO;
import com.itmk.dto.LoginResult;

public interface UserService {
    LoginResult login(LoginRequestDTO loginDTO);
} 