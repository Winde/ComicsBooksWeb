<%-- 
    Document   : changepath
    Created on : May 28, 2012, 3:53:52 PM
    Author     : Winde
--%>


<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%@ include file="/WEB-INF/jsp/topbar.jsp" %>

        <style>
            form>ul>li {
                list-style-type:none;               
            }
            form>ul {
                margin:0;
                padding:0;
            }
            
            body {
                margin: 0;
                background-repeat: repeat;
                background: url('resources/bookshelf.jpg');
                background-position: 0 40px;
            }
        </style>
        <div style="width:500px;margin:0 auto 0 auto;padding-top:60px">
            <form:form  method="post" modelAttribute="paths">
                <ul style="text-align:right;">
                    <li><form:label path="" cssStyle="float:left;color:white;font-weight:bold; margin-right:10px;padding-top:3px;">Comics Directory</form:label><form:input path="pathComics" cssStyle="width:67%;" id="pageInput"></form:input></li>
                    <li><form:label path="" cssStyle="float:left;color:white;font-weight:bold;margin-right:10px;padding-top:3px;">Books Directory</form:label><form:input path="pathLibros" cssStyle="width:67%;" id="pageInput"></form:input></li>
                        <li><input type="submit" value="Go" /></li>
                    </ul>
            </form:form>
        </div>

    </body>
</html>
