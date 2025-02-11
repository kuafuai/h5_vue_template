package com.kuafu.enums.service;


import com.kuafu.enums.manger.EnumManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnumService {
    @Autowired
    private EnumManager enumManager;


    /**
     * 获取指定类型的enums 列表
     *
     * @param c
     * @param <E>
     * @return
     */
    public <E extends Enum<E>> List<E> getEnumsList(Class c) {
        return enumManager.getEnumValues(c);
    }


    /**
     * 获取指定类型的enums
     *
     * @param name
     * @param <E>
     * @return
     */
    public <E extends Enum<E>> List<E> getEnumsList(String name) {
        return enumManager.getEnumValues(name);
    }


    public <E extends Enum<E>> E getEnumsByValue(Class c, Object value) {
        return (E) enumManager.getEnumByValue(c, value);
    }

    public <E extends Enum<E>> E getEnumsByValue(String name, Object value) {
        return enumManager.getEnumByValue(name, value);
    }
}
