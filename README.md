# NetSpider-FrameWork
Java编写的网络爬虫框架，Web管理界面，使用数据库作为URL队列储存和爬取数据储存载体，先完成了最基本的功能，日后慢慢完善哈，先推上来啦~  
开发环境为：IntelliJ IDEA 14+Tomcat8+Mysql5.5+JDK1.8+JavaEE7  
打算做成框架形式的webapp，部署之后，只要提供好起始的种子URL，设定要搜索深度，就能自动去抓取指定网页，并自动收集URL继续查找，而提取数据功能呢，打算以上传jar包的形式，jar包中继承指定的Handler类，打包好上传，并给出Handler类的带限定类名，这样在搜索网页的时候就会自动运行这个Handler，Handler中可以得到每次抓取的网页html源码，只要在Handler中对网页源码进行业务相关处理即可方便的实现自己需要的网络爬虫功能，里面提供了提取网页中邮箱的demo样例作为测试使用。  
