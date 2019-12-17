package com.github.kaguya.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;

/**
 * JSON工具，封装toJson()和fromJson()方法
 */
public class JsonUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";

    /**
     *----------------------------------------------------GSON----------------------------------------------------------
     */

    /**
     * 自定义Gson创建
     * <p>
     * 如果使用 Gson gson = new Gson()会使用默认配置
     * 在这里我们使用GsonBuilder自定义配置
     * <p>
     * serializeNulls：当字段为空或者null时候依然进行转换
     * setDateFormat: 日期的格式
     * registerTypeAdapter: 自定义某些对象序列和反序列化方式，自定义Adapter需实现JsonSerializer或者JsonDeserializer接口
     */
    public static Gson gsonBuilder() {
        GsonBuilder builder = new GsonBuilder()
                .serializeNulls()
                .setDateFormat(DATE_FORMAT);
        Gson gson = builder.create();
        return gson;
    }

    /**
     * 对象转换为json
     *
     * @param obj 需要转换的对象
     * @return
     */
    public static String objectToJson(Object obj) {
        if (null == obj) {
            return "";
        }
        Gson gson = gsonBuilder();
        return gson.toJson(obj);
    }

    /**
     * json转换为对象
     *
     * @param json  json字符串
     * @param clazz 需要转换的对象
     * @param <T>   返回的目标对象
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        T target;
        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {
            Gson gson = gsonBuilder();
            target = gson.fromJson(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("Convert Java Objects into JSON error: {0} to {1}", json, clazz), e);
        }
        return target;
    }

    /**
     * 添加泛型支持
     * DTO<T1, T2, ...>
     *
     * @param raw  原生类型，例如上面的：DTO
     * @param args 泛型的类型参数类型，例如上面的：T1，T2的类型。获取方式：new TypeToken<T1>(){}.getType()
     * @return
     */
    public static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    /**
     * json转换为对象，泛型转换
     */
    public static <T> T jsonToObject(String json, Type typeOfT) {
        T target;

        if (null == json) {
            return null;
        }
        try {
            Gson gson = gsonBuilder();
            target = gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("Convert JSON into Java Objects error: {0} to {1}", json, typeOfT), e);
        }
        return target;
    }

    /**
     *----------------------------------------------------FastJSON----------------------------------------------------------
     */


    /**
     * 对象转换为json
     *
     * @param obj 需要转换的对象
     * @return
     */
    public static String toJSONString(Object obj) {
        if (null == obj) {
            return "";
        }
        return JSON.toJSONString(obj);
    }

    /**
     * json转换为对象
     *
     * @param json  json字符串
     * @param clazz 需要转换的对象
     * @param <T>   返回的目标对象
     * @return
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        T target;
        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {
            target = JSON.parseObject(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("Convert Java Objects into JSON error: {0} to {1}", json, clazz), e);
        }
        return target;
    }

    /**
     * json转换为对象，泛型转换
     *
     * @param typeOfT 类型，例如：new com.alibaba.fastjson.TypeReference<DTO<Data, Msg>>(){}
     */
    public static <T> T parseObject(String json, Type typeOfT) {
        T target;

        if (null == json) {
            return null;
        }
        try {
            target = JSON.parseObject(json, typeOfT);
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("Convert JSON into Java Objects error: {0} to {1}", json, typeOfT), e);
        }
        return target;
    }
}

