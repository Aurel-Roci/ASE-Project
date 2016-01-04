<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="de.tum.score.transport4you.web.Group" %>
<%@ page import="de.tum.score.transport4you.web.User" %>
<%@ page import="com.googlecode.objectify.Key" %>
<%@ page import="com.googlecode.objectify.ObjectifyService" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
</head>

<body>

<%
	
	Key<Group> group = Key.create(Group.class, "default");
	List<User> users = ObjectifyService.ofy()
		.load()
		.type(User.class) // We want only Greetings
		.ancestor(group)    // Anyone in this book
		.order("-date")       // Most recent first - date is indexed.
	//	.limit(5)             // Only show 5 of them.
		.list();

    if (users.isEmpty()) {
	%>
		<p>No Users.</p>
	<%
    } else {
	%>
	<p>Users:</p>
	<ul>	
<%
	}
	// Look at all of our greetings
	for (User u : users) {
		pageContext.setAttribute("name",u.name);
		%>
			<li>${fn:escapeXml(name)}</li>
		<%

	}
%>
</ul>
<form action="/add" method="post">
	<div>Name:<input type="text" name="user_name" /></div>
	<div>E-Mail:<input type="text" name="user_email" /></div>
	<div>User ID:<input type="text" name="user_id" /></div>
	<div>Password:<input type="text" name="user_password" /></div>
	<div>User Address:<input type="text" name="user_address" /></div>
	<div>User Balance:<input type="text" name="user_balance" /></div>
	<div><input type="submit" value="Add User"/></div>
</form>
</body>
</html>
<%-- //[END all]--%>
