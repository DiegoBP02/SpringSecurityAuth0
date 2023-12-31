package com.example.demo.controller;


import com.example.demo.config.AuthConfig;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Controller
public class AuthController {
    @Autowired
    private AuthConfig config;

    @Autowired
    private AuthenticationController authenticationController;

    @Value(value = "${com.auth0.managementDomain}")
    private String managementDomain;

    @Value(value = "${com.auth0.managementClientId}")
    private String managementClientId;

    @Value(value = "${com.auth0.managementClientSecret}")
    private String managementClientSecret;

    @GetMapping(value = "/login")
    protected void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String redirectUri = "http://localhost:8080/callback";
        String authorizeUrl = authenticationController.buildAuthorizeUrl(request, response, redirectUri)
                .withScope("openid email")
                .build();
        response.sendRedirect(authorizeUrl);
    }

    @GetMapping(value = "/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response)
            throws IdentityVerificationException, IOException {
        Tokens tokens = authenticationController.handle(request, response);

        DecodedJWT jwt = JWT.decode(tokens.getIdToken());
        TestingAuthenticationToken authToken2 =
                new TestingAuthenticationToken(jwt.getSubject(), jwt.getToken());
        authToken2.setAuthenticated(true);

        SecurityContextHolder.getContext().setAuthentication(authToken2);
        response.sendRedirect(config.getContextPath(request) + "/");
    }

    public String getManagementApiToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("client_id", managementClientId);
        requestBody.put("client_secret", managementClientSecret);
        requestBody.put("audience", "https://" + managementDomain + "/api/v2/");
        requestBody.put("grant_type", "client_credentials");

        HttpEntity<String> request = new HttpEntity<String>(requestBody.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        HashMap<?, ?> result = restTemplate
                .postForObject("https://" + managementDomain + "/oauth/token", request, HashMap.class);

        assert result != null;
        return (String) result.get("access_token");
    }
}
