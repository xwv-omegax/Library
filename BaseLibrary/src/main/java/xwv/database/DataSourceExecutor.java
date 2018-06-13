package xwv.database;

import java.sql.PreparedStatement;
import java.sql.Statement;

public interface DataSourceExecutor<T> {
    T execute(Statement s);
}