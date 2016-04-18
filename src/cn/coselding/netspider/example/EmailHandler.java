package cn.coselding.netspider.example;

import cn.coselding.netspider.dao.JdbcUtils;
import cn.coselding.netspider.spider.DBHelper;
import cn.coselding.netspider.spider.SpiderHandler;

import java.beans.Statement;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**爬虫实现：爬网页的邮箱地址
 * Created by 宇强 on 2016/4/18 0018.
 */
public class EmailHandler extends SpiderHandler {

    //初始化，可以创建数据表
    @Override
    public void init() {
        /*
        try {
            List<String> names = DBHelper.query("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=? AND TABLE_NAME=?",new Object[]{"netspider","email"},String.class);
            if(names==null||names.size()<=0){
                String sql = "create table if not exists email(eid int primary key auto_increment,"
                        +"email varchar(255) unique,"
                        +"time datetime);";
                DBHelper.update(sql,new Object[]{});
                System.out.print("init...");
            }
        } catch (SQLException e) {
            JdbcUtils.rollback();
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.release();
        }
        */
    }

    //处理类，每次获取一个网页传到这里处理具体的业务请求
    @Override
    public void handle(String htmlContent) {
        String sql = "insert into email(email,time) values(?,?);";
        //System.out.print(htmlContent);
        Pattern pattern = Pattern
                .compile("\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}");
        Matcher matcher = pattern.matcher(htmlContent);
        //匹配邮箱，提取加入数据库
        while (matcher.find()) {
            String e = matcher.group();
            System.out.println(e);
            try {
                Connection connection = JdbcUtils.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,e);
                statement.setDate(2,new java.sql.Date(System.currentTimeMillis()));
                statement.executeUpdate();
            } catch (SQLException e1) {
                continue;
            }finally {
                JdbcUtils.release();
            }
        }
    }

    //销毁Handler
    @Override
    public void destroy() {
        System.out.print("destroy...");
    }
}
