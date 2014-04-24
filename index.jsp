<%-- 
    Document   : index
    Created on : Mar 7, 2014, 7:38:00 AM
    Author     : xumak-pc
--%>

<%@page import="java.awt.SystemColor, java.text.*, java.util.*"%> <%-- This line is used to make imports from java libraries, called directives ---%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%@include file="yoho.jsp" %>
        
        <%
            System.out.println("Calculating date");
            java.util.Date date = new java.util.Date();
        
        %>
        Hello! The time is now <%= date %>
        And by generating html, the time is now
        <% 
            out.println(String.valueOf(date)); 
            out.println("<BR>Your machine's address is: ");
            out.println(request.getRemoteAddr());
        %>
        <TABLE BORDER=2>
        <%
            for ( int i = 0; i < 5; i++ ) {
                %>
                <TR>
                <TD>Number</TD>
                <TD><%= i+1 %></TD>
                </TR>
                <%
            }
        %>
        <%-- To add declaration --%>
        <BR>
        <%!
            Date theDate = new Date();
            Date getDate()
            {
                System.out.println( "In getDate() method" );
                return theDate;
            }
        %>
        Hello!  The time is now <%= getDate() %>
        </TABLE>
    </body>
</html>
