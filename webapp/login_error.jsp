<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログインエラー</title>
<!-- Bootstrap CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<style>
    body {
        background-color: #f8d7da;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        color: #721c24;
    }
    .card {
        width: 350px;
        padding: 20px;
        border: 1px solid #f5c6cb;
        border-radius: 5px;
        background-color: #f8d7da;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        text-align: center;
    }
    .card-title {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 20px;
    }
    a {
        color: #004085;
        text-decoration: underline;
    }
</style>
</head>
<body>

<div class="card">
    <h2 class="card-title">ログインエラー</h2>
    <p>ユーザIDまたはパスワードが違います。</p>
    <a href="login.jsp">再入力</a>
</div>

<!-- Bootstrap JavaScript -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>
