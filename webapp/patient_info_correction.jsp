<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="medical_record.MRecord" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>患者情報修正</title>
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
        margin-right: 5px;
    }
    .form-group input[type="submit"] {
        padding: 10px 20px;
        border: none;
        border-radius: 4px;
        background-color: #007BFF;
        color: white;
        font-size: 16px;
        cursor: pointer;
    }
    .form-group input[type="submit"]:hover {
        background-color: #0056b3;
    }
    .button {
        padding: 10px 20px;
        border: none;
        border-radius: 4px;
        background-color: #007BFF;
        color: white;
        font-size: 16px;
        text-decoration: none;
        display: inline-block;
        margin: 5px 0;
    }
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
    .radio-group label {
        display: inline;
        margin-right: 30px; /* 男性と女性の間隔を調整 */
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

    // リクエストからpatientを取得
    MRecord patient = (MRecord) request.getAttribute("patient");
    
    // 生年月日入力誤り時
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<div class="container">
    <h1>患者情報修正</h1>

    <% if (hasAccess) { %>
        <% if (errorMessage != null) { %>
            <div class="error">
                <%= errorMessage %>
            </div>
        <% } %>
    
        <% if (patient != null) { %>
            <form action="RegisterPatientServlet" method="POST">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="p_id" value="<%= patient.getP_id() %>">

                <div class="form-group">
                    <label for="p_id">患者ID：</label>
                    <input type="text" id="p_id" name="p_id_display" value="<%= patient.getP_id() %>" disabled>
                </div>

                <div class="form-group">
                    <label for="name">名前：</label>
                    <input type="text" id="name" name="name" value="<%= patient.getName() != null ? patient.getName() : "" %>">
                </div>

                <div class="form-group">
                    <label for="sex">性別：</label>
                    <div class="radio-group">
                        <input type="radio" id="male" name="sex" value="男" <%= "男".equals(patient.getSex()) ? "checked" : "" %>>
                        <label for="male">男性</label>
                        <input type="radio" id="female" name="sex" value="女" <%= "女".equals(patient.getSex()) ? "checked" : "" %>>
                        <label for="female">女性</label>
                    </div>
                </div>

                <div class="form-group">
                    <label for="birthday">生年月日：</label>
                    <input type="date" id="birthday" name="birthday" value="<%= patient.getBirthday() != null ? patient.getBirthday() : "" %>">
                </div>

                <div class="form-group">
                    <label for="insurance">保険証番号：</label>
                    <input type="text" id="insurance" name="insurance" value="<%= patient.getInsurance_num() != null ? patient.getInsurance_num() : "" %>">
                </div>

                <div class="form-group">
                    <input type="submit" value="更新">
                </div>
            </form>
        <% } else { %>
            <p class="error">患者情報が見つかりません。</p>
        <% } %>
    <% } else { %>
        <p class="error">このページへのアクセス権がありません。</p>
    <% } %>
</div>

<p><a href="SearchServlet" class="button">戻る</a></p>
</body>
</html>
