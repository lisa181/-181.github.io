package com;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=GBK");
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession();
		//获取Session中的用户权限信息
		String name = (String) session.getAttribute("username");
		String grade = (String) session.getAttribute("grade");
		//如果不是管理员，重定向到登录页面
		if(!"admin".equals(grade)) {
			response.sendRedirect("../login.html");
		}
		//合法用户
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>管理员页面</TITLE></HEAD>");
		out.println("  <BODY>");
		out.println("<br>欢迎" + name + "管理员!");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}
}
