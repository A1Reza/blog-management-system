package com.blogmanagement.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebFilter("/api/posts/*")
public class AuthenticationFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();

        boolean requiresAuthentication =
                "POST".equals(method)
                        || "PUT".equals(method)
                        || "DELETE".equals(method);

        if (!requiresAuthentication) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getSession(false) == null
                || httpRequest.getSession(false).getAttribute("userId") == null) {

            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(
                    httpResponse.getOutputStream(),
                    Map.of("message", "Authentication required")
            );

            return;
        }

        chain.doFilter(request, response);
    }
}
