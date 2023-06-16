package com.example.demo.service;

import com.example.demo.controller.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    @Autowired
    private AuthController authController;

    public ResponseEntity<String> getCall(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authController.getManagementApiToken());

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate
                .exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> postCall(String url, String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authController.getManagementApiToken());

        HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(url, request, String.class);
    }
}
