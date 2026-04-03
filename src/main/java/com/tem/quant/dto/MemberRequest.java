package com.tem.quant.dto;

import lombok.Data;

public class MemberRequest {
    
    @Data
    public static class Signup {
        private String email;
        private String nickname;
        private String password;
    }

    @Data
    public static class Login {
        private String email;
        private String password;
    }
}