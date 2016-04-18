package junit.test;

import cn.coselding.netspider.domain.MyUrl;
import cn.coselding.netspider.service.MyUrlServiceImpl;
import cn.coselding.netspider.spider.SpiderHandler;
import cn.coselding.netspider.spider.SpiderService;
import org.junit.Test;

import java.io.IOException;

/**爬虫单元测试类
 * Created by 宇强 on 2016/4/18 0018.
 */
public class SpiderTest {

    @Test
    public void testSpider() throws IOException {
        MyUrlServiceImpl service = new MyUrlServiceImpl();
        MyUrl myUrl = service.getLastMyUrl();
        String maxCount = "1000";
        SpiderService spiderService = new SpiderService(service,myUrl,maxCount);

        try {
            //对该网页数据进行相关的提取操作
            Class clazz = Class.forName("cn.coselding.netspider.example.EmailHandler");
            SpiderHandler handler = (SpiderHandler) clazz.newInstance();

            spiderService.execute(handler);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
