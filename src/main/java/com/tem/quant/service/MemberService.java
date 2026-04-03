package com.tem.quant.service;

import com.tem.quant.dto.MemberRequest;
import com.tem.quant.entity.Member;
import com.tem.quant.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    // 1. 회원가입 로직
    public void signup(MemberRequest.Signup request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        Member member = Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(request.getPassword()) // 주의: 실제 배포 시 BCrypt 암호화 필수!
                .role(Member.Role.USER)
                .build();
        
        memberRepository.save(member);
    }

    // 2. 로그인 검증 로직
    public Member login(String email, String password) {
        return memberRepository.findByEmail(email)
                .filter(m -> m.getPassword().equals(password)) // 비번 일치 확인
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }
}