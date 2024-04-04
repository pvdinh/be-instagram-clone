package com.example.demo.utils;

import com.example.demo.models.UserAccount;
import com.example.demo.response.JwtResponse;
import com.example.demo.response.ResponseMessage;
import com.example.demo.response.ResponseObject;
import com.example.demo.services.UserAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenAuthentication {
    public static final long EXPIRATIONTIME = 3600 * 1000 * 24 * 10; // 10 days
    public static final String SECRET = "ThisIsASecret";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";

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

    @Autowired
    static UserAccountService userAccountService;

    public static Authentication getAuthentication(HttpServletRequest request) {
        if (userAccountService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            userAccountService = webApplicationContext.getBean(UserAccountService.class);
        }
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String username = getUsernameFromToken(token);
            UserAccount userAccount = userAccountService.findUserAccountByUsernameOrEmailOrPhoneNumberOrId(username);
            List<GrantedAuthority> grantedAuthorities = userAccount.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
            return username != null && isTokenExpired(token) ?
                    new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities) :
                    null;
        }
        return null;
    }

    public static String getUsernameFromToken(String token){
        String username = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();
        return username;
    }

    public static Boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
        return new Date(System.currentTimeMillis()).getTime() <= claims.getExpiration().getTime();
    }

    public static void addHeader(HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
//        res.setHeader("Access-Control-Allow-Origin", "https://fe-meta.herokuapp.com");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        res.setHeader("Access-Control-Max-Age", "12000");
        res.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        res.setHeader("Access-Control-Expose-Headers", "*");
    }
}
