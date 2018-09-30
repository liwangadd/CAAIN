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
<div class="container table-responsive text-center" style="margin-top:40px;">
    <table id="voteTable" class="table table-striped table-bordered" style="font-size:16px;">
        <thead id="voteThead">
        <tr>
            <th class="text-center">序号</th>
            <th class="text-center">奖项</th>
            <th class="text-center">一等奖</th>
            <th class="text-center">二等奖</th>
            <th class="text-center">三等奖</th>
        </tr>
        </thead>
        <tbody id="voteBody">

        </tbody>
    </table>

<div style="text-align:center; margin-top:25px;color:#747576; font-size:16px;" class="navbar-fixed-bottom">
    <script type="text/javascript">
        var today = new Date(); //新建一个Date对象
        var year = today.getFullYear();//查询年份
        document.write("中国人工智能学会 @" + year + "Power by BUPT");
    </script>
</div>
</body>
<script src="../script/toastr.min.js"></script>
<script src="../script/result-view.js"></script>
<script src="../script/confirm-dialog.js"></script>
</html>
