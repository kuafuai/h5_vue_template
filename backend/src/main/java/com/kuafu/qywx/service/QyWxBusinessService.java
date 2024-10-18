package com.kuafu.qywx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kuafu.common.cache.Cache;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.common.qywx.*;
import com.kuafu.common.util.SpringUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.common.util.WrapperFactory;
import com.kuafu.common.wx.WxAppAccessTokenRequest;
import com.kuafu.common.wx.WxAppAccessTokenResponse;
import com.kuafu.qywx.config.QyWxConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class QyWxBusinessService {

    @Value("${qywx.corp_id}")
    private String corpId;

    @Value("${qywx.corp_secret}")
    private String corpSecret;

    @Value("${qywx.agent_id}")
    private Integer agentId;

    @Resource
    private Cache cache;
    private static final String KEY = "qywx:accesstoken";

    private QyWxAppClient client = new QyWxAppClient();

    public String getAccessToken() {
        String accessToken = cache.getCacheObject(KEY);
        if (StringUtils.isNotEmpty(accessToken)) {
            return accessToken;
        }

        QyWxAppAccessTokenRequest request = QyWxAppAccessTokenRequest.builder()
                .corpId(corpId)
                .corpSecret(corpSecret)
                .build();

        QyWxAppAccessTokenResponse response = client.getAccessToken(request);
        if (response.getErrcode() != null && response.getErrcode() != 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "token获取失败");
        }

        accessToken = response.getAccessToken();
        int expires_in = response.getExpiresIn();
        // 写入缓存中
        cache.setCacheObject(KEY, accessToken, expires_in, TimeUnit.SECONDS);

        return accessToken;
    }


    /**
     * 同步用户
     *
     * @return
     */
    public boolean syncUser() {
        IService userService = SpringUtils.getBean(QyWxConstant.USER_TABLE);
        if (userService == null) {
            return false;
        }

        IService loginService = SpringUtils.getBean(QyWxConstant.LOGIN_TABLE);
        if (loginService == null) {
            return false;
        }

        // 获取token
        String token = getAccessToken();
        // 获取所有部门
        List<QyWxAppDepartment> departmentList =
                client.getDepartments(QyWxAppDepartmentRequest.builder().accessToken(token).build());

        //获取部门下的用户
        Map<String, QyWxAppUser> allUserMap = Maps.newHashMap();

        departmentList.forEach(p -> {
            List<QyWxAppUser> departmentUsers = client.getUsers(QyWxAppUserRequest.builder()
                    .accessToken(token).departmentId(p.getId())
                    .build());
            departmentUsers.forEach(u -> {
                if (allUserMap.containsKey(u.getUserId())) {
                    QyWxAppUser user = allUserMap.get(u.getUserId());
                    user.setDepartmentName(user.getDepartmentName() + "," + p.getName());
                } else {
                    u.setDepartmentName(p.getName());
                    allUserMap.put(u.getUserId(), u);
                }
            });
        });


        for (Map.Entry<String, QyWxAppUser> entry : allUserMap.entrySet()) {
            String key = entry.getKey();
            QyWxAppUser user = entry.getValue();

            QueryWrapper<?> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(QyWxConstant.USER_COLUMN_USER_ID, key);
            long count = userService.count(queryWrapper);
            if (count == 0) {
                Object userEntityObject = createNewUser(QyWxConstant.USER_TABLE);
                setFieldValue(userEntityObject, StringUtils.dbStrToHumpLower(QyWxConstant.USER_COLUMN_USER_ID), user.getUserId());
                setFieldValue(userEntityObject, StringUtils.dbStrToHumpLower(QyWxConstant.USER_COLUMN_USER_NAME), user.getUserName());
                setFieldValue(userEntityObject, StringUtils.dbStrToHumpLower(QyWxConstant.USER_COLUMN_DEPARTMENT_NAME), user.getDepartmentName());
                setFieldValue(userEntityObject, StringUtils.dbStrToHumpLower(QyWxConstant.USER_COLUMN_OPEN_USER_ID), user.getOpenUserid());

                boolean flag = userService.save(userEntityObject);
                if (flag) {
                    Long userId = getId(userEntityObject);
                    if (userId != null) {

                        Object loginEntityObject = createNewUser(QyWxConstant.LOGIN_TABLE);
                        setFieldValue(loginEntityObject, StringUtils.dbStrToHumpLower(QyWxConstant.LOGIN_COLUMN_KEY), user.getUserId());
                        setFieldValue(loginEntityObject, "relevanceTable", QyWxConstant.USER_TABLE);
                        setFieldValue(loginEntityObject, "relevanceId", String.valueOf(userId));

                        loginService.save(loginEntityObject);
                    }
                }
            }
        }

        return true;
    }


    /**
     * 发送消息
     *
     * @param userId
     * @param title
     * @param description
     * @param url
     */
    public void sendTextCardMessage(String userId, String title, String description, String url) {
        // 获取token
        String token = getAccessToken();
        QyWxAppMessageTextCardRequest request = new QyWxAppMessageTextCardRequest();
        request.setAccessToken(token);
        request.setTouser(userId);
        request.setAgentid(agentId);
        request.setMsgtype("textcard");
        request.setTextcard(TextCard.builder()
                .description(description)
                .title(title)
                .url(url)
                .build());

        client.sendMessage(request);
    }


    public Long getId(Object obj) {
        if (obj != null) {
            Object id = WrapperFactory.getTableModelId(obj);
            if (id instanceof Integer) {
                return ((Integer) id).longValue();
            } else {
                return (Long) id;
            }
        } else {
            return null;
        }
    }


    private Object createNewUser(String table) {
        Class<?> clazz = SpringUtils.getType(table);
        String className = clazz.getName();
        if (StringUtils.containsAnyIgnoreCase(className, "$")) {
            // 说明是代理
            clazz = clazz.getSuperclass();
        }

        Type superClass = clazz.getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superClass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Type entityType = actualTypeArguments[1];

            try {
                Class<?> entityClazz = Class.forName(entityType.getTypeName());
                return entityClazz.getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true); // 设置可访问
            field.set(obj, value); // 设置字段值
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getValue(Object current, String fileName) {
        final Class<?> aClass = current.getClass();
        final Field[] declaredFields = aClass.getDeclaredFields();
        try {
            for (Field f : declaredFields) {
                f.setAccessible(true);
                final String name = f.getName();
                if (StringUtils.equalsIgnoreCase(name, fileName)) {
                    return f.get(current);
                }
            }
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "关联字段不存在");
        }
    }
}
