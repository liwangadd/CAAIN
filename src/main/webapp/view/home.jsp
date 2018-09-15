<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=utf-8"%>

<html>

<head>
    <meta charset="utf-8" />
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" href="../css/bootstrap.css" />
    <script src="../script/jquery-3.3.1.js"></script>

    <link rel="stylesheet" href="../css/bootstrap-treeview.min.css" />
    <script src="../script/bootstrap-treeview.min.js"></script>

    <link rel="stylesheet" href="../css/page.css" />
</head>

<body>
    <div>
        <h3 style="float:right; color: red; font-family: 华文新魏; margin-right: 20px">
            欢迎参加人工智能科学技术奖评审
        </h3>
    </div>
    <div class="logo" onclick="window.location.href='home'">
        <br class="clear" />
        <h1 class="logo_text">吴文俊人工智能科学技术奖评审系统</h1>
        <h3 style="float:right;color:red;font-family:华文新魏;margin-right: 20px">
            参评项目的附件在电脑桌面上
        </h3>
    </div>
    <div class="line"></div>
    <div class="left_bar">
        <div id="tree" style="overflow-y:auto;overflow-x:auto;width:100%;height:80%; "></div>
        <div style="height:20%; background-color:#E5ECF9;padding-top:50px;text-align:center">
            <div style="height:30px;font-size:22px; padding-top:8px;">专家请投票</div>
            <a class="btn btn-primary" href="/vote" role="button" style="width:60px;margin-top:20px">进入</a>
        </div>
    </div>
    <object id="pdf_viewer" data="/pdf" type="application/pdf" class="right_bar">

    </object>

    <div style="text-align:center; margin-top:25px;color:#747576; font-size:16px;" class="navbar-fixed-bottom">
        <script type="text/javascript">
            var today = new Date(); //新建一个Date对象
            var year = today.getFullYear();//查询年份
            document.write("中国人工智能学会 @"+year + "Power by BUPT");
        </script>
    </div>
</body>

<script>

</script>
<
<script src="../script/home.js"></script>
</html>