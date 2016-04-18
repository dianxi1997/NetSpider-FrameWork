package cn.coselding.netspider.spider;

import cn.coselding.netspider.dao.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**Handler的数据库操作工具类
 * Created by 宇强 on 2016/4/18 0018.
 */
public class DBHelper {

    /**修改操作
     * @param sql
     * @param params
     * @throws SQLException
     */
    public static void update(String sql,Object[] params) throws SQLException {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            QueryRunner runner = new QueryRunner();
            runner.update(JdbcUtils.getConnection(), sql, params);
        }catch (SQLException e){
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.release();
        }
    }

    /**查询操作
     * @param sql
     * @param params
     * @param clazz
     * @param <T>
     * @return
     * @throws SQLException
     */
    public static <T> List<T> query(String sql,Object[] params,Class<T> clazz) throws SQLException {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();
            //设置只读
            JdbcUtils.setReadOnly();

            QueryRunner runner = new QueryRunner();
            return  runner.query(JdbcUtils.getConnection(), sql, new BeanListHandler<T>(clazz), params);
        }catch (SQLException e){
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.release();
        }
    }
}
