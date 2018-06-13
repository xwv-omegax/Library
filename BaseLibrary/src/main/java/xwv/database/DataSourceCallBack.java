package xwv.database;

import java.sql.ResultSet;

public interface DataSourceCallBack<T> {
    T callback(ResultSet rs);
}