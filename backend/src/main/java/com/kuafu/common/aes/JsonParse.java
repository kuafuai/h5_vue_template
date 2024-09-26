package com.kuafu.common.aes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonParse {

    /**
     * 提取出 JSON 包中的加密消息
     *
     * @param jsontext 待提取的json字符串
     * @return 提取出的加密消息字符串
     * @throws AesException
     */
    public static Object[] extract(String jsontext) throws AesException {
        Object[] result = new Object[3];
        try {
            Gson gson = new GsonBuilder().create();
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();

            // 将 JSON 字符串转换为 Map
            Map<String, Object> map = gson.fromJson(jsontext, type);

            String encrypt_msg = String.valueOf(map.get("encrypt"));
            String tousername = String.valueOf(map.get("tousername"));
            String agentid = String.valueOf(map.get("agentid"));

            result[0] = tousername;
            result[1] = encrypt_msg;
            result[2] = agentid;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.ParseJsonError);
        }
    }

    /**
     * 生成json消息
     *
     * @param encrypt   加密后的消息密文
     * @param signature 安全签名
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @return 生成的json字符串
     */
    public static String generate(String encrypt, String signature, String timestamp, String nonce) {

        String format = "{\"encrypt\":\"%1$s\",\"msgsignature\":\"%2$s\",\"timestamp\":\"%3$s\",\"nonce\":\"%4$s\"}";
        return String.format(format, encrypt, signature, timestamp, nonce);

    }
}
