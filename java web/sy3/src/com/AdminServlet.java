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
		//��ȡSession�е��û�Ȩ����Ϣ
		String name = (String) session.getAttribute("username");
		String grade = (String) session.getAttribute("grade");
		//������ǹ���Ա���ض��򵽵�¼ҳ��
		if(!"admin".equals(grade)) {
			response.sendRedirect("../login.html");
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
