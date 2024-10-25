package com.kuafu.web.service;

import com.kuafu.MisAppBackendApplication;
import com.kuafu.common.qywx.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

//@SpringBootTest(classes = {MisAppBackendApplication.class})
//@RunWith(SpringRunner.class)
@Slf4j
public class QyWxTest {

    @Test
    public void test_token() {
        String corpId = "ww72adcae86ddaa0f3";
        String secret = "HpDqe_v9AL52fPyGpZIqQq2MWsfGQmAJ9OKaiGnUOg8";

        QyWxAppClient client = new QyWxAppClient();
        QyWxAppAccessTokenResponse response = client.getAccessToken(QyWxAppAccessTokenRequest.builder()
                .corpId(corpId)
                .corpSecret(secret)
                .build());
        log.info("{}", response);
    }

    @Test
    public void test_department() {
        QyWxAppClient client = new QyWxAppClient();

        List<QyWxAppDepartment> departmentList = client.getDepartments(QyWxAppDepartmentRequest.builder()
                .accessToken("Gx7DPkd8cXUfBkFPoduAoog5Ke5dCwL7gy20CswAv219JeQXWP9Yc0IGJxuaYhf4dfeSue0-_B1DOiTM6P9k0wkUUgbSIL_ncUqkV7_7Pu0-mHdk7XcoouTScBj0v7EkDzCPq9GOdF2bMGtqMHtE0TrFw9z16A08eMOjQj-fstpanyIqw5whMu6UnMduGLn_svn39gbY1TS0jzpC1bhMrA")
                .build());

        log.info("{}", departmentList);
    }

    @Test
    public void test_user() {
        QyWxAppClient client = new QyWxAppClient();

        List<QyWxAppUser> users = client.getUsers(QyWxAppUserRequest.builder()
                .accessToken("Gx7DPkd8cXUfBkFPoduAoog5Ke5dCwL7gy20CswAv219JeQXWP9Yc0IGJxuaYhf4dfeSue0-_B1DOiTM6P9k0wkUUgbSIL_ncUqkV7_7Pu0-mHdk7XcoouTScBj0v7EkDzCPq9GOdF2bMGtqMHtE0TrFw9z16A08eMOjQj-fstpanyIqw5whMu6UnMduGLn_svn39gbY1TS0jzpC1bhMrA")
                .departmentId(1)
                .build());

        log.info("{}", users);
    }

    @Test
    public void send_message() {
        //JiangFeiZhiHangHeYi
        QyWxAppClient client = new QyWxAppClient();

        QyWxAppMessageTextCardRequest request = new QyWxAppMessageTextCardRequest();
        request.setAccessToken("Gx7DPkd8cXUfBkFPoduAoog5Ke5dCwL7gy20CswAv219JeQXWP9Yc0IGJxuaYhf4dfeSue0-_B1DOiTM6P9k0wkUUgbSIL_ncUqkV7_7Pu0-mHdk7XcoouTScBj0v7EkDzCPq9GOdF2bMGtqMHtE0TrFw9z16A08eMOjQj-fstpanyIqw5whMu6UnMduGLn_svn39gbY1TS0jzpC1bhMrA");
        request.setTouser("JiangFeiZhiHangHeYi");
        request.setAgentid(1000017);
        request.setMsgtype("textcard");
        request.setTextcard(TextCard.builder()
                .description("描述111")
                .title("任务1111")
                .url("https://www.baidu.com")
                .build());

        client.sendMessage(request);
    }

    @Test
    public void test_code() {
        QyWxAppClient client = new QyWxAppClient();
        QyWxAppCode2UserResponse response = client.getUserByCode(QyWxAppCode2UserRequest.builder()
                .accessToken("Gx7DPkd8cXUfBkFPoduAoog5Ke5dCwL7gy20CswAv219JeQXWP9Yc0IGJxuaYhf4dfeSue0-_B1DOiTM6P9k0wkUUgbSIL_ncUqkV7_7Pu0-mHdk7XcoouTScBj0v7EkDzCPq9GOdF2bMGtqMHtE0TrFw9z16A08eMOjQj-fstpanyIqw5whMu6UnMduGLn_svn39gbY1TS0jzpC1bhMrA")
                .code("bBJKIYejFb7DQg63skj9qSYk1ul1sd31rmSOgWPmbv0")
                .build());

        log.info("{}", response);
    }
}
