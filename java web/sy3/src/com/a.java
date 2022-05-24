package com;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class a extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	response.setContentType("text/html;charset=GBK");
	PrintWriter out = response.getWriter();
	
	HttpSession session = request.getSession();
	//获取Session中的用户权限信息
	String name = (String) session.getAttribute("username");
	String grade = (String) session.getAttribute("grade");
	//如果不是管理员，重定向到登录页面
	if(!"amdin".equals(grade)) {
		response.sendRedirect("/login.html");
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
