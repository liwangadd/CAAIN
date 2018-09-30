<%--
  Created by IntelliJ IDEA.
  User: windylee
  Date: 2018/3/16
  Time: 10:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <meta charset="utf-8" />
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <script src="../script/jquery-3.3.1.js"></script>
    <link href="../css/bootstrap.css" rel="stylesheet">
    <script src="../script/bootstrap.js"></script>

    <link href="../css/toastr.min.css" rel="stylesheet">
    <link href="../css/page.css" rel="stylesheet">
</head>

<body>
<div class="logo" onclick="window.location.href='home'">
    <br class="clear" />
    <h1 class="logo_text">吴文俊人工智能科学技术奖评审系统</h1>
</div>
<div class="line"></div>

<div class="container text-center" style="margin-top: 40px">
    <div id="pdf_btn_group">
        <%--<button class="btn btn-primary">自然科学奖PDF</button>--%>
        <%--<button class="btn btn-primary">科技发明奖PDF</button>--%>
        <%--<button class="btn btn-primary">技术发明奖PDF</button>--%>
    </div>

    <div id="vote_btn_group" style="margin-top: 20px">

    </div>

    <div id="vote_result_group" style="margin-top: 20px">

    </div>

    <div>
        <button id="clear_vote" class="btn btn-default" style="margin-top: 40px">清空投票结果</button>
    </div>
</div>

</body>
<script src="../script/toastr.min.js"></script>
<script src="../script/admin.js"></script>
</html>
