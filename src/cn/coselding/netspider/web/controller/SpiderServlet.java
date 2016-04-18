package cn.coselding.netspider.web.controller;

import cn.coselding.netspider.domain.MyUrl;
import cn.coselding.netspider.service.MyUrlServiceImpl;
import cn.coselding.netspider.spider.SpiderHandler;
import cn.coselding.netspider.spider.SpiderService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**处理爬虫请求的Servlet
 * Created by 宇强 on 2016/4/18 0018.
 */
@WebServlet(name = "SpiderServlet")
public class SpiderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取表单数据
        Map<String,Object> params = fileForm(request,response);
        //System.out.print(urls);
        //截取种子URL列表
        String urls[] = params.get("urls").toString().split(";");
        List<String> list = new ArrayList<String>();
        for (String u : urls) {
            System.out.print(u);
            list.add(u);
        }

        //把种子URL添加到数据库
        MyUrlServiceImpl service = new MyUrlServiceImpl();
        service.addUrls(list);

        //查询第一个URL开始请求
        MyUrl myUrl = service.getLastMyUrl();
        //开始爬虫
        SpiderService spiderService = new SpiderService(service,myUrl,params.get("maxCount").toString());

        try {
            Class<? extends SpiderHandler> clazz = null;
            //有jar包的话用特定的类加载器
            if(params.get("jar")!=null){
                //加载jar包
                URLClassLoader myClassLoader = new URLClassLoader(
                        new URL[] { new URL("file:"+((File)params.get("jar")).getAbsolutePath()) },
                        Thread.currentThread().getContextClassLoader());
                clazz = (Class<? extends SpiderHandler>) myClassLoader.loadClass(params.get("class").toString());
            }else{
                //没jar包的话，使用系统的原类加载器，从类路径加载
                clazz = (Class<? extends SpiderHandler>) Class.forName(params.get("class").toString());
            }
            //对该网页数据进行相关的提取操作
            SpiderHandler handler = clazz.newInstance();

            //用Handler来爬取所需数据
            spiderService.execute(handler);

        } catch (ClassNotFoundException e) {
            request.setAttribute("message","您输入的class类不存在哦！！！");
            request.getRequestDispatcher("/message.jsp").forward(request,response);
            return;
        } catch (InstantiationException e) {
            request.setAttribute("message","服务器未知异常！！！");
            request.getRequestDispatcher("/message.jsp").forward(request,response);
            return;
        } catch (IllegalAccessException e) {
            request.setAttribute("message","服务器未知异常！！！");
            request.getRequestDispatcher("/message.jsp").forward(request,response);
            return;
        }
    }

    /**获取普通的表单数据
     * @param request
     * @return
     */
    public Map<String,Object> formData(HttpServletRequest request){
        //获取表单数据
        String url = request.getParameter("urls");
        String classPath = request.getParameter("class");
        String maxCount = request.getParameter("maxCount");

        Map<String,Object> params = new HashMap<String, Object>();
        params.put("urls",url);
        params.put("class",classPath);
        params.put("maxCount",maxCount);
        return params;
    }

    /**获取文件表单数据
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public Map<String,Object> fileForm(HttpServletRequest request,HttpServletResponse response) throws IOException {
        // /服务器文件保存根目录
        String savePath = this.getServletContext().getRealPath(
                "/WEB-INF/upload");
        System.out.println("savePath = " + savePath);
        // 初始化upload对象
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");// 上传文件名中文乱码问题

        if (!upload.isMultipartContent(request)) {
            // /普通类型的表单
            return null;
        }
        // /获得上传数据
        List<FileItem> list = null;
        try {
            list = upload.parseRequest(request);
        } catch (FileUploadException e1) {
            throw new RuntimeException(e1);
        }
        // 设置单个文件大小
        upload.setFileSizeMax(1024);
        // 设置总文件大小
        upload.setSizeMax(1024 * 10);
        // 监听文件上传进度
        upload.setProgressListener(new ProgressListener() {

            public void update(long readedSize, long totalSize, int items) {
                System.out.println("文件总大小：" + totalSize + " , 已上传大小："
                        + readedSize);
            }
        });
        Map<String,Object> params = new HashMap<String, Object>();
        // 进行文件保存操作
        for (FileItem item : list) {

            if (item.isFormField()) {
                // /表单中的普通类型数据
                String name = item.getFieldName();
                // 普通表单中文乱码问题手工转换
                String value = item.getString("UTF-8");
                params.put(name,value);
            } else {
                // 获得文件名
                String filename = item.getName();

                // 判断上传文件为空
                if (filename == null || filename.trim().equals(""))
                    continue;

                filename = filename.substring(filename.lastIndexOf("\\") + 1);
                System.out.println("upload : filename = " + filename);
                // /原文件名生成对应的随即目录

                String realPath = EncodePath(filename, savePath);
                System.out.println("最终路径=" + realPath);

                // 获取文件流
                InputStream inputStream = item.getInputStream();
                File file = new File(realPath);
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                inputStream.close();
                outputStream.close();
                item.delete();
                params.put("jar",file);
            }
        }
        return params;
    }

    /**合成哈希打散路径
     * @param filename
     * @param savePath
     * @return
     */
    public String EncodePath(String filename, String savePath) {

        int hashCode = filename.hashCode();
        int dir1 = hashCode & 0xf;
        int dir2 = (hashCode & 0xf0) >> 4;
        String path = savePath + "\\" + dir1 + "\\" + dir2 + "\\";
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        return path + filename;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request, response);
    }
}
