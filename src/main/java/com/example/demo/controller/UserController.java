package com.example.demo.controller;

import com.example.demo.config.AuthConfig;
import com.example.demo.service.ApiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    private AuthController authController;

    @Autowired
    private AuthConfig authConfig;

    @Autowired
    private ApiService apiService;

    @Value(value = "${com.auth0.managementDomain}")
    private String managementDomain;

    @GetMapping(value = "/users")
    @ResponseBody
    public ResponseEntity<String> users(HttpServletRequest request, HttpServletResponse response){
        return apiService.getCall(authConfig.getUsersUrl());
    }

    @GetMapping(value = "/createUser")
    @ResponseBody
    public ResponseEntity<String> createUser(HttpServletResponse response){
        JSONObject request = new JSONObject();
        request.put("email", "randomEmail@email.com");
        request.put("given_name", "Name");
        request.put("family_name", "Family");
        request.put("connection", "Username-Password-Authentication");
        request.put("password", "Pa33w0rd");

        return apiService.postCall(authConfig.getUsersUrl(), request.toString());
    }
}
