# booking 客房预订管理系统

### 如何进入后台
- 页脚处有`进入后台`的入口
- 或者直接在浏览器输入`/admin`

### 数据库
- 数据库链接地址和密码在`application.properties`文件配置
- ！！！**云数据库密码不能上传到GitHub**
- ！！！**云数据库密码不能上传到GitHub**
- ！！！**云数据库密码不能上传到GitHub**
```javascript
#下面的是本地数据库配置
#spring.datasource.url = jdbc:mysql://localhost:3306/db_booking?serverTimezone=Asia/Shanghai&characterEncoding=utf-8
#spring.datasource.password = root
#下面的是云数据库配置
spring.datasource.url = jdbc:mysql://129.204.15.163:3306/db_booking?serverTimezone=Asia/Shanghai&characterEncoding=utf-8
spring.datasource.password = 省略
```

### 数据录入参考地址
- [锦江酒店Wehotel官网](https://hotel.bestwehotel.com/HotelSearch/)

## 前端源码
[点击这里跳转到booking-ui前端源码](https://github.com/WenjieZhengJerry/booking-ui)

## 更新日志
- 2019年12月28日：酒店添加新字段后,修改后台酒店管理各个功能、删除酒店/房间时同时删除对应的图片
- 2019年12月27日：完成后台评论管理等功能、完成用户界面评论的分页显示等功能、完成订单评价功能；完成首页酒店多条件查询功能
- 2019年12月25日：新增订单自动取消功能；酒店增加最低价字段显示
- 2019年12月24日：完成用户中心的酒店订单显示功能、完成酒店管理的编辑功能、多条件查询功能、完成房间管理所有功能
- 2019年12月23日：完成订单付款功能
- 2019年12月22日：完成用户登录、注册功能、添加数据库初始化测试类 InitDB 可以用它来初始化，使用时请将原有数据库删除并新建
- 2019年12月20日：完成首页酒店的分页显示功能(不包括多条件查询和排序)，酒店详情的显示功能(不包括评论和入住时间查询)，订单预订功能
- 2019年12月18日：完成用户管理的增加用户\更新用户、单项删除用户、批量删除用户、多条件查询用户等功能
- 2019年12月18日：完成订单管理的多条件查询功能
- 2019年12月17日：完成酒店管理单项删除、批量删除、添加酒店功能
- 2019年12月9日：完成订单管理单项删除、批量删除、编辑功能
