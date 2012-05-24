<%-- 
    Document   : hello
    Created on : May 16, 2012, 3:56:20 PM
    Author     : Winde
--%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html>
<html>
    <c:set var="pathNice" value="${fn:substringAfter(param.ruta,'/')}"/>
    <head>
        <title>
            <c:choose><c:when test="${model.contentType.equals('libros')}">Libros: </c:when><c:when test="${model.contentType.equals('comics')}">Comics: </c:when></c:choose>
            <c:choose><c:when test="${param.ruta!=null}"><c:out value="${pathNice}" /></c:when></c:choose>
        </title>
        <script type="text/javascript" src="resources/jquery.js"></script>
    </head>
    <body>
        <style>
            p {
                font-family: Verdana, Tahoma, Helvetica, Arial;
                font-size: 11px;
            }     
            a {
                text-decoration:none;
                color: #000;
            }
            .icon:hover {
                opacity: 0.9;
                text-shadow: 0px 0px 3px #CCC;
            }   
            .icon:hover>img {
                box-shadow: 0px 0px 5px #CCC;
            }
            .icon {
                width: 124px;
                height: 120px;
                display:inline-block;
                text-align: center;
                position: relative;
            }      
            .icon>img {
                width: 64px;
                height: 52px;
                position: absolute;
                top: 3px;
                left: 30px
            }
            .icon>p {
                position: absolute; 
                top: 56px; 
                left: 0px;
                height: 50px;
                overflow: hidden;
                text-align:center;
                width: 124px;
                padding: 0 2px 2px 2px
            } 

        </style>
        <div>

            <c:choose>
                <c:when test="${model.contentType.equals('comics') && param.ruta==null}">
                <a href="<c:url value="/libros.htm" ></c:url>" title="Libros">  
                    <div class="icon">
                        <img src="resources/folder_libros.png" />
                        <p>Libros</p>
                    </div>
                </a>
            </c:when>
                 <c:when test="${model.contentType.equals('libros') && param.ruta==null}">
                <a href="<c:url value="/comics.htm" ></c:url>" title="Comics">  
                    <div class="icon">
                        <img src="resources/folder_comics.png" />
                        <p>Comics</p>
                    </div>
                 </a>
                 </c:when>                
            </c:choose>


    <c:forEach items="${model.directorios}" var="dir">
        <c:set var="trimDir" value="${fn:substring(dir,0,50)}"/>
        <a href="<c:url value="" ><c:param name="ruta" value="${param.ruta}/${dir}"/></c:url>" title="<c:out value="${dir}"/>">
            <div class="icon">
                <img src="resources/folder.png" />
                <p><c:out value="${trimDir}"/></p>
            </div>
        </a>
    </c:forEach>
    <c:forEach items="${model.ficheros}" var="fichero">
        <c:set var="trimFichero" value="${fn:substring(fichero.filename,0,50)}"/>
        <c:choose>
            <c:when test="${fichero.contentType.equals('libros')}">
                <a href="<c:url value="/libro.htm" ><c:param name="ruta" value="${param.ruta}/${fichero.filename}"/><c:param name="pagina" value="0"/></c:url>" title="<c:out value="${fichero.filename}"/>">
                    <div class="icon">
                        <img src="resources/book.png" />
                    </c:when>
                    <c:when test="${fichero.contentType.equals('comics')}">
                        <a href="<c:url value="/comic.htm" ><c:param name="ruta" value="${param.ruta}/${fichero.filename}"/><c:param name="pagina" value="0"/></c:url>" title="<c:out value="${fichero.filename}"/>">
                            <div class="icon">
                                <img src="resources/comic.png" />
                            </c:when>                            
                        </c:choose>                             
                        <p><c:out value="${trimFichero}"/></p>
                    </div>
                </a>
            </c:forEach>
            <c:set var="baseURL" value="${fn:substringBefore(window.location.pathname,'?')}"/>
            <c:choose>
                <c:when test="${model.parentDirectory==''}">
                    <c:choose>
                        <c:when test="${not empty param.ruta}">

                            <a href="<c:url value="${requestScope['javax.servlet.forward.servlet_path']}" ></c:url>"><div class="icon"><img src="resources/parent.png" /></div></a>
                                </c:when>
                            </c:choose>
                        </c:when>
                        <c:otherwise>  
                    <a href="<c:url value="" ><c:param name="ruta" value="${model.parentDirectory}"/></c:url>"><div class="icon"><img src="resources/parent.png" /></div></a>
                        </c:otherwise>
                    </c:choose>
        </div>
        <br/>


        </body>
        </html>
