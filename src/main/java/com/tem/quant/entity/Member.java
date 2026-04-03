package com.tem.quant.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;     // 로그인 아이디로 사용

    @Column(nullable = false)
    private String password;  // 암호화된 비밀번호 저장

    @Column(nullable = false)
    private String nickname;  // 서비스 활동명

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // 사용자의 권한 (일반 유저, 관리자 등) - 나중에 Security 적용 시 필요
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER, ADMIN
    }
}