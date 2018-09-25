<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=utf-8" %>

<html>

<head>
    <meta charset="utf-8"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script src="../script/jquery-3.3.1.js"></script>
    <link href="../css/bootstrap.css" rel="stylesheet">
    <script src="../script/bootstrap.js"></script>

    <link href="../css/toastr.min.css" rel="stylesheet">
    <link href="../css/page.css" rel="stylesheet">
</head>

<body>
<div class="logo" onclick="window.location.href='home'">
    <br class="clear"/>
    <h1 class="logo_text">吴文俊人工智能科学技术奖评审系统</h1>
    <h3 class="pull-right">
        参评项目的附件在电脑桌面上
    </h3>
</div>
<div class="line"></div>

<div class="row">
    <h2 id="award_title" class="center-block" style="width:800px;text-align:center">吴文俊人工智能科学技术-自然科学奖</h2>
</div>

<div class="container table-responsive text-center" style="margin-top:40px;">
    <table id="voteTable" class="table table-striped table-bordered" style="font-size:16px;">
        <thead id="voteThead">
        <tr>
            <th class="text-center">序号</th>
            <th class="text-center">奖项</th>
            <th class="text-center">总专家数</th>
            <th class="text-center">文档名称</th>
            <th class="text-center">获奖结果</th>
        </tr>
        </thead>
        <tbody id="voteBody">

        </tbody>
    </table>

    <div>
        <button id="preVote" class="btn btn-primary" type="button" style="width:100px">确认预投票</button>
        <button id="submitVote" class="btn btn-primary" type="button" style="width:100px" disabled>确认投票</button>
    </div>
    <div id="awards_btn" style="margin-top:10px">
        <%--<button class="btn btn-default" type="button" style="width:100px">自然科学奖</button>--%>
        <%--<button class="btn btn-default" type="button" style="width:100px">技术发明奖</button>--%>
        <%--<button class="btn btn-default" type="button" style="width:100px">技术进步奖</button>--%>
    </div>
    <div>
        <button id="showResult" class="btn btn-default" type="button" style="width:110px;margin-top:10px">显示投票结果
        </button>
    </div>

    <div id="rest_div" class="panel panel-default hidden" style="margin-top: 20px">
        <div class="panel-body">
            <h2 id="rest_h2" style="color:red;font-weight: bold">还剩10位专家没有投票</h2>
        </div>
    </div>
</div>

<div style="text-align:center; margin-top:25px;color:#747576; font-size:16px;" class="navbar-fixed-bottom">
    <script type="text/javascript">
        var today = new Date(); //新建一个Date对象
        var year = today.getFullYear();//查询年份
        document.write("中国人工智能学会 @" + year + "Power by BUPT");
    </script>
</div>
</body>
<script src="../script/toastr.min.js"></script>
<script src="../script/vote.js"></script>
<script src="../script/confirm-dialog.js"></script>
</html>