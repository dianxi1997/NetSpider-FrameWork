<%--
  Created by IntelliJ IDEA.
  User: 宇强
  Date: 2016/4/18 0018
  Time: 14:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>

    <form action="${pageContext.request.contextPath}/spider"
          enctype="multipart/form-data" method="post">
      爬虫URL列表关闭前的最大网页访问数：<input type="text" name="maxCount" value="1000"><br/>
      上传Handler的jar包：<input type="file" name="jar"><br/>
      本次爬虫执行的SpiderHandler类路径：<input type="text" name="class" value="cn.coselding.netspider.example.EmailHandler"><br/>
      种子URL列表（一行一个URL，英文;隔开）：<textarea name="urls"></textarea><br/>
      <input type="submit" name="submit" value="提交">
    </form>
  </body>
</html>
