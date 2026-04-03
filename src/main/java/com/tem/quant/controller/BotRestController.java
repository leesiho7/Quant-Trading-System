package com.tem.quant.controller;

import com.tem.quant.entity.ApiConfig;
import com.tem.quant.repository.ApiConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bot")
public class BotRestController {

    private final ApiConfigRepository apiConfigRepository;

    // API 설정을 DB에 저장하는 엔드포인트
    @PostMapping("/connect")
    public ResponseEntity<?> connectBot(@RequestBody ApiConfig apiConfig) {
        try {
            // 1. 넘어온 데이터 저장
            apiConfig.setIsActive(true); // 연결 시 활성화 상태로 변경
            ApiConfig savedConfig = apiConfigRepository.save(apiConfig);
            
            // 2. 성공 응답 반환
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "봇이 성공적으로 연결되었습니다!",
                "data", savedConfig
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "message", "연결 실패: " + e.getMessage()
            ));
        }
    }
}