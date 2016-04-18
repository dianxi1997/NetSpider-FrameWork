package junit.test;

import cn.coselding.netspider.dao.MyUrlDao;
import cn.coselding.netspider.dao.impl.MyUrlDaoImpl;
import cn.coselding.netspider.domain.MyUrl;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by 宇强 on 2016/4/18 0018.
 */
public class DaoTest {

    @Test
    public void testAddUrl() throws SQLException {
        MyUrl url = new MyUrl();
        url.setReaded(0);
        url.setTime(new Date());
        url.setUrl("http://www.baidu.com");

        MyUrlDao urlDao =new MyUrlDaoImpl();
        urlDao.save(url);
    }
}
