package com.epam.rd.autotasks.springstatefulcalc.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.UUID;

@Controller
public class CalculationsController {
    private final RequestSupervisor requestSupervisor;

    public CalculationsController(RequestSupervisor requestSupervisor) {
        this.requestSupervisor = requestSupervisor;
    }

    private Cookie getCookie(String cookieCandidate) {
        if (cookieCandidate.isEmpty()) {
            cookieCandidate = UUID.randomUUID().toString();
        }
        Cookie cookie = new Cookie("serviceId", cookieCandidate);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    private String buildSetCookieHeader(Cookie cookie) {
        StringBuilder sb = new StringBuilder();
        sb.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        if (cookie.getMaxAge() > 0) {
            sb.append(" Max-Age=").append(cookie.getMaxAge()).append(";");
        }
        if (cookie.getPath() != null) {
            sb.append(" Path=").append(cookie.getPath()).append(";");
        }
        if (cookie.isHttpOnly()) {
            sb.append(" HttpOnly;");
        }
        return sb.toString();
    }

    @PutMapping("/calc/expression")
    public ResponseEntity<Void> setExpression(
            @RequestBody String expression,
            @CookieValue(value = "serviceId", defaultValue = "") String serviceId
    ) {
        Cookie cookie = getCookie(serviceId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, buildSetCookieHeader(cookie));
        return ResponseEntity
                .status(requestSupervisor.setExpression(expression, cookie.getValue()))
                .headers(headers)
                .build();
    }

    @PutMapping("/calc/{key}")
    public ResponseEntity<Void> setVariable(
            @PathVariable String key,
            @RequestBody String value,
            @CookieValue(value = "serviceId", defaultValue = "") String serviceId
    ) {
        Cookie cookie = getCookie(serviceId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, buildSetCookieHeader(cookie));
        return ResponseEntity
                .status(requestSupervisor.setVariable(key, value, cookie.getValue()))
                .headers(headers)
                .build();
    }

    @DeleteMapping("/calc/expression")
    public ResponseEntity<Void> deleteExpression(
            @CookieValue(value = "serviceId", defaultValue = "") String serviceId
    ) {
        Cookie cookie = getCookie(serviceId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, buildSetCookieHeader(cookie));
        requestSupervisor.deleteExpression(cookie.getValue());
        return ResponseEntity.noContent().headers(headers).build();
    }

    @DeleteMapping("/calc/{key}")
    public ResponseEntity<Void> deleteVariable(
            @PathVariable String key,
            @CookieValue(value = "serviceId", defaultValue = "") String serviceId
    ) {
        Cookie cookie = getCookie(serviceId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, buildSetCookieHeader(cookie));
        requestSupervisor.deleteVariable(key, cookie.getValue());
        return ResponseEntity.noContent().headers(headers).build();
    }

    @GetMapping("/calc/result")
    public ResponseEntity<Integer> getResult(
            @CookieValue(value = "serviceId", defaultValue = "") String serviceId
    ) {
        Cookie cookie = getCookie(serviceId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, buildSetCookieHeader(cookie));
        try {
            int result = requestSupervisor.getResult(cookie.getValue());
            return ResponseEntity.ok().headers(headers).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(409).headers(headers).build();
        }
    }
}