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
	//��ȡSession�е��û�Ȩ����Ϣ
	String name = (String) session.getAttribute("username");
	String grade = (String) session.getAttribute("grade");
	//������ǹ���Ա���ض��򵽵�¼ҳ��
	if(!"amdin".equals(grade)) {
		response.sendRedirect("/login.html");
	}
	//�Ϸ��û�
	out.println("<HTML>");
	out.println("  <HEAD><TITLE>����Աҳ��</TITLE></HEAD>");
	out.println("  <BODY>");
	out.println("<br>��ӭ" + name + "����Ա!");
	out.println("  </BODY>");
	out.println("</HTML>");
	out.flush();
	out.close();
	}

}
