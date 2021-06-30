package com.example.demo.utils;

import com.example.demo.response.JwtResponse;
import com.example.demo.response.ResponseMessage;
import com.example.demo.response.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

public class TokenAuthentication {
    static final long EXPIRATIONTIME = 3600 * 1000 * 24 * 10; // 10 days
    static final String SECRET = "ThisIsASecret";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    public static void addAuthentication(HttpServletResponse response, String username) throws IOException {
        String jwt = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        addHeader(response);
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseObject(HttpStatus.OK.value(),new JwtResponse(jwt))));
        response.getWriter().flush();
    }

    public static void failAuthentication(HttpServletResponse response) throws IOException {
        addHeader(response);
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseMessage(HttpStatus.UNAUTHORIZED.value(),"Login fail.")));
        response.getWriter().flush();
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String username = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
            return username != null && isTokenExpired(token) ?
                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()) :
                    null;
        }
        return null;
    }

    public static Boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
        return new Date(System.currentTimeMillis()).getTime() <= claims.getExpiration().getTime();
    }

    public static void addHeader(HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        res.setHeader("Access-Control-Max-Age", "12000");
        res.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        res.setHeader("Access-Control-Expose-Headers", "*");
    }
}
