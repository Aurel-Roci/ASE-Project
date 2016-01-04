<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.xml.bind.DatatypeConverter" %>
<%@ page import="java.io.ByteArrayInputStream" %>
<%@ page import="java.io.ObjectInputStream" %>
<%@ page import="java.io.ObjectInput" %>
<%@ page import="com.googlecode.objectify.Key" %>
<%@ page import="com.googlecode.objectify.ObjectifyService" %>
<%@ page import="de.tum.score.transport4you.web.User" %>
<%@ page import="de.tum.score.transport4you.web.Group" %>
<%@ page import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope" %>
<%@ page import="de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="/main.css"/>
</head>

<body>
<%
String title; 
if (session.getAttribute("user") == null && (request.getParameter("user") == null || request.getParameter("pass") == null) ){
%>

<form method="post" action="index.jsp">
<p>Username: <input type="text" name="user"/></p>
<p>Password: <input type="password" name="pass"/></p>
<input type="submit"/>
</form>
<%
//check credentials
}else if(session.getAttribute("user") == null){
	Key<Group> group = Key.create(Group.class, "default");
	List<User> users = ObjectifyService.ofy()
	.load()
	.type(User.class)
	.ancestor(group)
	.order("-date")
	.list();
	for (User u : users) {
		if(	u.name.equals(request.getParameter("user")) && 
			u.password.equals(User.hash(request.getParameter("pass")))){
				session.setAttribute("user",u.name);
				session.setAttribute("ticket",u.ticket);
				out.println("Hello " + session.getAttribute("user"));
				out.println("<p>"+u.name+" - "+u.password+" is valid </p>");
		}


	}



%>
<p>Checking credentials</p>
<%
// we have autheticated the user 
}else{
%>
<p>User known</p>
<%
String ticketString = session.getAttribute("ticket").toString();
ByteArrayInputStream byteInputStream = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(ticketString));
ObjectInput in = new ObjectInputStream(byteInputStream);
BlobEnvelope result = (BlobEnvelope) in.readObject();
BlobEntry data = result.getPublicBlobEntry();

out.println("<p>Balance is:"+data.getAccountBalance()+"</p>");

}

%>
</body>
</html>
