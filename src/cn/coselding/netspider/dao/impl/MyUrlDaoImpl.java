package cn.coselding.netspider.dao.impl;

import cn.coselding.netspider.dao.JdbcUtils;
import cn.coselding.netspider.domain.MyUrl;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 宇强 on 2016/4/18 0018.
 */
public class MyUrlDaoImpl implements cn.coselding.netspider.dao.MyUrlDao {


    /**增
     * @param url
     * @throws SQLException
     */
    @Override
    public void save(MyUrl url) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into MyUrl(url,time,readed) values(?,?,?);";
        Object[] params = {url.getUrl(),url.getTime(),url.getReaded()};
        runner.update(JdbcUtils.getConnection(), sql, params);
    }

    /**删
     * @param uid
     * @throws SQLException
     */
    @Override
    public void delete(int uid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "delete from MyUrl where uid=?;";
        runner.update(JdbcUtils.getConnection(), sql, uid);
    }

    /**改
     * @param url
     * @throws SQLException
     */
    @Override
    public void update(MyUrl url) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "update MyUrl set url=?,time=?,readed=? where uid=?;";
        Object[] params = {url.getUrl(),url.getTime(),url.getReaded(),url.getUid()};
        runner.update(JdbcUtils.getConnection(), sql, params);
    }

    /**改readed
     * @param url
     * @throws SQLException
     */
    @Override
    public void updateBySQL(String sql, Object[] params) throws SQLException {
        QueryRunner runner = new QueryRunner();
        runner.update(JdbcUtils.getConnection(), sql, params);
    }

    /**查询单个
     * @param uid
     * @return
     * @throws SQLException
     */
    @Override
    public MyUrl queryMyUrl(int uid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from MyUrl where uid=?;";
        MyUrl url = runner.query(JdbcUtils.getConnection(), sql, new BeanHandler<MyUrl>(MyUrl.class),uid);
        return  url;
    }

    /**分页查询多个
     * @param startindex
     * @param pagesize
     * @return
     * @throws SQLException
     */
    @Override
    public List<MyUrl> querypageData(int startindex, int pagesize) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from MyUrl limit ?,?;";
        List<MyUrl> urls = runner.query(JdbcUtils.getConnection(), sql, new BeanListHandler<MyUrl>(MyUrl.class),startindex,pagesize);
        return  urls;
    }

    /**查询总数
     * @return
     * @throws SQLException
     */
    @Override
    public long queryCount() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select count(*) from MyUrl;";
        return runner.query(JdbcUtils.getConnection(), sql, new ScalarHandler<Long>());
    }

    /**根据SQL语句自定义查询
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
    public List<MyUrl> queryBySQL(String sql, Object[] params) throws SQLException {
        QueryRunner runner = new QueryRunner();
        List<MyUrl> urls = runner.query(JdbcUtils.getConnection(), sql, new BeanListHandler<MyUrl>(MyUrl.class),params);
        return  urls;
    }
}
