package com.example.finalproject.finalproject.app.rebate.controller;

import com.example.finalproject.finalproject.app.rebate.service.RebateService;
import com.example.finalproject.finalproject.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
public class AdmRebateController {
    private final RebateService rebateService;

    @GetMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMakeData() {
        return "adm/rebate/makeData";
    }

    @PostMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public String makeData(String yearMonth) {
        rebateService.makeDate(yearMonth);

        return "redirect:/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth + "&msg=" + Ut.url.encode("정산데이터가 성공적으로 생성되었습니다.");
    }

    @GetMapping("/rebateOrderItemList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRebateOrderItemList(String yearMonth) {
        if (yearMonth == null) {
            yearMonth = "2022-10";
        }

        return "adm/rebate/rebateOrderItemList";
    }
}

