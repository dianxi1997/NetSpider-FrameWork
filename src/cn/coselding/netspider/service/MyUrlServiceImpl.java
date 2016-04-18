package cn.coselding.netspider.service;

import cn.coselding.netspider.dao.JdbcUtils;
import cn.coselding.netspider.dao.MyUrlDao;
import cn.coselding.netspider.dao.impl.MyUrlDaoImpl;
import cn.coselding.netspider.domain.MyUrl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**业务处理层
 * Created by 宇强 on 2016/4/18 0018.
 */
public class MyUrlServiceImpl {

    private MyUrlDao urlDao = new MyUrlDaoImpl();

    /**添加扫描得到的网页中的URL列表
     * @param urls
     */
    public void addUrls(Collection<String> urls) {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            if (urls != null && urls.size() > 0) {
                for (String u : urls) {
                    MyUrl url = new MyUrl();
                    url.setUrl(u);
                    url.setTime(new Date());
                    url.setReaded(0);
                    try {
                        urlDao.save(url);
                    } catch (SQLException e) {
                        //重复的网址不加入即可，不需要导致程序结束
                        if (e.getMessage().contains("Duplicate entry"))
                            continue;
                        else throw e;
                    }
                }
            }

            JdbcUtils.commit();
        } catch (SQLException e) {
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }

    /**获取当前数据库中的最新网址
     * @return
     */
    public MyUrl getLastMyUrl() {
        try {
            // 设置事务隔离级别
            JdbcUtils
                    .setTransactionIsolation(JdbcUtils.TRANSACTION_READ_COMMITTED);
            // 开启事务
            JdbcUtils.startTransaction();

            List<MyUrl> urls = urlDao.queryBySQL("select * from MyUrl where readed=0 order by time desc limit 0,1;", null);
            //已读网址删除
            if (urls != null && urls.size() > 0) {
                urlDao.updateBySQL("delete from MyUrl where url=?;", new Object[]{urls.get(0).getUrl()});
            }
            JdbcUtils.commit();
            return urls.size() > 0 ? urls.get(0) : null;
        } catch (SQLException e) {
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.release();
        }
    }
}
