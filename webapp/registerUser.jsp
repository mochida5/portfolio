<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>新規ユーザ登録</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f9f9f9;
        }
        h1 {
            color: #333;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-group input[type="text"],
        .form-group input[type="password"],
        .form-group select {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .form-group input[type="submit"],
        .button {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            background-color: #007BFF;
            color: white;
            font-size: 16px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin: 5px 0;
        }
        .form-group input[type="submit"]:hover,
        .button:hover {
            background-color: #0056b3;
        }
        .error {
            color: red;
            font-weight: bold;
        }
        .radio-group {
            display: flex;
            align-items: center;
        }
    </style>
</head>
<body>
<%
    HttpSession sessionObject = request.getSession(false); // セッションを取得（存在しない場合はnull）

    String userRole = (String) sessionObject.getAttribute("role");
    String loginUser = (String) sessionObject.getAttribute("username");

    // 許可されたロールのリスト
    List<String> allowedRoles = Arrays.asList("admin");

    // ユーザーのロールが許可されたロールのリストに含まれているかを確認
    boolean hasAccess = allowedRoles.contains(userRole);

    // リクエストからの値を取得
    String userId = request.getAttribute("userId") != null ? (String) request.getAttribute("userId") : "";
    String password = request.getAttribute("password") != null ? (String) request.getAttribute("password") : "";
    String name = request.getAttribute("name") != null ? (String) request.getAttribute("name") : "";
    String role = request.getAttribute("role") != null ? (String) request.getAttribute("role") : "";
    String department = request.getAttribute("department") != null ? (String) request.getAttribute("department") : "";
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<div class="container">
    <h1>新規ユーザ登録</h1>

    <% if (hasAccess) { %>
        <% if (errorMessage != null) { %>
            <div class="error">
                <%= errorMessage %>
            </div>
        <% } %>

        <form action="RegisterUserServlet" method="POST">
            <div class="form-group">
                <label for="userId">ユーザID：</label>
                <input type="text" id="userId" name="userId" value="<%= userId %>" required>
            </div>

            <div class="form-group">
                <label for="password">パスワード：</label>
                <input type="password" id="password" name="password" value="<%= password %>" required>
            </div>

            <div class="form-group">
                <label for="name">名前：</label>
                <input type="text" id="name" name="name" value="<%= name %>" required>
            </div>

            <div class="form-group">
                <label for="role">職種：</label>
                <select id="role" name="role" required>
                    <option value="2" <%= "2".equals(role) ? "selected" : "" %>>医者</option>
                    <option value="3" <%= "3".equals(role) ? "selected" : "" %>>看護師</option>
                    <option value="4" <%= "4".equals(role) ? "selected" : "" %>>事務</option>
                </select>
            </div>

            <div class="form-group">
                <label for="department">担当診療科：</label>
                <select id="department" name="department" required>
                    <option value="1" <%= "1".equals(department) ? "selected" : "" %>>内科</option>
                    <option value="2" <%= "2".equals(department) ? "selected" : "" %>>消化器内科</option>
                    <option value="3" <%= "3".equals(department) ? "selected" : "" %>>心臓血管外科</option>
                    <option value="4" <%= "4".equals(department) ? "selected" : "" %>>整形外科</option>
                    <option value="5" <%= "5".equals(department) ? "selected" : "" %>>外科</option>
                    <option value="6" <%= "6".equals(department) ? "selected" : "" %>>眼科</option>
                    <option value="7" <%= "7".equals(department) ? "selected" : "" %>>小児科</option>
                    <option value="8" <%= "8".equals(department) ? "selected" : "" %>>皮膚科</option>
                    <option value="9" <%= "9".equals(department) ? "selected" : "" %>>精神科</option>
                    <option value="10" <%= "10".equals(department) ? "selected" : "" %>>産科婦人科</option>
                    <option value="11" <%= "11".equals(department) ? "selected" : "" %>>泌尿器科</option>
                    <option value="12" <%= "12".equals(department) ? "selected" : "" %>>耳鼻咽喉科</option>
                    <option value="13" <%= "13".equals(department) ? "selected" : "" %>>脳神経外科</option>
                    <option value="14" <%= "14".equals(department) ? "selected" : "" %>>形成外科</option>
                </select>
            </div>

            <div class="form-group">
                <input type="submit" value="登録">
            </div>
        </form>
    <% } else { %>
        <p class="error">このページへのアクセス権がありません。</p>
    <% } %>
</div>

<p><a href="SearchServlet" class="button">戻る</a></p>
</body>
</html>
