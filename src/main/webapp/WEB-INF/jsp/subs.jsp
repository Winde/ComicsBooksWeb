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
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <title>
            <c:choose><c:when test="${model.contentType.equals('libros')}">Libros: </c:when><c:when test="${model.contentType.equals('comics')}">Comics: </c:when></c:choose>
            <c:choose><c:when test="${param.ruta!=null}"><c:out value="${pathNice}" /></c:when></c:choose>
                </title>
                <script type="text/javascript" src="resources/jquery.js"></script>
            </head>
            <body>
                <style type="text/css">
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
                    .thumb  {
                        -moz-box-shadow: 3px 3px 4px #000;
                        -webkit-box-shadow: 3px 3px 4px #000;
                        box-shadow: 3px 3px 4px #000;
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

                    .icon .title {
                        position: absolute;
                        display: none;
                        z-index: 999;
                        left: 5%;
                        top: 25%;
                        width: 100%;
                        background-color: white;
                        border: 2px solid #ccc;
                        -moz-box-shadow: 2px 2px #888;
                        -webkit-box-shadow: 2px 2px #888;
                        box-shadow: 2px 2px #888;
                        font-family: Arial;
                        font-size: 12px;                         
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
                    
                    #bookshelf {
                        padding-top: 160px;
                        position: relative;                        
                    }
                    
                    #bookshelf .search{
                        position: absolute;
                        top: 50px;
                        right: 50px;
                        width: 50%;                        
                    }
                    
                    #bookshelf .search .container {
                    	position: relative;
                    	overflow: hidden;
                    }
                    
                    #bookshelf .search input{
                        width: 100%;
                        /* height: 34px; */
                        padding: 6px 12px;
                        font-size: 14px;
                        line-height: 1.42857143;
                        color: #555;
                        background-color: #fff;
                        background-image: none;
                        border: 1px solid #ccc;
                        border-radius: 4px;
                        -webkit-box-shadow: inset 2px 3px 5px #CCC;
                        box-shadow: inset 2px 3px 5px #CCC;
                        -webkit-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
                        transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
                    }
                    
                     #bookshelf .search .btn{
                        position: absolute; 
                                           
                        top: 0px;
                        cursor:pointer;
                        /*width: 20px;*/
                        text-align: center;
                        /* height: 30px; */
                        background-color: #2489ce;
                        color: white;
                        display: inline-block;
                        vertical-align: top;
                        padding: 5.5px 12px;
                        /* font-size: 14px; */
                        /* line-height: 1.42857143; */
                        /*margin-top: 2px;*/
                        
                        font-weight: bold;
                        font-family: cursive;
                       
                        /* border: 2px solid grey; */
                        /* font-family: Arial, Helvetica, sans-serif; */
                        /* font-size: 14px; */
                        font-weight: bold;
                        /* display: block; */
                        color: #262626;
                        text-decoration: none;
                        text-transform: capitalize;
                        text-shadow: 0px 1px 1px #fff;
                        background-image: -moz-linear-gradient(top, #cacaca, #848484);
                        background-image: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#cacaca), to(#848484));
                     }
                     
                     #bookshelf .search .btn.btn-right {
                      	right: 0px;
                     	border-radius: 0px 4px 4px 0px;
                     	border-left: 1px solid black;
                     }
                     
                     #bookshelf .search .btn.btn-left {
                      	left: 0px;   
                     	border-radius: 4px 0px 0px 4px;
                     	width: 40px;
                     	border-right: 1px solid black;
                     }
                    
                     #bookshelf .search input{
                     	 padding-left: 70px;
                     }
                    
                    #bookshelf .search input:focus{
                           /*box-shadow: 0 0 5px rgba(81, 203, 238, 1);*/
                           /*padding: 3px 0px 3px 3px;*/
                           /*margin: 5px 1px 3px 0px;*/
                           border: 1px solid rgba(81, 203, 238, 1);
                    }
                    
                    
                </style>
                
                
                
                
        <%@ include file="/WEB-INF/jsp/topbar.jsp" %>

        <div id="bookshelf">
            <div class="search">  
            	
            </div>
            <c:out value="${referer}" />
            <c:choose>
                <c:when test="${referer.contains('comics')}">

                    <div class="contentType">
                        <a href="<c:url value="/libros.htm" ></c:url>" title="Libros"> 
                                <img  src="resources/folder_libros.png" />
                                <!--<p>Libros</p>-->
                            </a>

                        </div>


                </c:when>
                <c:when test="${referer.contains('libros') && param.ruta==null}">

                    <div class="contentType">
                        <a href="<c:url value="/comics.htm" ></c:url>" title="Comics">  
                                <img src="resources/folder_comics.png" />
                                <!--<p>Comics</p>-->
                            </a>
                        </div>

               </c:when>
               <c:otherwise>
               		<div class="contentType">
                        <a href="<c:url value="/comics.htm" ></c:url>" title="Comics">  
                                <img src="resources/folder_comics.png" />
                                <!--<p>Comics</p>-->
                            </a>
                     </div>
               </c:otherwise>                
            </c:choose>            
            <c:forEach items="${model.subs}" var="sub">
                
                <div class="icon iconFolder" data-sub="<c:out value="${sub}"/>">                    
                    <a href="#" class="subDelButton" title="<c:out value="${sub}"/>">
                            <img class="folder" style="z-index:1"src="resources/leopard_folder.png" />                        
                        </a>
                        <img class="folder" style="z-index:3" src="resources/leopard_folder_notab.png" />
                        
                	<a style="z-index:4" href="#" title="<c:out value="${sub}"/>"><p style="z-index:4" class="pInside filename"><c:out value="${sub}"/></p> </a>
                </div>

        </c:forEach>
        <c:set var="baseURL" value="${fn:substringBefore(window.location.pathname,'?')}"/>
    </div>
    <script>(function(a,b,c){if(c in b&&b[c]){var d,e=a.location,f=/^(a|html)$/i;a.addEventListener("click",function(a){d=a.target;while(!f.test(d.nodeName))d=d.parentNode;"href"in d&&(d.href.indexOf("http")||~d.href.indexOf(e.host))&&(a.preventDefault(),e.href=d.href)},!1)}})(document,window.navigator,"standalone")</script>

	<script>
		jQuery(document).ready(function(){
			jQuery('.icon.iconFolder').click(function(){
				var button = jQuery(this);
				var sub = jQuery(this).attr('data-sub');
				console.log(jQuery(this));
				console.log(button);
				console.log(sub);
				if (sub!=null && sub!=undefined) {
					jQuery.ajax({
						  url: "removesub.json",
						  data: "searchString=" + sub,
						  dataType: "json",
						  success: function(data){
							  if (data.result == "success"){
								  button.remove();
							  }
						  },
						  error : function(){
							  alert("error");
						  }
					});					
				}
				
			});
			
		});
	
	</script>
</body>
</html>
