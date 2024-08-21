<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="medical_record.MRecord" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>患者情報登録結果</title>
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
    <h1 class="my-4">患者情報登録結果</h1>

    <% 
        MRecord patient = (MRecord) request.getAttribute("patient");
    %>

    <div class="result-card">
        <p><strong>ID:</strong> <%= patient.getP_id() %></p>
        <p><strong>名前:</strong> <%= patient.getName() %></p>
        <p><strong>生年月日:</strong> <%= patient.getBirthday() %></p>
        <p><strong>性別:</strong> <%= patient.getSex() %></p>
        <p><strong>保険証番号:</strong> <%= patient.getInsurance_num() %></p>
    </div>

    <a href="SearchServlet" class="btn btn-primary btn-back">患者一覧へ</a>
</div>

<!-- Bootstrap JavaScript -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
