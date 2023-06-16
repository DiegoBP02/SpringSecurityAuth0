package com.example.demo.controller;

import com.example.demo.config.AuthConfig;
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

    @Value(value = "${com.auth0.managementDomain}")
    private String managementDomain;

    @GetMapping(value = "/users")
    @ResponseBody
    public ResponseEntity<String> users(HttpServletRequest request, HttpServletResponse response){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authController.getManagementApiToken());

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate
                .exchange("https://" + managementDomain + "/api/v2/users", HttpMethod.GET,
                        entity, String.class);
        return result;
    }

    @GetMapping(value = "/createUser")
    @ResponseBody
    public ResponseEntity<String> createUser(HttpServletResponse response){
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "randomEmail@email.com");
        requestBody.put("given_name", "Name");
        requestBody.put("family_name", "Family");
        requestBody.put("connection", "Username-Password-Authentication");
        requestBody.put("password", "Pa33w0rd");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " +authController.getManagementApiToken());

        HttpEntity<String> request = new HttpEntity<String>(requestBody.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.postForEntity(authConfig.getUsersUrl(), request, String.class);

        return result;
    }
}
