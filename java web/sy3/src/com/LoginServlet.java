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
		
		if("admin".equals(name) && "888888".equals(psw)){//����Ա�û�
			HttpSession session = request.getSession();
			//�ѵ�¼��Ϣ���û������û�Ȩ�޵ȼ����洢��Session�С�
			session.setAttribute("username",name);
			session.setAttribute("grade", "admin");
			//�ض��򵽹���Աҳ��
           response.sendRedirect("AdminServlet");
		}
		else if("normal".equals(name) && "123456".equals(psw)){//��ͨ�û�
			HttpSession session = request.getSession();
			//�ѵ�¼��Ϣ���û������û�Ȩ�޵ȼ����洢��Session�С�
			session.setAttribute("username",name);
			session.setAttribute("grade", "normal");
			//�ض��򵽹���Աҳ��
	         response.sendRedirect("NormalServlet");
		}
		else{//�Ƿ��û�
			out.print("<BR>�û�����������������µ�¼��<br>");	
			//ͨ������ת���ѵ�¼����������
			request.getRequestDispatcher("/login.html").include(request, response);
		}
	}
}
