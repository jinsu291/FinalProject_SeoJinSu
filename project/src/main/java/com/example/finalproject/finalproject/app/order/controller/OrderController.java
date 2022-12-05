package com.example.finalproject.finalproject.app.order.controller;

import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.member.service.MemberService;
import com.example.finalproject.finalproject.app.order.entity.Order;
import com.example.finalproject.finalproject.app.order.exception.ActorCanNotSeeOrderException;
import com.example.finalproject.finalproject.app.order.exception.OrderIdNotMatchedException;
import com.example.finalproject.finalproject.app.order.service.OrderService;
import com.example.finalproject.finalproject.app.security.dto.MemberContext;
import com.example.finalproject.finalproject.util.Ut;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final MemberService memberService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showDetail(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model) {
        Order order = orderService.findForPrintById(id).get();

        Member actor = memberContext.getMember();

        long restCash = memberService.getRestCash(actor);

        if (orderService.actorCanSee(actor, order) == false) {
            throw new ActorCanNotSeeOrderException();
        }

        model.addAttribute("order", order);
        model.addAttribute("actorRestCash", restCash);
        return "order/detail";
    }

    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    private final String SECRET_KEY = "test_sk_JQbgMGZzorz5oyRvgkLVl5E1em4d";

    @RequestMapping("/{id}/success")
    public String confirmPayment(
            @PathVariable long id,
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount,
            Model model
    ) throws Exception {

        Order order = orderService.findForPrintById(id).get();

        long orderIdInputed = Long.parseLong(orderId.split("__")[1]);

        if ( id != orderIdInputed ) {
            throw new OrderIdNotMatchedException();
        }

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(order.calculatePayPrice()));

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            orderService.payByTossPayments(order);

            return "redirect:/order/%d?msg=%s".formatted(order.getId(), Ut.url.encode("결제가 완료되었습니다."));
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/fail";
        }
    }

    @RequestMapping("/{id}/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "order/fail";
    }
}