package com.epam.rd.autotasks.springstatefulcalc.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class CalculationsController {
    private final RequestSupervisor requestSupervisor;

    public CalculationsController(RequestSupervisor requestSupervisor) {
        this.requestSupervisor = requestSupervisor;
    }

    @PutMapping("/calc/expression")
    public ResponseEntity<Void> setExpression(
            @RequestBody String expression,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(
                requestSupervisor.setExpression(expression, getCookie(request, response))
        ).build();
    }

    private String getCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if (cookie.getName().equals("serviceId")) {
                    return cookie.getValue();
                }
            }
        }
        String serviceId = UUID.randomUUID().toString();
        response.addCookie(new Cookie("serviceId", serviceId));
        return serviceId;
    };

    @PutMapping("/calc/{key}")
    public ResponseEntity<Void> setVariable(
            @PathVariable String key,
            @RequestBody String value,
            HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.status(
                requestSupervisor.setVariable(key, value, getCookie(request, response))
        ).build();
    }

    @DeleteMapping("/calc/expression")
    public ResponseEntity<Void> deleteExpression(HttpServletRequest request, HttpServletResponse response) {
        requestSupervisor.deleteExpression(getCookie(request, response));
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/calc/{key}")
    public ResponseEntity<Void> deleteVariable(@PathVariable String key, HttpServletRequest request, HttpServletResponse response) {
        requestSupervisor.deleteVariable(key, getCookie(request, response));
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/calc/result")
    public ResponseEntity<Integer> getResult(HttpServletRequest request, HttpServletResponse response) {
        try {
            int result = requestSupervisor.getResult(getCookie(request, response));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(409).build();
        }
    }
}