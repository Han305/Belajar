package com.rayhan.kantinku.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

public class CsrfAttributeToCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterchain)
            throws ServletException, IOException {

        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                .getName());
        if(csrf != null) {
            Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
            String token = csrf.getToken();
            if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                cookie = new Cookie("XSRFF-TOKEN", token);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        filterchain.doFilter(request, response);
    }
}

