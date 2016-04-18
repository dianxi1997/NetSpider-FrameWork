package cn.coselding.netspider.domain;

import java.util.Date;

/**网址类，对应MyUrl表中的实体
 * Created by 宇强 on 2016/4/18 0018.
 */
public class MyUrl {

    //网址
    private String url;
    //网址id
    private int uid;
    //网址入表时间
    private Date time;
    //网址是否已爬
    private int readed;


    @Override
    public String toString() {
        return "MyUrl{" +
                "url='" + url + '\'' +
                ", uid=" + uid +
                ", time=" + time +
                ", readed=" + readed +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }
}
