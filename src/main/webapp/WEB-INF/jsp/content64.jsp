<%-- 
    Document   : comic
    Created on : May 20, 2012, 7:09:24 PM
    Author     : Winde
--%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html>
<html>
    <head>       
        <title><c:out value="${model.contentName}"/></title>
        <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="PUBLIC">
        <script type="text/javascript" src="resources/jquery.js"></script>
    </head>
    <body style="margin:0px;">
        <style>
            div:selection {
                background-color: transparent;
            }

        </style> 
        <script type="text/javascript">
            var urlComic ='<c:url value="" ><c:param name="ruta" value="${param.ruta}"/><c:param name="json" value="true"/></c:url>';	
            var paginaActual= <c:out value="${param.pagina}"/>; 
            var needsNotice = <c:choose><c:when test="${model.contentType.equals('libros')}" >true;</c:when><c:otherwise>false;</c:otherwise></c:choose>
            var salir = "<c:url value="${model.exit}" ><c:if test="${!model.parentDirectory.equals('')}"><c:param name="ruta" value="${model.parentDirectory}"/></c:if></c:url>";

            var imagenes = {}; 
        </script>


        <div style="width: 100%; height: 10%; position:fixed; top:0px; left:0px; " onclick="window.location=salir">&nbsp;</div>
        <div id="bgFaderLeft" style="width: 35%; height: 100%; position:fixed; top:0px; left:0px; display:none;background-image:url('resources/arrow_left.png');background-repeat:no-repeat;background-position:center; "></div><div style="width: 35%; height: 100%; position:fixed; top:10%; left:0px; " onclick="getPrevious()">&nbsp;</div>
        <div id="bgFaderRight" style="width: 35%; height: 100%; position:fixed; top:0px; right:0px; display:none;background-image:url('resources/arrow_right.png');background-repeat:no-repeat;background-position:center; "></div><div style="width: 35%; height: 100%; position:fixed; top:10%; right:0px; " onclick="getNext()">&nbsp;</div>
        <c:set var="img64" value="data:image/${model.extension};base64,${model.imagen}" />        
        <div style="text-align: center">

            <img id="pagina" src="<c:out value="${img64}" />" style="margin: 0 auto 0 auto" />
        </div>
        <div style="text-align:center">
            <form:form  method="post" modelAttribute="pagina">
                <form:input path="numero" cssStyle="width:25px" id="pageInput"></form:input> of <c:out value="${model.totalPages}" />
                <input type="submit" value="Go" />
            </form:form>
        </div>
        <script type="text/javascript">
            function prefetch(inc) {
                var pagina = paginaActual+inc;
                if ((imagenes[pagina] == undefined) || (imagenes[pagina] == 'clean')) {
                    delete imagenes[pagina];
                    console.log("Actual "+paginaActual + " Pidiento prefetch "+pagina);

                    $.getJSON(urlComic+'&pagina='+pagina, function(data) {
                        if (imagenes[pagina] != undefined) {
					

                        } else {
                            if (data['imagen'] == null) {
                                imagenes[pagina] = null;
                            } else {
                                imagenes[pagina] = "data:image/"+data['extension']+";base64,"+data['imagen'];
                                console.log("Actual "+paginaActual + " Prefetch "+pagina);
                                if (pagina == paginaActual+1) {
                                    $('#bgFaderRight').fadeIn('slow'); 
                                    $('#bgFaderRight').fadeOut('slow');
                                }
                                if (pagina == paginaActual-1) {
                                    $('#bgFaderLeft').fadeIn('slow'); 
                                    $('#bgFaderLeft').fadeOut('slow');
                                }
                            }
                        }
                    });
                }
            }

            function getNext() {
                var pagina = paginaActual+1;

                if (imagenes[pagina] != undefined) {
                    // Tenemos esa imagen
                    if (imagenes[pagina] ==null) {
                        window.location=salir;
                    } else {
                        $('#pagina')[0].src=imagenes[pagina];
                        paginaActual++;
                        setTimeout(200,window.scrollTo(0,1));
                        var inputObj = document.getElementById( "pageInput" ); 
                        if( inputObj ) { 
                            // Update the value 
                            inputObj.value = parseInt(inputObj.value)+1; 
                        }				    
                                
                        if ((imagenes[pagina+1] == undefined) || (imagenes[pagina+1] == 'clean')){
                            prefetch(1);
                        } else {
                            $('#bgFaderRight').fadeIn('slow'); 
                            $('#bgFaderRight').fadeOut('slow');
                        }
                        if ((imagenes[pagina+2] == undefined) || (imagenes[pagina+2] == 'clean')){
                            prefetch(2);
                        }

                        if (imagenes[pagina-2] != undefined) {
                            delete imagenes[pagina-2];
                            imagenes[pagina-2]='clean';
                            console.log("Actual "+paginaActual +" Clean "+(pagina-2));
                        }
                    }
                } 
            }
            
            function getPrevious() {
                var pagina = paginaActual-1;        
                if (pagina < 0) {
                    window.location=salir;
                } else {
                    if (imagenes[pagina] != undefined) {
                        $('#pagina')[0].src=imagenes[pagina];
                        paginaActual--;
                        setTimeout(200,window.scrollTo(0,1));
                        var inputObj = document.getElementById( "pageInput" ); 
                        if( inputObj ) { 
                            // Update the value 
                            inputObj.value = parseInt(inputObj.value)-1; 
                        }  

                        if ((imagenes[pagina-1] == undefined) || (imagenes[pagina-1] == 'clean')){
                            prefetch(-1);
                        }  else {
                            $('#bgFaderLeft').fadeIn('slow'); 
                            $('#bgFaderLeft').fadeOut('slow');
                        }
                        if (imagenes[pagina+3] != undefined){
                            imagenes[pagina+3]='clean';
                            console.log("Actual "+paginaActual +" Clean "+(pagina+3));
                        }
                    }
                }
            }


            function handleArrowKeys(evt) {
                evt = (evt) ? evt : ((window.event) ? event : null);
                if (evt) {
                    switch (evt.keyCode) {
                        case 37:
                            window.location= previousPage;
                            break;    
                        case 39:
                            window.location= nextPage;
                            break;      
                    }
                }
            }


            imagenes[paginaActual] = $('#pagina')[0].src;

            document.onkeyup = handleArrowKeys;
            function ajustar() {
                var viewportwidth;
                var viewportheight;
                // the more standards compliant browsers (mozilla/netscape/opera/IE7) use window.innerWidth and window.innerHeight  
                if (typeof window.innerWidth != 'undefined')
                {
                    viewportwidth = window.innerWidth,
                    viewportheight = window.innerHeight
                }
                // IE6 in standards compliant mode (i.e. with a valid doctype as the first line in the document)
                else if (typeof document.documentElement != 'undefined'
                    && typeof document.documentElement.clientWidth !=
                    'undefined' && document.documentElement.clientWidth != 0)
                {
                    viewportwidth = document.documentElement.clientWidth,
                    viewportheight = document.documentElement.clientHeight
                }
                // older versions of IE 
                else
                {
                    viewportwidth = document.getElementsByTagName('body')[0].clientWidth,
                    viewportheight = document.getElementsByTagName('body')[0].clientHeight
                }
                document.getElementById("pagina").style.width = (viewportwidth-20)+"px";
            }

            window.onorientationchange=ajustar;
            window.onresize=ajustar;

            document.body.onload=function() {
                ajustar();
                setTimeout(500,prefetch(1));
            }

            
        </script>

    </body>
</html>
