<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン - 電子カルテシステム</title>
<!-- Bootstrap CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<style>
    body {
        background-color: #f0f0f0;
        padding: 0;
        margin: 0;
        height: 100vh; /* ビューポート全体の高さを確保 */
    }
    .login-container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%; /* 高さをビューポート全体に設定 */
        margin: 0; /* マージンをリセット */
    }
    .card {
        width: 350px;
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 5px;
        background-color: #fff;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
    .card-title {
        text-align: center;
        margin-bottom: 20px;
        font-size: 24px;
        font-weight: bold;
        color: #333;
    }
    .system-title {
        text-align: center;
        font-size: 18px;
        color: #666;
        margin-bottom: 30px;
    }
    .user-table {
        margin: 20px auto;
        max-width: 800px;
        padding-bottom: 20px; /* 下部の余白を調整 */
    }
    .user-table th, .user-table td {
        text-align: center;
    }
</style>

</head>
<body>

<div class="login-container">
    <div class="card">
        <h2 class="system-title">電子カルテシステム</h2>
        <h2 class="card-title">ログイン</h2>
        <form action="LoginServlet" method="POST">
            <div class="form-group">
                <label for="username">ユーザID：</label>
                <input type="text" id="username" name="username" class="form-control">
            </div>
            <div class="form-group">
                <label for="password">パスワード：</label>
                <input type="password" id="password" name="password" class="form-control">
            </div>
            <button type="submit" class="btn btn-primary btn-block">ログイン</button>
        </form>
    </div>
</div>

<!-- Bootstrap JavaScript -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>
