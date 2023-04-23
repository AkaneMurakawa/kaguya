package com.github.kaguya.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * I18解析器
 */
public class I18nLocaleResolver implements LocaleResolver {

    private static final String SESSION_LANGUAGE = "language";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute(SESSION_LANGUAGE);
        Locale locale = Locale.getDefault();
        if (StringUtils.isNotEmpty(lang)) {
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
