package com.tem.quant.repository;

import com.tem.quant.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    // 이메일로 사용자 찾기 (로그인 시 사용)
    Optional<Member> findByEmail(String email);

    // 이메일 중복 체크용
    boolean existsByEmail(String email);
    
    // 닉네임 중복 체크용
    boolean existsByNickname(String nickname);
}