package com.kava.kbpd.common.database.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StringArrayTypeHandler extends BaseTypeHandler<String[]> {

    /**
     * 设置参数：Java String[] → 数据库字符串（用逗号连接）
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < parameter.length; j++) {
            if (j > 0) sb.append(",");
            sb.append(parameter[j]);
        }
        ps.setString(i, sb.toString());
    }

    /**
     * 从结果集中通过列名获取值：数据库字符串 → Java String[]
     */
    @Override
    public String[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        return convert(str);
    }

    /**
     * 从结果集中通过索引获取值
     */
    @Override
    public String[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        return convert(str);
    }

    /**
     * 从 CallableStatement 获取值
     */
    @Override
    public String[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        return convert(str);
    }

    /**
     * 转换逻辑：字符串转数组（去除空格）
     */
    private String[] convert(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new String[0];
        }
        // 拆分并去除每个元素前后空格
        return Arrays.stream(str.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .toArray(String[]::new);
    }
}