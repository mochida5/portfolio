<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%
    HttpSession sessionObject = request.getSession(false); // セッションを取得（存在しない場合はnull）

    String userRole = (String) sessionObject.getAttribute("role");
    String loginUser = (String) sessionObject.getAttribute("username");
    
    // 許可されたロールのリスト
    List<String> allowedRoles = Arrays.asList("admin", "clerk");

    // ユーザーのロールが許可されたロールのリストに含まれているかを確認
    boolean hasAccess = allowedRoles.contains(userRole);
    boolean isClerk = "clerk".equals(userRole);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>電子カルテシステム</title>

<!-- Bootstrap CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<style>
    .container {
        margin-top: 20px;
    }
    .btn-custom {
        margin: 5px;
        font-size: 1rem;
        padding: 10px 20px;
    }
    .navbar {
        background-color: #343a40;
        padding: 10px;
    }
    .navbar a {
        color: white;
        margin: 0 10px;
        text-decoration: none;
        font-size: 1.1rem;
    }
    .navbar a:hover {
        text-decoration: underline;
    }
</style>
</head>
<body>
<div class="container">
    <h1>電子カルテシステム</h1>
    <br><br><br>
    <div class="navbar">
        <% if (hasAccess) { %>
            <a href="registerPatient.jsp" class="btn btn-custom btn-primary">新患登録</a>
            <a href="SearchServlet" class="btn btn-custom btn-secondary">患者一覧</a>
            <% if (!isClerk) { %>
                <a href="registerUser.jsp" class="btn btn-custom btn-success">ユーザ登録</a>
            <% } %>
        <% } else { %>
            <p>このページへのアクセス権がありません。</p>
        <% } %>
        
        <a href="LogoutServlet" class="btn btn-custom btn-danger">ログアウト</a>
    </div>
</div>

<!-- Bootstrap JavaScript -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
