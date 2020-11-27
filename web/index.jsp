<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>拍卖首页</title>
    <link href="css/common.css" rel="stylesheet" type="text/css"/>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>

    <!--导入js文件-->
    <script type="text/javascript" src="js/WebCalendar.js"></script>
</head>

<body>
<div class="wrap">
    <!-- main begin-->
    <div class="sale">
        <h1 class="lf">在线拍卖系统</h1>
        <div class="logout right"><a href="#" title="注销" onclick="location='${pageContext.request.contextPath}/doLogout'">注销</a></div>
    </div>
    <div class="forms">
        <form id="form_query" action="${pageContext.request.contextPath}/queryAuctions" method="post">
            <input id="page" name="pageNum" type="hidden" value="1">
            <label for="name">名称</label>
            <input name="auctionname" value="${condition.auctionname}" type="text" class="nwinput" id="name"/>
            <label for="names">描述</label>
            <input name="auctiondesc" value="${condition.auctiondesc}" type="text" id="names" class="nwinput"/>

            <label for="time">开始时间</label>
            <input name="auctionstarttime"
                   value="<fmt:formatDate value="${condition.auctionstarttime}" pattern="yyyy-MM-dd HH:mm:ss" />"
                   type="text" id="time" class="nwinput" readonly="readonly" onclick="selectDate(this,'yyyy-MM-dd')"/>

            <label for="end-time">结束时间</label>
            <input name="auctionendtime"
                   value="<fmt:formatDate value="${condition.auctionendtime}" pattern="yyyy-MM-dd HH:mm:ss" />"
                   type="text" id="end-time" class="nwinput" readonly="readonly" onclick="selectDate(this,'yyyy-MM-dd')"/>

            <label for="price">起拍价</label>
            <input name="auctionstartprice" value="${condition.auctionstartprice}" type="text" id="price" class="nwinput" />
            <input type="submit"  value="查询" class="spbg buttombg f14  sale-buttom"/>
        </form>



        <c:if test="${sessionScope.user.userisadmin==1}">
            <input type="button" value="发布" onclick="location='${pageContext.request.contextPath}/addAuction.jsp'" class="spbg buttombg f14  sale-buttom buttomb" />
        </c:if>
        <c:if test="${sessionScope.user.userisadmin==0}">
            <input type="button" value="竞拍结果" onclick="location='${pageContext.request.contextPath}/toAuctionResult'" class="spbg buttombg f14  sale-buttom buttomb" />
        </c:if>

    </div>
    <div class="items">
        <ul class="rows even strong">
            <li>名称</li>
            <li class="list-wd">描述</li>
            <li>开始时间</li>
            <li>结束时间</li>
            <li>起拍价</li>
            <li class="borderno">操作</li>
        </ul>

        <c:forEach var="auction" items="${auctionList}" varStatus="state">
                  <!-- 分格颜色-->
            <ul <c:if test="${state.index%2==0}">
                class="rows"
            </c:if>
                    <c:if test="${state.index%2==1}">
                        class="rows even"
                    </c:if>>


                <li><a href="">${auction.auctionname}</a></li>
                <li class="list-wd">${auction.auctiondesc}</li>
                <li>
                    <fmt:formatDate value="${auction.auctionstarttime}" pattern="yyyy-MM-dd "></fmt:formatDate>
                </li>
                <li><fmt:formatDate value="${auction.auctionendtime}" pattern="yyyy-MM-dd "></fmt:formatDate></li>
                <li>${auction.auctionstartprice}</li>
                <li class="borderno red">
                    <c:if test="${sessionScope.user.userisadmin==1}">
                    <a href="${pageContext.request.contextPath}/updateAuction/${auction.auctionid}" title="竞拍" onclick="dele();">修改</a>|
                    <a href="javascript:deleteAuction(${auction.auctionid})" title="竞拍" onclick="abc();">删除</a>
                    </c:if>
                    <c:if test="${sessionScope.user.userisadmin==0}">
                        <a href="${pageContext.request.contextPath}/toAuctionDetail/${auction.auctionid}" title="竞拍" onclick="dele();">竞拍</a>
                    </c:if>
                </li>
            </ul>
        </c:forEach>

<!--     <div class="page">
            【当前第${page.pageNum}页,总共${page.pages},总共${page.total}条记录】
            <a href="${pageContext.request.contextPath}/queryAuctions?pageNum=1">首页</a>
            <a href="${pageContext.request.contextPath}/queryAuctions?pageNum=${page.prePage}">上一页</a>
            <a href="${pageContext.request.contextPath}/queryAuctions?pageNum=${page.nextPage}">下一页</a>
            <a href="${pageContext.request.contextPath}/queryAuctions?pageNum=${page.pages}">尾页</a>
        </div> -->
        <div class="page">
            【当前第${page.pageNum}页,总共${page.pages},总共${page.total}条记录】
            <a href="javascript:jumpPage(1)">首页</a>
            <a href="javascript:jumpPage(${page.prePage})">上一页</a>
            <a href="javascript:jumpPage(${page.nextPage})">下一页</a>
            <a href="javascript:jumpPage(${page.pages})">尾页</a>
        </div>
    </div>
<script type="text/javascript">
    function jumpPage(page_no) {
        document.getElementById("page").value=page_no;

        //手动提交查询表单
        document.getElementById("form_query").submit();
    }
    function deleteAuction(auctionId) {
        if (confirm("是否确定删除该商品")){
            location ="${pageContext.request.contextPath}/deleteAction/"+auctionId;
        }

    }
</script>

    <!-- main end-->
</div>
</body>
</html>
