package xwv.database;

import java.sql.PreparedStatement;

public interface DataSourcePreparedExecutor<T> {
    T execute(PreparedStatement pre_stmt);
}