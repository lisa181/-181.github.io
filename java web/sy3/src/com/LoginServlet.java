package com;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=GBK");
		PrintWriter out = response.getWriter();
		
		String name = request.getParameter("username");
		String psw = request.getParameter("password");
		
		if("admin".equals(name) && "888888".equals(psw)){//管理员用户
			HttpSession session = request.getSession();
			//把登录信息（用户名和用户权限等级）存储在Session中。
			session.setAttribute("username",name);
			session.setAttribute("grade", "admin");
			//重定向到管理员页面
           response.sendRedirect("AdminServlet");
		}
		else if("normal".equals(name) && "123456".equals(psw)){//普通用户
			HttpSession session = request.getSession();
			//把登录信息（用户名和用户权限等级）存储在Session中。
			session.setAttribute("username",name);
			session.setAttribute("grade", "normal");
			//重定向到管理员页面
	         response.sendRedirect("NormalServlet");
		}
		else{//非法用户
			out.print("<BR>用户名或密码错误！请重新登录！<br>");	
			//通过请求转发把登录表单包含进来
			request.getRequestDispatcher("/login.html").include(request, response);
		}
	}
}
