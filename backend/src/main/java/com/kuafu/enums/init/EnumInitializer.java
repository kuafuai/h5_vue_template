package com.kuafu.enums.init;

import com.kuafu.common.annotation.EnumMarker;
import com.kuafu.enums.manger.EnumManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
@Slf4j
public class EnumInitializer implements CommandLineRunner {

    @Autowired
    private EnumManager enumManager;


    @Override
    public void run(String... args) throws Exception {
        log.info("开始注册枚举");
        // 指定要扫描的包
        String basePackage = "com.kuafu.enums.domain";


        // 创建扫描器
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(EnumMarker.class));

        // 扫描指定包
        Set<BeanDefinition> beans = scanner.findCandidateComponents(basePackage);

        // 注册枚举
        for (BeanDefinition beanDefinition : beans) {
            try {
                Class<?> enumClass = Class.forName(beanDefinition.getBeanClassName());
                if (Enum.class.isAssignableFrom(enumClass)) {
                    enumManager.registerEnum((Class<? extends Enum<?>>) enumClass);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

