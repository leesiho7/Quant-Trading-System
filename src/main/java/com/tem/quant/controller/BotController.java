package com.tem.quant.controller;

import com.tem.quant.entity.ApiConfig;
import com.tem.quant.repository.ApiConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor // 생성자 주입을 위해 꼭 필요합니다!
@RequestMapping("/bot")
public class BotController {

    private final ApiConfigRepository apiConfigRepository;

    // 1. 봇 연동 설정 페이지 화면 띄우기
    @GetMapping("/setup")
    public String showSetupPage(Model model) {
        model.addAttribute("apiConfig", new ApiConfig());
        return "strategy-tester"; // templates/strategy-tester.html을 찾아갑니다.
    }

    // 2. 전략 시뮬레이터 페이지 직접 열기
    @GetMapping("/strategy-tester")
    public String strategyTester() {
        return "strategy-tester"; // templates/strategy-tester.html을 열어줍니다.
    }

    // 3. API 설정 저장하기 (POST 요청)
    @PostMapping("/save-config")
    @ResponseBody // 성공/실패 문자열만 그대로 반환합니다.
    public String saveConfig(@ModelAttribute ApiConfig apiConfig) {
        try {
            apiConfigRepository.save(apiConfig);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}