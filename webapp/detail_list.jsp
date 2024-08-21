<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="medical_record.MRecord" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <title>電子カルテシステム</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding-top: 150px;
            background-color: #f5f5f5;
            color: #333;
        }
        .fixed-header {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            background-color: #00796b;
            color: white;
            border-bottom: 2px solid #004d40;
            padding: 20px 0;
            z-index: 1000;
            text-align: center;
            font-size: 18px;
            font-weight: bold;
        }
        .fixed-header table {
            margin: 0 auto;
        }
        .fixed-header th, .fixed-header td {
            padding: 10px 15px;
            background-color: #00796b;
            color: white;
        }
        .fixed-header .back-link {
            position: absolute;
            right: 20px;
            top: 20px;
            color: white;
            text-decoration: none;
        }
        
        .content {
            padding: 20px;
        }
        .record-section {
            border: 1px solid #cfd8dc;
            background-color: white;
            margin-bottom: 20px;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .record-section .header {
            font-weight: bold;
            background-color: #b2dfdb;
            padding: 12px;
            border-bottom: 1px solid #cfd8dc;
        }
        .record-section .details {
            display: flex;
            border-top: 1px solid #cfd8dc;
        }
        .record-section .details div {
            width: 50%;
            padding: 15px;
        }
        .record-section .details .findings {
            background-color: #fafafa;
        }
        .record-section .details .treatment {
            background-color: #f9fbe7;
        }
        textarea {
            width: 100%;
            height: 200px;
            border: 1px solid #cfd8dc;
            padding: 8px;
            border-radius: 4px;
            box-sizing: border-box;
            background-color: white;
            cursor: not-allowed;
            resize: none;
            caret-color: transparent;
        }
        textarea::-webkit-clear-button,
        textarea::-ms-clear {
            display: none;
        }
        .thumbnail {
            width: 100px;
            height: 100px;
            object-fit: cover;
            cursor: pointer;
        }
        .image {
            margin-top: 10px;
        }
    </style>
    <script>
        function openNewWindow(p_id, p_name, p_sex, p_age, p_birthday) {
            const newWindow = window.open("", "newWindow", "width=800,height=800,scrollbars=yes,resizable=yes");
            newWindow.document.write(`
                <html>
                <head>
                    <title>新規カルテ登録</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            padding: 20px;
                        }
                        label, input, textarea {
                            display: block;
                            margin-bottom: 10px;
                        }
                        textarea {
                            width: 100%;
                            height: 100px;
                        }
                        .fixed-header {
                            position: fixed;
                            top: 0;
                            left: 0;
                            width: 100%;
                            background-color: #00796b;
                            color: white;
                            border-bottom: 2px solid #004d40;
                            padding: 20px 0;
                            z-index: 1000;
                            text-align: center;
                            font-size: 18px;
                            font-weight: bold;
                        }
                        .fixed-header table {
                            margin: 0 auto;
                        }
                        .fixed-header th, .fixed-header td {
                            padding: 10px 15px;
                            background-color: #00796b;
                            color: white;
                        }
                        .content {
                            padding-top: 150px;
                        }
                    </style>
                </head>
                <body>
	                <div class="fixed-header">
	                    <table>
	                        <tr>
	                            <th>患者ID</th>
	                            <td>` + p_id + `</td>
	                            <th>患者名</th>
	                            <td>` + p_name + `</td>
	                            <th>性別</th>
	                            <td>` + p_sex + `</td>
	                            <th>年齢</th>
	                            <td>` + p_age + `</td>
	                            <th>生年月日</th>
	                            <td>` + p_birthday + `</td>
	                        </tr>
	                    </table>
	                </div>
	                <div class="content">
	                    <h2>新規カルテ登録</h2>
	                    <form id="registrationForm" action="RegisterDetailServlet" method="post" target="resultFrame" enctype="multipart/form-data">
	                        <input type="hidden" name="username" value="<%= session.getAttribute("username") %>">
	                        <input type="hidden" name="p_id" value="` + p_id + `">
	                        <input type="hidden" name="p_name" value="` + p_name + `">
	                        <input type="hidden" name="p_sex" value="` + p_sex + `">
	                        <input type="hidden" name="p_age" value="` + p_age + `">
	                        <input type="hidden" name="p_birthday" value="` + p_birthday + `">

	                        <label for="disease">病名：</label>
	                        <select id="disease" name="dis_id" class="form-control">
	                            <option value="">選択してください</option>
	                            <% 
	                                List<MRecord> diseaseList = (List<MRecord>) request.getAttribute("diseaseList");
	                                if (diseaseList != null) {
	                                    for (MRecord record : diseaseList) {
	                            %>
	                            <option value="<%= record.getDis_id() %>"><%= record.getDis_name() %></option>
	                            <% 
	                                    }
	                                }
	                            %>
	                        </select><br><br>
	                        
	                        <label for="detail">所見:</label>
	                        <textarea id="detail" name="detail" rows="4" required></textarea><br><br>
	                        <label for="treatment">処置内容:</label>
	                        <textarea id="treatment" name="treatment" rows="4"></textarea><br><br>
	                        <label for="file">画像ファイル:</label>
	                        <input type="file" id="file" name="file"><br><br>
	                        <button type="submit">登録</button>
	                    </form>
	                </div>
	            </body>
	            </html>
	        `);
            newWindow.document.close();

            newWindow.onload = function() {
                const form = newWindow.document.getElementById("registrationForm");
                form.onsubmit = function(event) {
                    event.preventDefault();

                    const xhr = new XMLHttpRequest();
                    xhr.open("POST", form.action, true);
                    xhr.onload = function() {
                        if (xhr.status === 200) {
                            if (newWindow.opener && !newWindow.opener.closed) {
                                newWindow.opener.location.reload();
                            }
                            newWindow.close();
                        }
                    };
                    const formData = new FormData(form);
                    xhr.send(formData);
                };
            };
        }

        function openImageInNewWindow(imageUrl) {
            const newWindow = window.open("", "_blank", "width=800,height=800,scrollbars=yes,resizable=yes");
            newWindow.document.write('<html><head><title>画像表示</title></head><body><img src="' + imageUrl + '" style="width:100%; height:auto;" /></body></html>');
            newWindow.document.close();
        }
    </script>
</head>
<body>

<%
    List<MRecord> detailList = (List<MRecord>) request.getAttribute("detailList");
    String p_id = (detailList != null && !detailList.isEmpty()) ? String.valueOf(detailList.get(0).getP_id()) : "";
    String p_name = (detailList != null && !detailList.isEmpty()) ? detailList.get(0).getName() : "";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>

<!-- 固定ヘッダー -->
<div class="fixed-header">
    <table>
        <tr>
            <th>患者ID</th>
            <td><%= p_id %></td>
            <th>患者名</th>
            <td><%= p_name %></td>
            <th>性別</th>
            <td><%= (detailList != null && !detailList.isEmpty()) ? detailList.get(0).getSex() : "" %></td>
            <th>年齢</th>
            <td><%= (detailList != null && !detailList.isEmpty()) ? detailList.get(0).getAge() : "" %></td>
            <th>生年月日</th>
            <td><%= (detailList != null && !detailList.isEmpty()) ? detailList.get(0).getBirthday() : "" %></td>
        </tr>
    </table>
    <a href="<%= request.getContextPath() %>/SearchServlet" class="back-link">患者一覧へ</a>
</div>

<div class="content">
<% 
    String userRole = (String) session.getAttribute("role"); 
    if (!"clerk".equals(userRole)) { 
%>
    <button onclick="openNewWindow('<%= p_id %>', '<%= p_name %>', '<%= detailList != null && !detailList.isEmpty() ? detailList.get(0).getSex() : "" %>', '<%= detailList != null && !detailList.isEmpty() ? detailList.get(0).getAge() : "" %>', '<%= detailList != null && !detailList.isEmpty() ? detailList.get(0).getBirthday() : "" %>')">新規登録</button>
<% 
    } 
%>

    <br>
    
    <% 
        boolean recordsDisplayed = false;
        if (detailList != null && !detailList.isEmpty()) { 
            for (MRecord record : detailList) { 
                if (record.getLastupdate() != null) {
                    recordsDisplayed = true;
    %>
                    <div class="record-section">
                        <div class="header">
                            診察日：<%= sdf.format(record.getLastupdate()) %>　病名：<%= record.getDis_name() != null ? record.getDis_name() : "不明" %>　診察医：<%= record.getEx_doctor() != null ? record.getEx_doctor() : "不明" %>　主治医：<%= record.getDoctor_name() != null ? record.getDoctor_name() : "不明" %>
                        </div>
                        <div class="details">
                            <div class="findings">
                                <div>所見</div>
                                <textarea readonly><%= record.getDetail() %></textarea>
                            </div>
                            <div class="treatment">
                                <div>処置内容</div>
                                <textarea readonly><%= record.getTreatment() %></textarea>
                            </div>
                        </div>
                        <% if (record.getFile_path() != null && !record.getFile_path().isEmpty()) { %>
                            <div class="image">
                                <img src="<%= request.getContextPath() %>/<%= record.getFile_path() %>" alt="uploaded image" class="thumbnail" onclick="openImageInNewWindow('<%= request.getContextPath() %>/<%= record.getFile_path() %>')" />
                            </div>
                        <% } %>
                    </div>
    <% 
                }
            }
        }
    %>
    
    <% if (!recordsDisplayed) { %>
        <p>診察日が登録されていません。新規カルテを登録してください。</p>
    <% } %>
</div>


</body>
</html>
