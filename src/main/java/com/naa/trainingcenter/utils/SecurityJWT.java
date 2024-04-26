package com.naa.trainingcenter.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.naa.trainingcenter.security.properties.SecurityProperties;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;

public final class SecurityJWT {

    private SecurityJWT(){}

    public static Map<String, String> getTokenMap(String accessToken, String refreshToken) {
        final Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    public static String makeAccessToken(CustomUserDetails user, String requestUrl) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityProperties.ACCESS_TOKEN_EXPIRE_TIME))
                .withIssuer(requestUrl)
                .withClaim("roles", getRolesFromUser(user))
                .withClaim("fullName", user.getName()  + " " + user.getSurname())
                .withClaim("position", user.getPosition())
                .withClaim("id", user.getId())
                .sign(SecurityProperties.ALGORITHM);
    }

    public static String makeRefreshToken(UserDetails user, String requestUrl) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityProperties.REFRESH_TOKEN_EXPIRE_TIME))
                .withIssuer(requestUrl)
                .sign(SecurityProperties.ALGORITHM);
    }

    public static DecodedJWT getDecodedJwtFromBearerToken(String bearerToken) {
        String token = bearerToken.substring(SecurityProperties.BEARER_PREFIX_LEN);
        Algorithm algorithm = Algorithm.HMAC256(SecurityProperties.BASE64_SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public static String getSubjectFromBearerToken(String bearerToken) {
        DecodedJWT decodedJWT = getDecodedJwtFromBearerToken(bearerToken);
        return decodedJWT.getSubject();
    }

    private static List<String> getRolesFromUser(UserDetails user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
