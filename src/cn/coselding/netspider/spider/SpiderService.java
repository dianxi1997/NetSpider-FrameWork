package cn.coselding.netspider.spider;

import cn.coselding.netspider.domain.MyUrl;
import cn.coselding.netspider.service.MyUrlServiceImpl;
import cn.coselding.netspider.spider.http.OKHttp;
import cn.coselding.netspider.utils.ConfigUtil;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**爬虫类封装
 * Created by 宇强 on 2016/4/18 0018.
 */
public class SpiderService {

    //HTTP客户端
    private OKHttp okHttp;
    //当前爬到的网页html源码
    private String htmlContent;
    //操作所有等待URL队列
    private MyUrlServiceImpl service;
    //是否达到了URL队列添加上限
    private boolean add;

    //当前访问了多少页面了
    private int count;
    //设置的访问页面最大值，达到这个值之后访问的页面里面的URL不再进行提取
    private int max;

    //构造函数，初始化各个成员变量
    public SpiderService(MyUrlServiceImpl service, MyUrl myUrl, String maxCount) throws IOException {
        okHttp = new OKHttp();
        //获取网页请求内容
        htmlContent = okHttp.getString(myUrl.getUrl());
        this.service = service;
        add = true;
        count = 0;
        try {
            max = Integer.parseInt(maxCount);
        } catch (Exception e) {
            //系统默认最大访问页面数值
            max = Integer.parseInt(ConfigUtil.getProperty("maxCount"));
        }
    }


    /**爬虫执行操作
     * @param handler
     * @throws IOException
     */
    public void execute(SpiderHandler handler) throws IOException {
        do {
            //还没到达上限，添加URL列表
            if (add)
                addURLs(htmlContent);
            //处理网页内容数据提取
            handler.handle(htmlContent);
            //访问页面计数器+1
            count++;
            //访问页面数达到最大值，关闭URL添加开关
            if (count == max) {
                add = false;
            }
            //当前页面访问结束，提取下一个URL
            MyUrl myUrl = service.getLastMyUrl();
            //url为空说明数据库队列中的所有URL都访问完毕
            if (myUrl == null) break;
            while(true) {
                try {
                    //请求下一个URL，获取网页html代码
                    htmlContent = okHttp.getString(myUrl.getUrl());
                    break;
                } catch (Exception e) {
                    //出问题，跳过该页面
                    myUrl = service.getLastMyUrl();
                    continue;
                }
            }
        } while (true);
    }

    /**添加访问页面中得到的新的URL列表
     * @param htmlContent
     */
    public void addURLs(String htmlContent) {
        //获取网页中的其他超链接，Set可以避免列表中的重复网址
        Set<String> set = new HashSet<String>();
        Pattern pattern = Pattern
                .compile("((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+");
        Matcher matcher = pattern.matcher(htmlContent);
        //匹配网址
        while (matcher.find()) {
            String u = matcher.group();
            if (u.contains("\""))
                u = u.substring(0, u.indexOf("\""));
            if (u.contains("'"))
                u = u.substring(0, u.indexOf("'"));
            if (u.contains(")"))
                u = u.substring(0, u.indexOf(")"));
            if (u.contains(">"))
                u = u.substring(0, u.indexOf(">"));
            //System.out.println(u);
            set.add(u);
        }
        MyUrlServiceImpl service = new MyUrlServiceImpl();
        //获取的超链接放入数据库
        service.addUrls(set);
    }

}
