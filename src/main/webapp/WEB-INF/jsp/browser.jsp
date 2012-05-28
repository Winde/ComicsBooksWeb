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
                top: 11px;
                left: 30px
            }            
            .icon>.folder {
                width: 90px;
                height: 128px;
                position: absolute;
                top: 11px;
                left: 30px
            }
            .icon>.badgeRead {
                width: 65px;
                height:65px;
                position: absolute;
                top: -1px;
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



            .readPageNumber {
                z-index:11;
                position:absolute;
                color:white;
                top:105px;
                left:85px;
                background: url('resources/badge.png');
                background-size: 40px 40px;
                height:30px;
                width:40px; 
                padding-top:10px;
                font-weight:bold;
                text-align:center;
            }

            body {
                margin: 0;
                background-repeat: repeat;
                background: url('resources/bookshelf.jpg');
                background-position: 0 40px;
            }

            #topBar     {
                width:100%; 
                height:40px;
                float: left;
                padding: 0;
                margin-bottom: 2px;
                background-image: -moz-linear-gradient(top, #cacaca, #848484);
                background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#cacaca), to(#848484));
            }
            #topBar ul  {
                padding: 0;
                margin: 0;
                float: left;
            }
            #topBar li  {
                float: left;
                list-style: none;
                background: none;
            }
            #topBar a { outline: none; }
            #topBar li a:link, #menu li a:visited {
                font-family: Arial, Helvetica, sans-serif;
                font-size: 14px;
                font-weight: bold;
                display: block;
                color: #262626;
                text-decoration: none;
                text-transform: capitalize;
                padding: 11.5px 28px;
            }

            #topBar li a:link, #topBar li a:visited {
                font-family: Arial, Helvetica, sans-serif;
                font-size: 14px;
                font-weight: bold;
                display: block;
                color: #262626;
                padding: 11.5px 28px;
                text-decoration: none;
                text-transform: capitalize;

                /* CSS3 Text Shadow */
                text-shadow: 0px 1px 1px #fff;

                /* CSS3 Background Gradient */
                background-image: -moz-linear-gradient(top, #cacaca, #848484);
                background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#cacaca), to(#848484));
            }

            #topBar li:first-child a {
                /* Rounded Corners */
                /*-moz-border-radius-topleft: 10px;
                -moz-border-radius-bottomleft: 10px;
                -webkit-border-top-left-radius: 10px;
                -webkit-border-bottom-left-radius: 10px;
                */
            }
            #topBar li:last-child a {
                /* Rounded Corners */
                /*-moz-border-radius-topright: 10px;
                -moz-border-radius-bottomright: 10px;
                -webkit-border-top-right-radius: 10px;
                -webkit-border-bottom-right-radius: 10px;
                */
            }

            #topBar     {
                float: left;
                padding: 0;
                margin: 0;

                /* Box Shadow */
                box-shadow: 0 1px 0 #000;
                -moz-box-shadow: 0 1px 0 #000;
                -webkit-box-shadow: 0 1px 0 #000;

                /* Rounded Corners */
                /*-moz-border-radius-topleft: 10px;
                -moz-border-radius-bottomleft: 10px;
                -webkit-border-top-left-radius: 10px;
                -webkit-border-bottom-left-radius: 10px;
                */
                /* Rounded Corners */
                /*-moz-border-radius-topright: 10px;
                -moz-border-radius-bottomright: 10px;
                -webkit-border-top-right-radius: 10px;
                -webkit-border-bottom-right-radius: 10px;
                */
            }

            #topBar li  {
                float: left;
                list-style: none;
                background: none;
                border-right: #000 solid 1px;
            }

            #topBar ul  {
                /*border-top: #f3f3f3 1px solid;*/
                padding: 0;
                margin: 0;
                float: left;

                /* Background Gradient */
                background-image: -moz-linear-gradient(top, #b4b4b4, #707070);
                background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#b4b4b4), to(#707070));
            }

            #topBar li a:hover    {
                cursor: pointer;
                color: #fff;
                text-shadow: 0px -1px 1px #000;

                /* Background Gradient */
                background-image: -moz-linear-gradient(top, #929292, #545454);
                background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#929292), to(#545454));
            }

            a.active:link, a.active:active, a.active:visited	{
                color: #fff !important;
                text-shadow: 0px -1px 1px #000!important;
                background-image: -moz-linear-gradient(top, #444, #666)!important;
                background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#444), to(#666))!important;

                /* Box Shadow */
                box-shadow: inset 0 0 10px #000;
                -moz-box-shadow: inset 0 0 10px #000;
                -webkit-box-shadow: inset 0 0 10px #000;
            }


            /*
                        #topBar>a>.topButton {
                            width:150px;
                            height:20px;
                            margin-top:5px;
                            margin-right:20px;
                            padding-top: 5px;
                            padding-bottom: 5px;
                            outline: none;
                            cursor: pointer;
                            display: inline-block;
                            text-decoration: none;
                            text-align: center;
                            text-shadow: 0 1px 1px rgba(0,0,0,.3);
                            -webkit-border-radius: .5em; 
                            -moz-border-radius: .5em;
                            border: white solid 1px;
                            -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.2);
                            -moz-box-shadow: 0 1px 2px rgba(0,0,0,.2);
                            box-shadow: 0 1px 2px rgba(0,0,0,.2);
                            color: #fef4e9;
                            border: solid 1px #da7c0c;
                            background: #f78d1d;
                            background: -webkit-gradient(linear, left top, left bottom, from(#faa51a), to(#f47a20));
                            background: -moz-linear-gradient(top,  #faa51a,  #f47a20);
                            filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#faa51a', endColorstr='#f47a20');
                        }
                        #topBar>a>.topButton:hover {
                            background: #f47c20;
                            background: -webkit-gradient(linear, left top, left bottom, from(#f88e11), to(#f06015));
                            background: -moz-linear-gradient(top,  #f88e11,  #f06015);
                            filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#f88e11', endColorstr='#f06015');
                        }  
            
                        #topBar>a>.topButton:active {
                            color: #fcd3a5;
                            background: -webkit-gradient(linear, left top, left bottom, from(#f47a20), to(#faa51a));
                            background: -moz-linear-gradient(top,  #f47a20,  #faa51a);
                            filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#f47a20', endColorstr='#faa51a');
                        }
            */
        </style>
        <div id="topBar">
            <ul>
                <li>
                    <a href="<c:url value="/read.htm"><c:param name="ruta" value="${param.ruta}" /><c:param name="type" value="${model.contentType}" /><c:param name="read" value="true" /></c:url>">
                        Mark all as read
                    </a>
                </li>
                <li>
                    <a href="<c:url value="/read.htm"><c:param name="ruta" value="${param.ruta}" /><c:param name="type" value="${model.contentType}" /><c:param name="read" value="false" /></c:url>">
                        Mark all as unread
                    </a>   
                </li>                
                <li>
                    <a class="<c:if test="${'true'.equals(model.keepLastRead)}">active</c:if>" href="<c:url value="/lastreadfunction.htm">
                           <c:if test="${param.ruta!=null}">
                               <c:param name="ruta" value="${param.ruta}" />
                           </c:if>
                           <c:param name="type" value="${model.contentType}" />                                                
                           <c:choose>
                               <c:when test="${'true'.equals(model.keepLastRead)}">
                                   <c:param name="lastread" value="false" />
                               </c:when>
                               <c:otherwise>
                                   <c:param name="lastread" value="true" />
                               </c:otherwise>
                           </c:choose>
                       </c:url>">
                        Save last read page 
                    </a>   
                </li>
                <c:if test="${'true'.equals(model.keepLastRead)}">
                    <li>
                        <a href="<c:url value="/lastreadfunction.htm">
                               <c:if test="${param.ruta!=null}">
                                   <c:param name="ruta" value="${param.ruta}" />
                               </c:if>
                               <c:param name="type" value="${model.contentType}" />
                               <c:param name="lastread" value="wipe" />
                           </c:url>
                           ">
                            Remove bookmarks
                        </a>
                    </li>
                </c:if>
            </ul>
        </div>

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
                        <c:if test="${fichero.lastReadPage!=null && 'true'.equals(model.keepLastRead) && fichero.lastReadPage>0}"><div class="readPageNumber">${fichero.lastReadPage+1}</div></c:if>                        
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
