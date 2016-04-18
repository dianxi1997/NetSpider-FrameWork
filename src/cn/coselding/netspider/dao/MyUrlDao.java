package cn.coselding.netspider.dao;

import cn.coselding.netspider.domain.MyUrl;

import java.sql.SQLException;
import java.util.List;

/**MyUrl数据库Dao接口
 * Created by 宇强 on 2016/4/18 0018.
 */
public interface MyUrlDao {
    void save(MyUrl url) throws SQLException;

    void delete(int uid) throws SQLException;

    void update(MyUrl url) throws SQLException;

    void updateBySQL(String sql, Object[] params) throws SQLException;

    MyUrl queryMyUrl(int uid) throws SQLException;

    List<MyUrl> querypageData(int startindex, int pagesize) throws SQLException;

    long queryCount() throws SQLException;

    List<MyUrl> queryBySQL(String sql, Object[] params) throws SQLException;
}
