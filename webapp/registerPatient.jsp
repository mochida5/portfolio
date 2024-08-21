<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>新規患者登録</title>
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
        .form-group input[type="date"] {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .form-group input[type="radio"] {
            margin: 0;
            padding: 0;
            vertical-align: middle;
        }
        .form-group label[for="male"],
        .form-group label[for="female"] {
            margin: 0;
            padding-left: 5px;
            vertical-align: middle;
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
    // セッションから属性を取得
    HttpSession sessionObject = request.getSession(false); // セッションを取得（存在しない場合はnull）

    String userRole = (String) sessionObject.getAttribute("role");
    String loginUser = (String) sessionObject.getAttribute("username");

    // 許可されたロールのリスト
    List<String> allowedRoles = Arrays.asList("admin", "clerk");

    // ユーザーのロールが許可されたロールのリストに含まれているかを確認
    boolean hasAccess = allowedRoles.contains(userRole);

    // リクエストからの値を取得
    String name = request.getAttribute("name") != null ? (String) request.getAttribute("name") : "";
    String birthday = request.getAttribute("birthday") != null ? (String) request.getAttribute("birthday") : "";
    String sex = request.getAttribute("sex") != null ? (String) request.getAttribute("sex") : "";
    String insurance = request.getAttribute("insurance") != null ? (String) request.getAttribute("insurance") : "";
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<div class="container">
    <h1>新規患者登録</h1>

    <% if (hasAccess) { %>
        <% if (errorMessage != null) { %>
            <div class="error">
                <%= errorMessage %>
            </div>
        <% } %>

        <form action="RegisterPatientServlet" method="POST">
            <input type="hidden" name="action" value="new">
            <div class="form-group">
                <label for="name">名前：</label>
                <input type="text" id="name" name="name" value="<%= name %>" required>
            </div>

            <div class="form-group">
                <label for="birthday">生年月日：</label>
                <input type="date" id="birthday" name="birthday" value="<%= birthday %>" required>
            </div>

			<div class="form-group">
			    <label for="sex">性別：</label>
			    <input type="radio" id="male" name="sex" value="男" <%= "男".equals(sex) ? "checked" : "" %> required>
			    <label for="male" style="display: inline; margin-right: 20px;">男性</label>
			    <input type="radio" id="female" name="sex" value="女" <%= "女".equals(sex) ? "checked" : "" %> required>
			    <label for="female" style="display: inline;">女性</label>
			</div>

            <div class="form-group">
                <label for="insurance">保険証番号：</label>
                <input type="text" id="insurance" name="insurance" value="<%= insurance %>" required>
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
