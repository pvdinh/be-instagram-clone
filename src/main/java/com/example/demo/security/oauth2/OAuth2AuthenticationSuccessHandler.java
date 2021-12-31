package com.example.demo.security.oauth2;

import com.example.demo.models.AuthProvider;
import com.example.demo.models.UserAccount;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.security.TokenProvider;
import com.example.demo.services.UserAccountService;
import com.example.demo.services.UserAccountSettingService;
import com.example.demo.utils.ConvertSHA1;
import com.example.demo.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserAccountSettingService userAccountSettingService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        // đăng nhập thành công , lưu thông tin user vào csdl
        CustomOauth2User customOauth2User = (CustomOauth2User) authentication.getPrincipal();
        UserAccount userAccount = new UserAccount(customOauth2User.getId()
                ,customOauth2User.getName()
                , ConvertSHA1.convertSHA1(customOauth2User.getId())
                ,customOauth2User.getEmail(),null
                , AuthProvider.facebook, new ArrayList<String>(Collections.singleton("ROLE_USER")));
        userAccountService.addUserAccount(userAccount);

        UserAccountSetting userAccountSetting = new UserAccountSetting(customOauth2User.getId()
                ,customOauth2User.getName(),"",0,0,0
                ,customOauth2User.getPicture()
                ,customOauth2User.getName(),"");
        userAccountSettingService.addUserAccountSetting(userAccountSetting);

        if (response.isCommitted()) {
            System.out.println("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String redirectUri = CookieUtils.getCook(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME);
        return redirectUri+"/"+tokenProvider.createToken(authentication);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}