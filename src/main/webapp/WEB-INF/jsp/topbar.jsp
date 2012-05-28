<%-- 
    Document   : topbar
    Created on : May 28, 2012, 3:54:44 PM
    Author     : Winde
--%>

<style type="text/css">
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
<c:choose>
    <c:when test="${model.contentType!=null && (model.contentType.contains('comic') || model.contentType.contains('libro'))}">
        <c:set var="type" value="${model.contentType}" />
    </c:when>
    <c:otherwise>
        <c:set var ="type" value="comics" />
    </c:otherwise>
</c:choose>

    <div id="topBar">
    <ul>
        <li>
            <a href="<c:url value="/changepath.htm" ><c:if test="${param.ruta!=null && !''.equals(param.ruta)}" ><c:param name="ruta" value="${param.ruta}" /></c:if><c:if test="${type!=null}"><c:param name="type" value="${type}" /></c:if></c:url>">
                    Change content paths</a>
        </li>
        
        <li>
            <a href="<c:url value="/read.htm"><c:param name="ruta" value="${param.ruta}" /><c:param name="type" value="${type}" /><c:param name="read" value="true" /></c:url>">
                Mark all as read
            </a>
        </li>
        <li>
            <a href="<c:url value="/read.htm"><c:param name="ruta" value="${param.ruta}" /><c:param name="type" value="${type}" /><c:param name="read" value="false" /></c:url>">
                Mark all as unread
            </a>   
        </li>                
        <li>
            <a class="<c:if test="${'true'.equals(model.keepLastRead)}">active</c:if>" href="<c:url value="/lastreadfunction.htm">
                    <c:if test="${param.ruta!=null}">
                        <c:param name="ruta" value="${param.ruta}" />
                    </c:if>
                    <c:param name="type" value="${type}" />                                                
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
                    <c:param name="type" value="${type}" />
                    <c:param name="lastread" value="wipe" />
                    </c:url>
                    ">
                    Remove bookmarks
                </a>
            </li>
        </c:if>
    </ul>
</div>
