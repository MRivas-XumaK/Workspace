<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>File Upload!</h1>
        <form action="FileServlet" method="POST" enctype="multipart/form-data" >
            File: <input type="file"  name="file" id="file"/>
            <br>
            <input type="submit" value="Upload" name="upload" id="upload"/>
        </form>
    </body>
</html>
