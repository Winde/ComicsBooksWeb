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

            .contentType {
                width: 160px;
                height: 160px;
                display:inline-block;
                text-align: center;
                position: relative;
                vertical-align: top;
            }
            .contentType>a>img {
                width: 130px;
                height: 130px;
                position: absolute;
                top: 20px;
                left: 30px
            }   

            .icon {
                width: 130px;
                height: 160px;
                display:inline-block;
                text-align: center;
                position: relative;
                vertical-align: top;
            }      
            .icon>a>img {
                width: 90px;
                height: 128px;
                position: absolute;
                top: 7px;
                left: 30px
            }            
            .icon>.folder {
                width: 90px;
                height: 128px;
                position: absolute;
                top: 7px;
                left: 30px
            }
            .icon>.badgeRead {
                width: 65px;
                height:65px;
                position: absolute;
                top: -5px;
                left: 75px
            }
            .icon>p {
                position: absolute; 
                top: 10px; 
                left: 30px;
                height: 90px;
                overflow: hidden;
                text-align:center;
                width: 65px;
                padding: 0 2px 2px 2px;
                color: white;
                text-shadow: grey 2px 1px 1px;
            }

            .iconFolder {
                top: 4px; 
            }

            .icon>a>.pInside {
                position: absolute; 
                top: 10px; 
                left: 30px;
                height: 90px;
                overflow: hidden;
                text-align:center;
                width: 65px;
                padding: 0 2px 2px 2px;
                color: black;
                text-shadow: grey 2px 1px 1px;
            }

            .iconFolder>a>.pInside{ 
                top: 18px;
                color: white;
            }
            body {
                margin: 0;
                background-repeat: repeat;
                background: url('resources/bookshelf.jpg');
            }

        </style>
        <div id="bookshelf">

            <c:choose>
                <c:when test="${model.contentType.equals('comics') && param.ruta==null}">

                    <div class="contentType">
                        <a href="<c:url value="/libros.htm" ></c:url>" title="Libros"> 
                            <img  src="resources/folder_libros.png" />
                            <!--<p>Libros</p>-->
                        </a>

                    </div>


                </c:when>
                <c:when test="${model.contentType.equals('libros') && param.ruta==null}">

                    <div class="contentType">
                        <a href="<c:url value="/comics.htm" ></c:url>" title="Comics">  
                            <img src="resources/folder_comics.png" />
                            <!--<p>Comics</p>-->
                        </a>
                    </div>

                </c:when>                
            </c:choose>


            <c:forEach items="${model.directorios}" var="dir">
                <c:set var="trimDir" value="${fn:substring(dir,0,50)}"/>

                <div class="icon iconFolder">                    
                    <a href="<c:url value="" ><c:param name="ruta" value="${param.ruta}/${dir}"/></c:url>" title="<c:out value="${dir}"/>">
                        <img class="folder" style="z-index:1"src="resources/leopard_folder.png" />                        
                    </a>
                    <img class="folder" style="z-index:3" src="resources/leopard_folder_notab.png" />

                    <div style="position:absolute;top:10px;right:-4px;z-index:2;width:70px;height:114px;background-size: 100%;;background:url('<c:url value="/thumb.htm" ><c:param name="ruta" value="${param.ruta}/${dir}"/><c:param name="type" value="${model.contentType}"/></c:url>');background-position:right;-webkit-transform: rotate(45deg); " /></div>
                <a style="z-index:4" href="<c:url value="" ><c:param name="ruta" value="${param.ruta}/${dir}"/></c:url>" title="<c:out value="${dir}"/>"><p style="z-index:4" class="pInside"><c:out value="${trimDir}"/></p> </a>
            </div>

        </c:forEach>
        <c:forEach items="${model.ficheros}" var="fichero">
            <c:set var="trimFichero" value="${fn:substring(fichero.filename,0,50)}"/>

            <div class="icon">
                <c:if test="${fichero.isRead.equals('true')}"><img class="badgeRead" style="z-index:9" src="resources/read.png" /></c:if>
                <c:choose>
                    <c:when test="${fichero.contentType.equals('libros')}">
                        <a href="<c:url value="/libro.htm" ><c:param name="ruta" value="${param.ruta}/${fichero.filename}"/></c:url>" title="<c:out value="${fichero.filename}"/>">
                        </c:when>
                        <c:when test="${fichero.contentType.equals('comics')}">
                            <a href="<c:url value="/comic.htm" ><c:param name="ruta" value="${param.ruta}/${fichero.filename}"/></c:url>" title="<c:out value="${fichero.filename}"/>">
                            </c:when>                            
                        </c:choose>
                        <img style="z-index:2" src="<c:url value="/thumb.htm" ><c:param name="ruta" value="${param.ruta}/${fichero.filename}"/><c:param name="type" value="${fichero.contentType}"/></c:url>" />
                    </a>                           
                    <p style="z-index:1"><c:out value="${trimFichero}"/></p>
            </div>

        </c:forEach>
        <c:set var="baseURL" value="${fn:substringBefore(window.location.pathname,'?')}"/>
        <c:choose>
            <c:when test="${model.parentDirectory==''}">
                <c:choose>
                    <c:when test="${not empty param.ruta}">

                        <div class="icon"><a href="<c:url value="${requestScope['javax.servlet.forward.servlet_path']}" ></c:url>"><img src="resources/back.png" /></a></div>
                            </c:when>
                        </c:choose>
                    </c:when>
                    <c:otherwise>  
                <div class="icon"><a href="<c:url value="" ><c:param name="ruta" value="${model.parentDirectory}"/></c:url>"><img src="resources/back.png" /></a></div>
                    </c:otherwise>
                </c:choose>
    </div>

</body>
</html>
