<%@ page  pageEncoding="GBK"%>
<%
	String username = request.getParameter("username");
		String password = request.getParameter("password");
		String age  = request.getParameter("age");
		int cookieAge = (age==null)? 0: Integer.parseInt(age);
	//用户信息验证，这里是简单设定两合法用户，实际中访问有数据库进行验证
	if("web".equals(username) && "123456".equals(password) ||
		"jsp".equals(username) && "888888".equals(password) ) 
	 {
		Cookie mycookie =  new Cookie("cookie_username",null);//新建
		//把登录信息存入Cookie
		mycookie.setValue(username);
		if(cookieAge!=0) mycookie.setMaxAge(cookieAge*24*3600); //设置最大的存活期，单位为秒
		response.addCookie(mycookie);                       //把cookie写入浏览器
		response.sendRedirect("work.jsp");                  //重定向到工作页面
	}
	else{
		out.print("<p><b>你没有登录或用户名密码错误！请重新登录！</b><p>");
		pageContext.include("login.html");               //动态包含
		}
 %>
