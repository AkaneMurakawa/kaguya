package com.github.kaguya.filter;

import com.github.kaguya.config.SessionCookieContainer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "requestFilter", urlPatterns = {"/*"})
public class RequestFilter implements Filter {

    @Resource
    private SessionCookieContainer sessionCookieContainer;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            if (null == sessionCookieContainer.getUserSession(request)) {
                sessionCookieContainer.setSession(request);
            }
            filterChain.doFilter(request, response);
        }catch (Exception e){
            e.printStackTrace();
            log.error("filter set session failed", e);
        }
    }

    @Override
    public void destroy() {

    }
}
