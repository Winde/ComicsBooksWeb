<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html>
<html>
    <head>       
        <title><c:out value="${model.contentName}"/></title>
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0"/>
        <meta HTTP-EQUIV="CACHE-CONTROL" CONTENT="PUBLIC" />
        <script type="text/javascript" src="resources/jquery.js"></script>
        <script type="text/javascript" src="resources/jquery.touchwipe.js"></script>
        <!-- <script type="text/javascript" src="resources/jquery.mobile-1.3.0/jquery.mobile-1.3.0.js"></script> -->
        <link rel="stylesheet" type="text/css" media="all" href="resources/jquery.mobile-1.3.0/jquery.mobile-1.3.0.css" />
    </head>
    <body style="margin:0px;">
        <style>
            div.mover {
                background-color: transparent;
                -webkit-touch-callout: none;
                -webkit-user-select: none;
                -khtml-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;
                -webkit-tap-highlight-color: rgba(0,0,0,0);
            }
            #pagina {
                width: 100%;
            }
        </style> 
        <script type="text/javascript">       
            var paginaActual = null;            
            var inputObj = [];
            var urlComic ="<c:url value="" ><c:param name="ruta" value="${param.ruta}"/><c:param name="json" value="true"/></c:url>";	
            var needsNotice = <c:choose><c:when test="${model.contentType.equals('libros')}" >true;</c:when><c:otherwise>false;</c:otherwise></c:choose>
            var salir = "<c:url value="${model.exit}" ><c:if test="${!model.parentDirectory.equals('')}"><c:param name="ruta" value="${model.parentDirectory}"/></c:if></c:url>";
            var salirYLeer="<c:url value="/read.htm"><c:param name="ruta" value="${param.ruta}"/><c:param name="read" value="true" /><c:param name="type" value="${model.contentType}"/></c:url>";	             
            var positionTop=false;
            var positionBottom=false; 
            var storedPositionTop=false;
            var storedPositionBottom=false;            
            var imagenes = {};
            </script>






        <c:set var="img64" value="data:image/${model.extension};base64,${model.imagen}" />        
        <div id="paginaDiv" style="text-align: center">
            <div id="loading" style="width: 100%; height: 100%;position:fixed; top:0px; left:0px;background-image:url('resources/ajax-loader.gif');background-repeat:no-repeat;background-position:center;display:none;"></div> 
            <div id="bgFaderLeft" style="width: 35%; height: 100%; position:fixed; top:0px; left:0px; display:none;background-image:url('resources/arrow_left.png');background-repeat:no-repeat;background-position:center;background-size: contain; max-width: 256px;"></div>        
            <div id="bgFaderRight" style="width: 35%; height: 100%; position:fixed; top:0px; right:0px; display:none;background-image:url('resources/arrow_right.png');background-repeat:no-repeat;background-position:center;background-size: contain; max-width: 256px;"></div>
            <div class="mover" style="width: 100%; height: 10%; position:fixed; top:0px; left:0px; " onclick="window.location=salir">&nbsp;</div>
            <div class="mover" style="width: 35%; height: 100%; position:fixed; top:10%; left:0px; " onclick="getPrevious()">&nbsp;</div> 
            <div class="mover" style="width: 35%; height: 100%; position:fixed; top:10%; right:0px; " onclick="getNext()">&nbsp;</div> 

            <img id="pagina" src="<c:out value="${img64}" />" style="margin: 0 auto 0 auto" />
        </div>
        <div style="text-align:center">
            
            <form:form  data-ajax="false" method="post" modelAttribute="pagina">
                <div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c">
                     <input type="text" name="numero" id="pageInput" class="ui-input-text ui-body-c" value="" placeholder="<c:out value="${pagina.numero}"/> of <c:out value="${model.totalPages}"/>" totalPages="${model.totalPages}" current="${pagina.numero}">   
                </div>
                
            </form:form>
        </div>
        <script type="text/javascript">
            
            function setRead(pagina) {
                var readPageURL = "<c:url value="/readpage.htm"><c:param name="ruta" value="${param.ruta}" /><c:param name="type" value="${model.contentType}" /></c:url>" + "&pagina="+pagina;
                $.getJSON(readPageURL, null);
            }
            
            function flashArrowRight() {
                
                $('#bgFaderRight').stop().fadeIn("slow", function(){
                    $('#bgFaderRight').fadeOut('slow',function(){
                        $('#bgFaderRight').stop(true,true);
                    });
                });
    
            }        
            
            function flashArrowLeft() {
                $('#bgFaderLeft').fadeIn("slow", function(){
                    $('#bgFaderLeft').fadeOut('slow',function(){
                        $('#bgFaderLeft').stop(true,true);
                    });
                });

            }
            
            function prefetch(inc) {
                var pagina = paginaActual+inc;
                if ((imagenes[pagina] == undefined) || (imagenes[pagina] == 'clean')) {
                    delete imagenes[pagina];
                    imagenes[pagina] = 'prefetch';
                    $.getJSON(urlComic+'&pagina='+pagina, function(data) {      
                        if ((imagenes[pagina] != undefined) && (imagenes[pagina]!='prefetch')) {
				
                        } else {
                            if ((data['imagen'] === null) && data['pagina'] == pagina) {
                                imagenes[pagina] = null;
                            } else {
                                if (data['pagina']!=pagina) {
                                    delete imagenes[pagina];
                                } else {
                                    var imagen = "data:image/"+data['extension']+";base64,"+data['imagen'];                                       
                                    imagenes[pagina] = imagen;                          
                                    if (pagina == paginaActual+1) {
                                        flashArrowRight();
                                    }
                                    if (pagina == paginaActual-1) {
                                       flashArrowLeft();                                     
                                    }
                                    if (inc<3){
                                        prefetch(inc+1);
                                    }
                                    
                                }
                            }
                        }
                    });
                }
            }

            function getNext() {
                getNextWithPosition(1);
            }

            function changeInputValue(value) {
                if( inputObj.length>0 ) { 
                    // Update the value 
                    inputObj.attr('current',value);
                    inputObj.attr('placeholder',value+" of " + inputObj.attr('totalpages'));
                }                
            }

            function getNextWithPosition(position) {
                var pagina = paginaActual+1;
                $('#loading').show();
                $('#loading').fadeOut(1000);
                if (imagenes[pagina] ===null){
                    setRead(0);
                    window.location=salirYLeer;
                } else{ 
                    if ((imagenes[pagina] != undefined) && (imagenes[pagina]!='clean') && (imagenes[pagina]!='prefetch')) {  
                        $('#pagina').attr('src',imagenes[pagina]).one("load", function(){
                            if ((imagenes[pagina+1] == undefined) || (imagenes[pagina+1] == 'clean') && (imagenes[pagina+1] != 'prefetch')){
                            } else {   
                                window.scrollTo(0,1);           
                                flashArrowRight();
                            }   
                        });
                        //$('#pagina')[0].src=imagenes[pagina];        
                        
                        paginaActual++;
                        setRead(paginaActual);                                     
                        changeInputValue(paginaActual+1);
                       
                        if ((imagenes[pagina+1] == undefined) || (imagenes[pagina+1] == 'clean') && (imagenes[pagina+1] != 'prefetch')){
                            prefetch(1);         
                        }
                        if ((imagenes[pagina+2] == undefined) || (imagenes[pagina+2] == 'clean') && (imagenes[pagina+2] != 'prefetch')){
                            prefetch(2);
                        }
                        if ((imagenes[pagina+3] == undefined) || (imagenes[pagina+3] == 'clean') && (imagenes[pagina+3] != 'prefetch')){
                            prefetch(3);
                        }
                    } else {
                        if (imagenes[pagina]!='prefetch')  {
                            prefetch(1);
                        }
                    }                              
                    
                    /*    if ((imagenes[pagina+2] == undefined) || (imagenes[pagina+2] == 'clean')){
                        prefetch(2);
                    }
                     */
                    if (imagenes[pagina-2] != undefined) {
                        delete imagenes[pagina-2];
                        imagenes[pagina-2]='clean';                      
                    }
                    
                } 
            }
            
            function getPrevious() {
                getPreviousWithPosition(1);
            }
            
            function getPreviousWithPosition(position) {
                var pagina = paginaActual-1; 
                $('#loading').show();
                $('#loading').fadeOut(1000);
                if (pagina < 0) {
                    window.location=salir;
                } else {
                    if ((imagenes[pagina] != undefined) && (imagenes[pagina]!='clean')) {
                        $('#pagina')[0].src=imagenes[pagina];
                        paginaActual--;
                        window.scrollTo(0,position);
                        changeInputValue(paginaActual+1); 
                    }
                    if ((imagenes[pagina-1] == undefined) || (imagenes[pagina-1] == 'clean')){
                        prefetch(-1);
                    }  else {
                        $('#bgFaderLeft').fadeIn('slow'); 
                        $('#bgFaderLeft').fadeOut('slow');
                       
                    }
                    if (imagenes[pagina+2] != undefined){
                        imagenes[pagina+2]='clean';                      
                    }
                    
                }
            }

            function storePositionValues() {
                storedPositionTop=positionTop;
                storedPositionBottom=positionBottom;  
            }


            function handleArrowKeys(evt) {
                evt = (evt) ? evt : ((window.event) ? event : null);
                if (evt) {
                    switch (evt.keyCode) {
                        case 37: //Left Arrow
                            getPrevious();
                            break;    
                            case 39: //Right Arrow
                            getNext();
                            break; 
                        case 33: //Page Up
                            if (positionTop && storedPositionTop) {
                                getPreviousWithPosition(document.body.offsetHeight);
                            }
                            break;
                        case 34: //Page Down
                            if (positionBottom && storedPositionBottom) {
                                getNext();
                            }
                            break;                                                        
                    }
                }
                return false;
            }

       
           
            document.onkeydown = storePositionValues;
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
                inputObj = jQuery("#pageInput" );                 
                paginaActual= <c:choose><c:when test="${param.pagina!=null}"><c:out value="${param.pagina}"/></c:when><c:otherwise>inputObj.attr('current')-1</c:otherwise></c:choose>; 
                imagenes[paginaActual] = $('#pagina')[0].src;


                setTimeout('prefetch(1)',500);
                if ( $(document).scrollTop()  <= 1) {
                    positionTop=true;
                }
                if (  document.documentElement.clientHeight + 
                    $(document).scrollTop() >= document.body.offsetHeight )
                { 
                    // Display alert or whatever you want to do when you're 
                    //   at the bottom of the page. 
                    positionBottom=true;
                }
                
                $(window).scroll(function() {
                    positionTop=false;
                    positionBottom=false;
                    if ( $(document).scrollTop()  <= 1) {
                        positionTop=true;
                    }
                    
                    if (  document.documentElement.clientHeight + 
                        $(document).scrollTop() >= document.body.offsetHeight )
                    { 
                        // Display alert or whatever you want to do when you're 
                        //   at the bottom of the page. 
                        positionBottom=true;
                    }
                });
                
                $("#paginaDiv").touchwipe({
                    wipeLeft: function() { getNext(); },
                    wipeRight: function() { getPrevious(); },
                    min_move_x: 20,
                    min_move_y: 20,
                    preventDefaultEvents: false
                });
            }
            
            
        </script>

    </body>
</html>
