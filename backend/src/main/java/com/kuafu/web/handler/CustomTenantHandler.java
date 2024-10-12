package com.kuafu.web.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomTenantHandler implements TenantLineHandler {

    @Override
    public Expression getTenantId() {
        // 假设有一个租户上下文，能够从中获取当前用户的租户
        Integer tenantId = TenantContextHolder.getCurrentTenantId();
        // 返回租户ID的表达式，LongValue 是 JSQLParser 中表示 bigint 类型的 class
        return new LongValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return "xxxxx";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 创建一个包含需要忽略的表名的集合
        Set<String> ignoreTables = new HashSet<>();          // 检查当前表名是否在忽略列表中
         ignoreTables.add("xxxxx");
        // 根据需要返回是否忽略该表
        return ignoreTables.contains(tableName);
    }

}