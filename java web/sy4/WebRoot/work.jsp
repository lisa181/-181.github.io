<%@ page  pageEncoding="GBK" %>
<% 
	String username = null;
	Cookie c[] = request.getCookies();//��������Cookies
	if(c!=null){ //����Cookie
		for(int i=0; i < c.length; i++) //��������Cookies,������Ҫ��Cookie
		{
			String cookieName = c[i].getName(); 
			if(cookieName.equals("cookie_username")) //�ҵ�
				username = c[i].getValue(); //ȡ��ֵ
		}
	}
	else{//������Cookie,�ض���
	   response.sendRedirect("login.html");
	}
	
	if(null==username || 0 == username.length()){ //����������û�ҵ������Cookie
		response.sendRedirect("login.html");  //ת���µ�¼
	}
	else{ //�ҵ�����Cookie
		out.print("<p><b>��ӭ"+ username + "</b></p>");
		out.print("�����ǹ���ҳ�������......");
	}
%>