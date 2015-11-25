<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="icon" href="resources/images/favicon.ico" type="image/x-icon" />
<title>她是我的</title>
<link rel="stylesheet" href="resources/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="resources/slideshow/css/default.css">
<link rel="stylesheet" href="resources/slideshow/css/skitter.styles.css" type="text/css" media="all" />
<link rel="stylesheet" href="resources/css/index.css" type="text/css" />
</head>
<body>
	<div class="container">
		<!-- <div class="row">
			<nav class="navbar navbar-default navbar-fixed-top">
				<div class="container">
					<div class="navber-head">
					</div>
				</div>
			</nav>
		</div> -->
		
		<div class="row">
			<form action="${pageContext.request.contextPath }/view/upload" id="form" method="post" enctype="multipart/form-data">
				<input type="file" multiple="multiple" name="preview_upload_file" id="preview_upload_file">
				<input type="submit" value="Submit" >
			</form>
		</div>
		
		<div class="row">
			<div class="col-md-7" >
				<div class="htmleaf-container">
					<div id="page">
						<div id="content">
						  <div class="border_box">
							<div class="box_skitter box_skitter_large">
							  <ul>
								<li><a href="javascript:void(0);"><img src="resources/slideshow/images/example/001.jpg" class="circles" /></a><div class="label_text"><p>么么哒</p></div></li>
								<li><a href="javascript:void(0);"><img src="resources/slideshow/images/example/002.jpg" class="circlesInside" /></a><div class="label_text"><p>么么哒</p></div></li>
								<li><a href="javascript:void(0);"><img src="resources/slideshow/images/example/003.jpg" class="circlesRotate" /></a><div class="label_text"><p>么么哒</p></div></li>
								<li><a href="javascript:void(0);"><img src="resources/slideshow/images/example/004.jpg" class="cubeShow" /></a><div class="label_text"><p>么么哒</p></div></li> 
							  </ul>
							</div>
						  </div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-5">
				<h3>么么哒
					
				</h3>
			</div>
		</div>
	</div>
	<footer class="footer_container navbar-fixed-bottom">
		<div class="container">
			<div class="row">
				<div class="col-md-12 footer-content">
					憋说话&nbsp;
					<a class="footicon" href="http://wpa.qq.com/msgrd?v=3&uin=396909730&site=qq&menu=yes" target="_blank">
						吻我
					</a>
					
				</div>
			</div>
		</div>
	</footer>
	<span id="mysong">${pageContext.request.contextPath}/resources/audio/xiaomao.mp3</span>
	<script type="text/javascript" src="resources/js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="resources/js/jmp3/jquery.jmp3.js"></script>
	<script type="text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="resources/slideshow/js/jquery.easing.1.3.js"></script>
	<script type="text/javascript" src="resources/slideshow/js/jquery.skitter.min.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#mysong").jmp3({
				width : "0",
				autoplay : "true",
				backcolor : "000000",
				forecolor : "00ff00",
				showdownload : "false",
				repeat : "yes",
				showfilename : "false"
			});
			
			$('.box_skitter_large').skitter({
				theme: 'default',
				dots: true, 
				preview: true,
				numbers_align: 'center'
			});
		});
	</script>
</body>
</html>
