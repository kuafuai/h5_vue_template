package com.kuafu.enums.manger;

import com.kuafu.enums.domain.ValueEnums;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EnumManager {

    private final static Map<Class<?>, List<Enum<?>>> enumMap = new HashMap<>(); // 类与枚举类型的映射关系
    private final static Map<String, List<Enum<?>>> enumNameMap = new HashMap<>(); // 名称与枚举的映射管理

    public <E extends Enum<E>> void registerEnum(Class<? extends Enum<?>> enumClass) {
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
//        final Enum<?>[] enumConstants1 = enumClass.getEnumConstants();
        final List<Enum<?>> list = Arrays.asList(enumConstants);

        enumMap.put(enumClass, list);
        String enumName = enumClass.getSimpleName();
        enumName = Character.toLowerCase(enumName.charAt(0)) + enumName.substring(1);

        enumNameMap.put(enumName, list); // 首字母小写的数据
//        enumNameMap.put(enumName, enumValues);
    }

    public static <E extends Enum<E>> List<E> getEnumValues(Class<E> enumClass) {
        return (List<E>) enumMap.get(enumClass);
    }

    public static <E extends Enum<E>> List<E> getEnumValues(String enumName) {
        enumName = Character.toLowerCase(enumName.charAt(0)) + enumName.substring(1);
        return (List<E>) enumNameMap.get(enumName);
    }

    public <E extends Enum<E>> E getEnumByValue(Class<E> enumClass, Object value) {
        List<E> enumValues = getEnumValues(enumClass);
        if (enumValues != null) {
            for (E enumValue : enumValues) {
                if (enumValue instanceof ValueEnums) {
                    if (((ValueEnums) enumValue).getValue().equals(value)) {
                        return enumValue;
                    }
                }
            }
        }
        return null; // 或抛出异常
    }


    /**
     * 获取枚举的值
     *
     * @param enumName
     * @param value
     * @param <E>
     * @return
     */
    public <E extends Enum<E>> E getEnumByValue(String enumName, Object value) {
        List<E> enumValues = getEnumValues(enumName);
        if (enumValues != null) {
            for (E enumValue : enumValues) {
                if (enumValue instanceof ValueEnums) {
                    if (((ValueEnums) enumValue).getValue().equals(value)) {
                        return enumValue;
                    }
                }
            }
        }
        return null; // 或抛出异常
    }

    private String toLowerCamelCase(String className) {
        if (className == null || className.isEmpty()) {
            return className;
        }
        // 将首字母小写
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}

