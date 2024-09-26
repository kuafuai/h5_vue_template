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
                .accessToken("XRH8a-lUkshFdWoi9eujXzsUg6QdXJgJvwz-JvdNBnr-sw_ipzquuuvcfsjYEaiUoJ66qRlBE8Wd3Q4JxXPEOtlnwvO7Ymo8CPpX_FvIyUyfz-7Wzi4qrP4uujfQzk12aBDy9vr4VSFUvAXG5jaQlnp5_0QE2qIxjuzn0faeIBXEb6pNRDONPWBMuiQbVRgtAXUPS22Ai8Ydm7kwG_7TqQ")
                .build());

        log.info("{}", departmentList);
    }

    @Test
    public void test_user() {
        QyWxAppClient client = new QyWxAppClient();

        List<QyWxAppUser> users = client.getUsers(QyWxAppUserRequest.builder()
                .accessToken("XRH8a-lUkshFdWoi9eujXzsUg6QdXJgJvwz-JvdNBnr-sw_ipzquuuvcfsjYEaiUoJ66qRlBE8Wd3Q4JxXPEOtlnwvO7Ymo8CPpX_FvIyUyfz-7Wzi4qrP4uujfQzk12aBDy9vr4VSFUvAXG5jaQlnp5_0QE2qIxjuzn0faeIBXEb6pNRDONPWBMuiQbVRgtAXUPS22Ai8Ydm7kwG_7TqQ")
                .departmentId(1)
                .build());

        log.info("{}", users);
    }

    @Test
    public void test_code() {
        QyWxAppClient client = new QyWxAppClient();
        QyWxAppCode2UserResponse response = client.getUserByCode(QyWxAppCode2UserRequest.builder()
                .accessToken("XRH8a-lUkshFdWoi9eujXzsUg6QdXJgJvwz-JvdNBnr-sw_ipzquuuvcfsjYEaiUoJ66qRlBE8Wd3Q4JxXPEOtlnwvO7Ymo8CPpX_FvIyUyfz-7Wzi4qrP4uujfQzk12aBDy9vr4VSFUvAXG5jaQlnp5_0QE2qIxjuzn0faeIBXEb6pNRDONPWBMuiQbVRgtAXUPS22Ai8Ydm7kwG_7TqQ")
                .code("bBJKIYejFb7DQg63skj9qSYk1ul1sd31rmSOgWPmbv0")
                .build());

        log.info("{}", response);
    }
}
