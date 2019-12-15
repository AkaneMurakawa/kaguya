package com.github.kaguya.config;

import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class I18nLocaleResolver implements LocaleResolver {

    private static final String SESSION_LAN = "lang";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute(SESSION_LAN);
        Locale locale = Locale.getDefault();
        if (!StringUtils.isEmpty(lang)) {
            String[] split = lang.split("_");
            try {
                locale = new Locale(split[0], split[1]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
