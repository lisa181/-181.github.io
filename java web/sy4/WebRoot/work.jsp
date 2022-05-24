<%@ page  pageEncoding="GBK" %>
<% 
	String username = null;
	Cookie c[] = request.getCookies();//读出所有Cookies
	if(c!=null){ //存在Cookie
		for(int i=0; i < c.length; i++) //遍历所有Cookies,查找需要的Cookie
		{
			String cookieName = c[i].getName(); 
			if(cookieName.equals("cookie_username")) //找到
				username = c[i].getValue(); //取出值
		}
	}
	else{//不存在Cookie,重定向
	   response.sendRedirect("login.html");
	}
	
	if(null==username || 0 == username.length()){ //遍历结束后没找到所需的Cookie
		response.sendRedirect("login.html");  //转重新登录
	}
	else{ //找到所需Cookie
		out.print("<p><b>欢迎"+ username + "</b></p>");
		out.print("这里是工作页面的内容......");
	}
%>