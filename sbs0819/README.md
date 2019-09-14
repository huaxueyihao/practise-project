
## 1.目标
   
    从最简单的springboot+layui单体项目，整合不同的框架或技术方案进行迭代升级学习,
    
    

## 2.项目结构介绍
    
    --sbs0819
    |----boot-aparent                ##父工程，依赖中心
    |----boot-bmodule01              ##子模块01，即是最简单的springboot+layui+tk.mybatis+h2(数据库)，主要学习layui 
    |----boot-bmodule0101            ##子模块0101,将layui换成layuimini，完成菜单动态加载，文件上传等
    |----boot-bmodule0102            ##子模块0102,主要是boot-bmodule01模块的稳定版，前端仍有需要优化，但不是专业前端，不在优化
    |----boot-bmodule02              ##子模块02，主要完善后端，主要完成登录认证功能，日志管理，统一异常处理等后台功能
    |----boot-bmodule03              ##子模块03，进行前后端分离，前端采用vue.js(使用nginx)，主要使用学习前后端，以及后端的单点登录问题
    |----boot-bmodule04              ##子模块04，将mybatis持久层，封装api化
    |----boot-bmodule05              ##子模块05以上，进行分布式化，整合redis，activemq等等
    |----fastdfs-client              ##fastdfs-client,文件服务器上传的依赖模块，这是第三方提供
    |----file-upload                 ##file-upload多种文件存储方式支持
    
    
    
## 3.最新进展

    2019-09-14至2019-09-20 新增 boot-bmodule0102稳定版及file-upload模块
    2019-09-06至2019-09-10 新增 boot-bmodule0101小版本进行过度
    2019-09-01至2019-09-04 boot-bmodule01 已经完成
    
    
    
    

   


