package com.github.kaguya.config;

import com.github.kaguya.constant.Header;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * I18解析器
 */
public class I18nLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader(Header.LANGUAGE);
        if (StringUtils.isNotBlank(lang) && 4 == lang.length()) {
            return new Locale(lang.substring(0, 2), lang.substring(2, 4).toUpperCase());
        }
        return Locale.getDefault();
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
