
Spring-Security


## 1 Spring Security框架介绍

### 1.1 Spring Security的功能

>1.Authentication:认证，就是用户登录。<br/>
>2.Authorization:授权，判断用户拥有什么权限<br/>
>3.安全防护，防止跨站请求，session攻击等<br/>
>4.非常容易结合SpringMVC进行使用<br/>


## 2 Spring Security与Shiro的区别

### 2.1 相同点
>1.认证功能：
>2.授权功能：
>3.加密功能：
>4.会话管理：
>5.缓存支持
>6.rememberMe功能：

### 2.2 不相同

**优点**

> 1 Spring Security 基于Spring开发，项目如使用Spring作为基础，配合Spring Security做权限更加方便。而Shiro需要和Spring整合进行开发<br/>
> 2 Spring Security 功能比Shiro更加丰富些，例如安全防护方面
> 3 Spring Security 社区资源相对比Shiro更加丰富

**缺点**
> 1 Shiro 的配置和使用比较简单，Spring Security 上手复杂些
> 2 Shiro 依赖性低，不需要任何框架和容器，可独立运行。Spring Security 依赖Spring容器

## 3 Spring Security+SpringMVC+Spring环境

### 3.1 项目结构

    // 项目的总体结构
    springsecurity1001
    ├── README.md                           
    ├── security-parent                     #依赖中心
    │   ├── pom.xml
    │   └── src
    └── spring-security01                   #springmvc+springsecurity demo
        ├── pom.xml
        └── src
        
    // spring-security01 结构
    spring-security01
    ├── pom.xml                                     #pom文件
    └── src
        ├── main
        │   ├── java
        │   ├── resources
        │   │   ├── applicationContext.xml          # 应用上线文容器文件
        │   │   ├── applicationTest.xml             # 测试备份，忽略
        │   │   ├── spring-security.xml             # spring-security文件
        │   │   └── springmvc.xml                   # springmvc容器文件
        │   └── webapp
        │       └── WEB-INF
        │           └── web.xml                     # web应用的web.xml文件
        └── test
            └── java


### 3.2 依赖文件pom.xml
    
#### 3.2.1 父security-parent的pom文件(好多用不着)

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
    
        <groupId>com.security.study</groupId>
        <artifactId>security-parent</artifactId>
        <version>1.0-SNAPSHOT</version>
        <!--    这个packaging一定要补上，自定义tomcat启动，没有启动在打包的时候会提示父工程应该是聚合工程的错误-->
        <packaging>pom</packaging>
    
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.1.5.RELEASE</version>
            <relativePath/>
        </parent>
    
    
        <properties>
            <junit.version>4.12</junit.version>
            <lombok.version>1.18.2</lombok.version>
            <slf4j.api.version>1.7.26</slf4j.api.version>
    
            <h2.version>1.4.199</h2.version>
            <mybatis.spring.version>2.1.0</mybatis.spring.version>
            <tk.mybatis.version>2.1.5</tk.mybatis.version>
            <tk.mapper.version>4.0.3</tk.mapper.version>
            <pagerhelper.version>5.1.8</pagerhelper.version>
            <commons.lang3.version>3.8.1</commons.lang3.version>
            <commons.io.version>2.6</commons.io.version>
            <fastdfs.client.version>1.0-SNAPSHOT</fastdfs.client.version>
    
            <spring.security.version>5.1.5.RELEASE</spring.security.version>
    
    
        </properties>
    
        <dependencyManagement>
            <dependencies>
                <dependency>
                    <groupId>junit</groupId>
                    <artifactId>junit</artifactId>
                    <version>${junit.version}</version>
                </dependency>
                <dependency>
                    <groupId>org.projectlombok</groupId>
                    <artifactId>lombok</artifactId>
                    <version>${lombok.version}</version>
                </dependency>
                <dependency>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-api</artifactId>
                    <version>${slf4j.api.version}</version>
                </dependency>
    
                <!--            数据库-->
                <dependency>
                    <groupId>com.h2database</groupId>
                    <artifactId>h2</artifactId>
                    <version>${h2.version}</version>
                </dependency>
    
                <dependency>
                    <groupId>tk.mybatis</groupId>
                    <artifactId>mapper-spring-boot-starter</artifactId>
                    <version>${tk.mybatis.version}</version>
                </dependency>
                <dependency>
                    <groupId>tk.mybatis</groupId>
                    <artifactId>mapper</artifactId>
                    <version>${tk.mapper.version}</version>
                </dependency>
    
                <dependency>
                    <groupId>com.github.pagehelper</groupId>
                    <artifactId>pagehelper</artifactId>
                    <version>${pagerhelper.version}</version>
                </dependency>
    
    
                <dependency>
                    <groupId>org.apache.commons</groupId>
                    <artifactId>commons-lang3</artifactId>
                    <version>${commons.lang3.version}</version>
                </dependency>
    
                <dependency>
                    <groupId>commons-io</groupId>
                    <artifactId>commons-io</artifactId>
                    <version>${commons.io.version}</version>
                </dependency>
    
                <dependency>
                    <groupId>org.springframework.security</groupId>
                    <artifactId>spring-security-web</artifactId>
                    <version>${spring.security.version}</version>
                </dependency>
                <dependency>
                    <groupId>org.springframework.security</groupId>
                    <artifactId>spring-security-config</artifactId>
                    <version>${spring.security.version}</version>
                </dependency>
    
    
            </dependencies>
        </dependencyManagement>
    
    </project>


    
    
    
    
    
#### 3.2.2 子springsecurity1001的pom文件

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <parent>
            <artifactId>security-parent</artifactId>
            <groupId>com.security.study</groupId>
            <version>1.0-SNAPSHOT</version>
            <relativePath>../security-parent/pom.xml</relativePath>
        </parent>
        <modelVersion>4.0.0</modelVersion>
    
        <artifactId>spring-security01</artifactId>
        <packaging>war</packaging>
    
    
        <dependencies>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-core</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-context</artifactId>
                <version>5.1.7.RELEASE</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-webmvc</artifactId>
                <version>5.1.7.RELEASE</version>
            </dependency>
    
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-config</artifactId>
            </dependency>
    
    
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-web</artifactId>
            </dependency>
    
            <dependency>
                <groupId>jstl</groupId>
                <artifactId>jstl</artifactId>
                <version>1.2</version>
            </dependency>
    
    
            <dependency>
                <groupId>javax.servlet</groupId>
                <artifactId>servlet-api</artifactId>
                <version>2.5</version>
                <!--            不加这scope启动会报错，自定义的tomcat启动-->
                <scope>provided</scope>
            </dependency>
        </dependencies>
    
    
        <build>
            <plugins>
                <!--            jdk版本插件-->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.1</version>
                    <configuration>
                        <source>1.8</source>
                        <target>1.8</target>
                        <encoding>UTF-8</encoding>
                        <showWarnings>true</showWarnings>
                    </configuration>
                </plugin>
    
                <!--            tomcat7插件-->
                <plugin>
                    <groupId>org.apache.tomcat.maven</groupId>
                    <artifactId>tomcat7-maven-plugin</artifactId>
                    <version>2.2</version>
                    <configuration>
                        <port>8080</port>
                        <path>/ssl</path>
                        <server>tomcat7</server>
                    </configuration>
                </plugin>
            </plugins>
    
        </build>
    </project>



### 3.3 配置文件

#### 3.3.1 web.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
             version="2.5">
    
    
        <!--    SpringSecurity过滤器链-->
        <filter>
            <filter-name>springSecurityFilterChain</filter-name>
            <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
        </filter>
        <filter-mapping>
            <filter-name>springSecurityFilterChain</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>
    
    
        <!--    启动Spring-->
        <listener>
            <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        </listener>
        <context-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>
                classpath:applicationContext.xml
                classpath:spring-security.xml
            </param-value>
        </context-param>
    
        <!--    启动SpringMvc-->
        <servlet>
            <servlet-name>DispatcherServlet</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <init-param>
                <param-name>contextConfigLocation</param-name>
                <param-value>classpath:springmvc.xml</param-value>
            </init-param>
            <!--        服务器启动就启动-->
            <load-on-startup>1</load-on-startup>
        </servlet>
        <servlet-mapping>
            <servlet-name>DispatcherServlet</servlet-name>
            <url-pattern>/</url-pattern>
        </servlet-mapping>
    </web-app>


#### 3.3.2 applicationContext.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    
    
    
    
    
    </beans>

    

#### 3.3.3 springmvc.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    
    
    
    
    
    </beans>


#### 3.3.4 spring-security.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:security="http://www.springframework.org/schema/security"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/security
            http://www.springframework.org/schema/security/spring-security-4.2.xsd">
    
    
    <!--    最基本的配置-->
        <security:http>
            <security:http-basic/>
        </security:http>
    
        <security:authentication-manager></security:authentication-manager>
    
    
    
    </beans>
    
    
#### 3.3.5 启动测试

![avatar](spring-security01/src/main/resources/images/start_springsecurity1001.jpg)

![avatar](spring-security01/src/main/resources/images/start_success.jpg)

## 4 HttpBasic方式的权限实现

### 4.1 创建ProductController


    spring-security01     
    ├── pom.xml
    └── src
        └── main
            ├── java
            │   └── com
            │       └── mvc
            │           └── security
            │               └── study
            │                   └── controller
            │                       └── ProductController.java          #商品controller
            ├── resources
            │   ├── applicationContext.xml
            │   ├── applicationTest.xml
            │   ├── images                                              # markdown 图片文件
            │   │   ├── start_springsecurity1001.jpg
            │   │   └── start_success.jpg
            │   ├── spring-security.xml
            │   └── springmvc.xml
            └── webapp
                └── WEB-INF
                    ├── jsp
                    │   ├── index.jsp                                   # index首页：localhost:8080/ssl/proudct/index
                    │   └── product                                     # 商品的crud页面
                    │       ├── productAdd.jsp
                    │       ├── productDelete.jsp
                    │       ├── productList.jsp
                    │       └── productUpdate.jsp
                    └── web.xml

    
### 4.2 配置springmvc.xml文件

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:contentxt="http://www.springframework.org/schema/context"
           xmlns:mvc="http://www.springframework.org/schema/mvc"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">
    
    
        <!--    扫描Controller-->
        <contentxt:component-scan base-package="com.mvc.security.study"/>
    
        <!--    注解方式处理器映射器和处理器适配器-->
        <mvc:annotation-driven/>
    
        <!--    视图解析器-->
        <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
            <property name="prefix" value="/WEB-INF/jsp/"></property>
            <property name="suffix" value=".jsp"></property>
        </bean>
    
    </beans>


    







   


