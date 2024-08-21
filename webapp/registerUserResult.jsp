<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="medical_record.MRecord" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ユーザ登録結果</title>
    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .container {
            margin-top: 20px;
        }
        .result-card {
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .btn-back {
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1 class="my-4">ユーザ登録結果</h1>

    <%
        MRecord user = (MRecord) request.getAttribute("user");
        
        // 職種と診療科の静的なマッピング
        Map<String, String> roles = new HashMap<>();
        roles.put("2", "医者");
        roles.put("3", "看護師");
        roles.put("4", "事務");

        Map<Integer, String> departments = new HashMap<>();
        departments.put(1, "内科");
        departments.put(2, "消化器内科");
        departments.put(3, "心臓血管外科");
        departments.put(4, "整形外科");
        departments.put(5, "外科");
        departments.put(6, "眼科");
        departments.put(7, "小児科");
        departments.put(8, "皮膚科");
        departments.put(9, "精神科");
        departments.put(10, "産科婦人科");
        departments.put(11, "泌尿器科");
        departments.put(12, "耳鼻咽喉科");
        departments.put(13, "脳神経外科");
        departments.put(14, "形成外科");
    %>

    <div class="result-card">
        <p><strong>ID:</strong> <%= user.getUserid() %></p>
        <p><strong>名前:</strong> <%= user.getName() %></p>
        <p><strong>職種:</strong> <%= roles.get(user.getRole()) %></p>
        <p><strong>診療科:</strong> <%= departments.get(user.getDep_id()) %></p>
    </div>

    <a href="top.jsp" class="btn btn-secondary btn-back">メニューへ戻る</a>
</div>

<!-- Bootstrap JavaScript -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
