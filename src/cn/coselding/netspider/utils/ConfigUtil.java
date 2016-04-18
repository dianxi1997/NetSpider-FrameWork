package cn.coselding.netspider.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 宇强 on 2016/4/18 0018.
 */
public class ConfigUtil {

    private static Properties properties = null;
    static {
        //静态代码块，加载配置文件
        InputStream is = null;
        properties = new Properties();
        try {
            is = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**获取配置文件属性
     * @param name
     * @return
     */
    public static String getProperty(String name){
        return properties.getProperty(name);
    }
}
