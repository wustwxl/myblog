#使用SpringCloud搭建个人博客入门#
##本Demo主要使用了Eureka、Ribbon/Feign##


简述框架
两个微服务,分别是文章服务提供者8001端口,和评论服务提供者8011端口,
微服务之间的通讯采用HttpClient。

系统服务对外消费者80端口,
与服务提供者的调用主要借助Eureka服务注册与发现进行调用,服务提供者返回Json数据,服务消费者接收后进行解析处理,然后传递给前端Thymeleaf进行显示。

eureka服务7001端口,
服务注册与发现。

api服务封装为Jar包,
主要包括JavaBean对象以及Util工具类,方便提供给其余服务直接调用。

mybatis-generator服务,
用于反向生成Mapper文件。

other-blog为本Demo原本模版(大神博客站点：http://13blog.site),该Demo主要参考其改造而成,可直接运行,端口为9001。
http://13blog.site

注释：本Demo主要实现了博客的文章模块和评论模块,用于自身回顾Eureka,
如想了解SpringCloud其余功能的使用,可参考GitHub上的其余Demo。

ToDo:
后台仪表盘的显示
后台文件管理
后台页面管理
后台友链管理
后台系统设置
帐号登陆


