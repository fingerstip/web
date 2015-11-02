<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="icon" href="resources/images/favicon.ico" type="image/x-icon" />
<title></title>

<link href="coolShow.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="coolShow.js"></script>

</head>

<body style = "background:#ecf0f1;">
<br><br><br>
<center><p style = "color:#334960">点击图标进行展示</p></center>
<div class = "main">
	<div id = "coolShow"></div>
	<div id = "handBar"></div>
</div>

<script type="text/javascript">
/*定义需要展示的图片以及图片的展示时间*/
$(document).ready(function() { 
	$('#coolShow').coolShow({
		imgSrc:['images/1.png','images/2.png','images/3.png'],
		speed:40
	});
});
</script>

</body>
</html>