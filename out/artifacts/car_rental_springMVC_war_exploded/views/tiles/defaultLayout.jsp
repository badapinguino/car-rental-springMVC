<%--
  Created by IntelliJ IDEA.
  User: SI2001
  Date: 16/09/2019
  Time: 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><tiles:getAsString name="title" /></title>
    <link href="<c:url value='/resources/css/bootstrap.css' />"  rel="stylesheet"></link>
    <link href="<c:url value='/resources/css/mio.css' />" rel="stylesheet"></link>
</head>

<body>
<header id="header">
    <tiles:insertAttribute name="header" />
</header>

<%--<section id="sidemenu">--%>
<%--    <tiles:insertAttribute name="menu" />--%>
<%--</section>--%>

<section id="site-content">
    <tiles:insertAttribute name="body" />
</section>

<%--<footer id="footer">--%>
<%--    <tiles:insertAttribute name="footer" />--%>
<%--</footer>--%>
</body>
</html>