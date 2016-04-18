package cn.coselding.netspider.spider;

/**爬虫数据提取业务处理类Handler
 * Created by 宇强 on 2016/4/18 0018.
 */
public abstract class SpiderHandler {

    public SpiderHandler() {
        init();
    }

    abstract public void init();

    abstract public void handle(String htmlContent);

    abstract public void destroy();
}
