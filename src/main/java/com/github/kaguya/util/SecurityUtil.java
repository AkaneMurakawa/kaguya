package com.github.kaguya.util;

import com.google.common.io.BaseEncoding;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.InputStream;

/**
 * 加密工具
 */
public class SecurityUtil {

    /**
     * -----------------------------------------MD5------------------------------------------------------------------
     */
    public static String md5Hex(InputStream data) throws Exception {
        return DigestUtils.md5Hex(data);
    }

    public static String md5Hex(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    public static String md5Hex(final String data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * ----------------------------------------SHA256----------------------------------------------------------------
     */
    public static String sha256Hex(InputStream data) throws Exception {
        return DigestUtils.sha256Hex(data);
    }

    public static String sha256Hex(byte[] data) throws Exception {
        return DigestUtils.sha256Hex(data);
    }

    public static String sha256Hex(final String data) {
        return DigestUtils.sha256Hex(data);
    }

    /**
     * ----------------------------------------BASE64----------------------------------------------------------------
     */
    public static String base64(byte[] bytes) {
        return BaseEncoding.base64().encode(bytes);
    }

    public static byte[] base64(String key) {
        return BaseEncoding.base64().decode(key);
    }

    public static String base64Str(String key) {
        return new String(BaseEncoding.base64().decode(key));
    }

}
