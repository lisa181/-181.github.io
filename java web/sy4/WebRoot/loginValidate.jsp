<%@ page  pageEncoding="GBK"%>
<%
	String username = request.getParameter("username");
		String password = request.getParameter("password");
		String age  = request.getParameter("age");
		int cookieAge = (age==null)? 0: Integer.parseInt(age);
	//�û���Ϣ��֤�������Ǽ��趨���Ϸ��û���ʵ���з��������ݿ������֤
	if("web".equals(username) && "123456".equals(password) ||
		"jsp".equals(username) && "888888".equals(password) ) 
	 {
		Cookie mycookie =  new Cookie("cookie_username",null);//�½�
		//�ѵ�¼��Ϣ����Cookie
		mycookie.setValue(username);
		if(cookieAge!=0) mycookie.setMaxAge(cookieAge*24*3600); //�������Ĵ���ڣ���λΪ��
		response.addCookie(mycookie);                       //��cookieд�������
		response.sendRedirect("work.jsp");                  //�ض��򵽹���ҳ��
	}
	else{
		out.print("<p><b>��û�е�¼���û���������������µ�¼��</b><p>");
		pageContext.include("login.html");               //��̬����
		}
 %>
