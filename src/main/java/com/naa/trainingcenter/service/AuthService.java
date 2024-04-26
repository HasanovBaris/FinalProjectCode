package com.naa.trainingcenter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naa.trainingcenter.utils.CustomUserDetails;
import com.naa.trainingcenter.security.properties.SecurityProperties;
import com.naa.trainingcenter.utils.SecurityJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(SecurityProperties.BEARER_PREFIX)) {
            try {
                String username = SecurityJWT.getSubjectFromBearerToken(bearerToken);
                CustomUserDetails user = userService.loadUserByUsername(username);

                String requestUrl = request.getRequestURL().toString();
                String accessToken = SecurityJWT.makeAccessToken(user, requestUrl);
                String newRefreshToken = SecurityJWT.makeRefreshToken(user, requestUrl);

                final Map<String, String> tokenMap = SecurityJWT.getTokenMap(accessToken, newRefreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), tokenMap);

            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());

                final Map<String, String> error = Map.of("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
