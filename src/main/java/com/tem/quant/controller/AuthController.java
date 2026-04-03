package com.tem.quant.controller;

import com.tem.quant.dto.MemberRequest;
import com.tem.quant.entity.Member;
import com.tem.quant.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    // 회원가입 처리
    @PostMapping("/register")
    public String register(@ModelAttribute MemberRequest.Signup request) {
        memberService.signup(request);
        return "redirect:/login"; // 가입 성공 시 로그인 페이지로
    }

    // 로그인 처리 (세션 설정)
    @PostMapping("/login")
    public String login(@ModelAttribute MemberRequest.Login request, HttpSession session) {
        try {
            Member member = memberService.login(request.getEmail(), request.getPassword());
            
            // 핵심: 세션에 로그인 유저 정보 저장
            session.setAttribute("loginMember", member);
            
            return "redirect:/index"; // 로그인 성공 시 홈으로
        } catch (Exception e) {
            return "redirect:/login?error"; // 실패 시 에러 파라미터 전달
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login";
    }
}