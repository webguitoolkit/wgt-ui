<%@ page language="java" pageEncoding="UTF-8" isErrorPage="true" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>Cookie error</title>
	<link rel="stylesheet" type="text/css" href="./styles/ibacer.css">  
  </head>
	<body style="margin: 10px;">
	<h1>Cookie error</h1>
	<span style="color:red;">You have to allow session cookies to use this application</span><br><br>
	<hr>
	User Agent: <%= request.getHeader("User-Agent") %><br>
	Referer: <%= request.getHeader("Referer") %><br>
	Accept: <%= request.getHeader("Accept") %><br>
	Remote: <%=request.getRemoteHost() %> - <%= request.getRemoteAddr() %><br>
	Date: <script language=javascript> 
		  var date=new Date(); 
		  var dd=date.getDate(); 
		  var mm=date.getMonth() + 1; 
		  var yy=date.getYear(); 
		  var HH=date.getHours(); 
		  var MM=date.getMinutes(); 
		  document.write(dd+"."+mm+"."+yy+" / "+HH+":"+MM); 
</script>
  </body>
</html>
