package com.mytest.ssm.utils.mapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
	public T getEntity(ResultSet rs) throws SQLException;
}
