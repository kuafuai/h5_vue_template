package com.kuafu.enums.handler;

import com.kuafu.enums.domain.ValueEnums;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ValueEnumsTypeHandler 是一个自定义的枚举类型处理器，适用于继承了 ValueEnums 接口的枚举类型。
 * 它能够将数据库中的整数值与枚举对象进行双向转换，便于 MyBatis Plus 自动映射和操作。
 *
 * @param <E> 泛型参数，表示继承了 ValueEnums 接口的枚举类型
 */
public class ValueEnumsTypeHandler<E extends Enum<E> & ValueEnums> extends BaseTypeHandler<E> {
    private final Class<E> type;

    /**
     * 构造方法，用于传入具体的枚举类型。
     *
     * @param type 枚举类型的 Class 对象
     * @throws IllegalArgumentException 如果 type 参数为 null，抛出 IllegalArgumentException 异常
     */
    public ValueEnumsTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    /**
     * 设置 PreparedStatement 中的非空参数，将枚举类型的值转换为对应的整数存储到数据库。
     *
     * @param ps        PreparedStatement 对象
     * @param i         参数索引
     * @param parameter 枚举类型的参数
     * @param jdbcType  JDBC 类型（可能为 null）
     * @throws SQLException 当数据库访问错误时抛出 SQLException
     */
    @SneakyThrows
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {

        ValueEnums v =  parameter;
        final String value1 = v.getValue();
        ps.setString(i, value1);
//        Method getValueMethod = parameter.getClass().getMethod("getValue"); // 类加载器不一致，使用反射获取
//        String value = (String) getValueMethod.invoke(parameter);
//        ps.setString(i, value);
    }

    /**
     * 从 ResultSet 中获取列值并将其转换为枚举类型（通过列名）。
     *
     * @param rs         ResultSet 对象
     * @param columnName 列名
     * @return 与列值对应的枚举实例，若无匹配则返回 null
     * @throws SQLException 当数据库访问错误时抛出 SQLException
     */
    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        final String string = rs.getString(columnName);
        return getEnum(string);
    }

    /**
     * 从 ResultSet 中获取列值并将其转换为枚举类型（通过列索引）。
     *
     * @param rs          ResultSet 对象
     * @param columnIndex 列索引
     * @return 与列值对应的枚举实例，若无匹配则返回 null
     * @throws SQLException 当数据库访问错误时抛出 SQLException
     */
    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return getEnum(value);
    }

    /**
     * 从 CallableStatement 中获取参数值并将其转换为枚举类型。
     *
     * @param cs          CallableStatement 对象
     * @param columnIndex 列索引
     * @return 与列值对应的枚举实例，若无匹配则返回 null
     * @throws SQLException 当数据库访问错误时抛出 SQLException
     */
    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return getEnum(value);
    }

    /**
     * 根据整数值获取对应的枚举实例。
     *
     * @param value 整数值
     * @return 与整数值对应的枚举实例，若无匹配则返回 null
     */
    private E getEnum(String value) {

        for (E enumConstant : type.getEnumConstants()) {
            if (enumConstant.getValue().equals(value)) {
                return enumConstant;
            }
        }
        return null;
    }
}

