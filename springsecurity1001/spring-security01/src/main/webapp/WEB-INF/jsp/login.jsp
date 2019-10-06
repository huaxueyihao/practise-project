<%--
  Created by IntelliJ IDEA.
  User: amao
  Date: 2019-10-06
  Time: 10:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登陆页面</title>
</head>

    <h3>登陆页面</h3>
    <form action="${pageContext.request.contextPath}/securityLogin" method="post">
        用户名 <input type="text" name="username"> <br/>
        密码 <input type="text" name="password"> <br/>

        <input type="submit" value="提交">

    </form>

</body>
</html>
