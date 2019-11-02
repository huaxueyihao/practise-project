package com.mybatis.book.study.type;

import com.mybatis.book.study.model.Enabled;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EnabledTypeHandler implements TypeHandler<Enabled> {

    private final Map<Integer,Enabled> enabledMap = new HashMap<>();

    public EnabledTypeHandler() {
        for (Enabled value : Enabled.values()) {
            enabledMap.put(value.getValue(),value);
        }
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Enabled enabled, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i,enabled.getValue());
    }

    @Override
    public Enabled getResult(ResultSet resultSet, String columnName) throws SQLException {
        int value = resultSet.getInt(columnName);
        return enabledMap.get(value);
    }

    @Override
    public Enabled getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int value = resultSet.getInt(columnIndex);
        return enabledMap.get(value);
    }

    @Override
    public Enabled getResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int value = callableStatement.getInt(columnIndex);
        return enabledMap.get(value);
    }
}
