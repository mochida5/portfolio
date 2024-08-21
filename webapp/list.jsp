<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="medical_record.MRecord" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="javax.servlet.http.HttpSession" %>

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
    .table-hover tbody tr:hover {
        background-color: #f5f5f5; /* マウスオーバー時の背景色 */
    }
    .btn-sm {
        font-size: 0.875rem; /* ボタンのフォントサイズ */
        padding: 0.25rem 0.5rem; /* ボタンのパディング */
        border-radius: 0.2rem; /* ボタンの角丸 */
    }
    .btn-link {
        color: #007bff;
        font-size: 0.875rem;
        text-decoration: none;
    }
    .btn-link:hover {
        color: #0056b3;
        text-decoration: underline;
    }
    
    /* カラム幅を指定 */
	.col-id {
	    width: 7%;
	}
	.col-name {
	    width: 12%;
	}
	.col-age {
	    width: 6%;
	}
	.col-sex {
	    width: 6%;
	}
	.col-birthday {
	    width: 11%;
	}
	.col-department {
	    width: 11%;
	}
	.col-insurance {
	    width: 10%;
	}
	.col-lastupdate {
	    width: 14%;
	}
	.col-detail {
	    width: 11%;
	}
	.col-correction {
	    width: 13%;
	}
	.col-delete {
	    width: 10%;
	}
    
</style>
<script type="text/javascript">
function submitForm() {
    document.getElementById("searchForm").submit();
}
</script>
</head>
<body>
<div class="container">
    <h1>患者一覧</h1>

    <% HttpSession sessionObj = request.getSession();
       String loginUser = (String) sessionObj.getAttribute("username");
       String role = (String) sessionObj.getAttribute("role");
       String nurseDepartment = (String) sessionObj.getAttribute("Department");
       
       List<MRecord> records = (List<MRecord>) request.getAttribute("patientList");
       String patientId = request.getParameter("patientId");
       String patientName = request.getParameter("patientName");
       String department = request.getParameter("department");
       List<String> departments = (List<String>) request.getAttribute("departments");
    %>

    <form id="searchForm" action="KeywordSearchServlet" method="GET" class="form-inline mb-3">
        <% if ("doctor".equals(role)) { %>
            <div class="form-check mr-3">
                <input class="form-check-input" type="radio" id="showAllPatients" name="patientView" value="all" <%= "all".equals(request.getParameter("patientView")) ? "checked" : "" %> onchange="submitForm()">
                <label class="form-check-label" for="showAllPatients">全患者表示</label>
            </div>
            <div class="form-check mr-3">
                <input class="form-check-input" type="radio" id="showAssignedPatients" name="patientView" value="assigned" <%= "assigned".equals(request.getParameter("patientView")) || request.getParameter("patientView") == null ? "checked" : "" %> onchange="submitForm()">
                <label class="form-check-label" for="showAssignedPatients">担当患者</label>
            </div>
        <% } %>
        <input type="text" class="form-control mr-2" name="patientId" placeholder="患者ID" value="<%= patientId != null ? patientId : "" %>">
        <input type="text" class="form-control mr-2" name="patientName" placeholder="名前" value="<%= patientName != null ? patientName : "" %>">
        <select class="form-control mr-2" name="department" onchange="submitForm()">
            <option value="">全診療科</option>
            <% if (departments != null) {
                for (String dep : departments) {
            %>
            <option value="<%= dep %>" <%= dep.equals(department) ? "selected" : "" %>><%= dep %></option>
            <% } } %>
        </select>
        <button type="submit" class="btn btn-primary">検索</button>
    </form>

<table class="table table-bordered table-hover">
    <thead>
        <tr>
            <th class="col-id">患者ID</th>
            <th class="col-name">名前</th>
            <th class="col-age">年齢</th>
            <th class="col-sex">性別</th>
            <th class="col-birthday">生年月日</th>
            <th class="col-department">診療科</th>
            <% if ("clerk".equals(role) || "admin".equals(role)) { %>
                <th class="col-insurance">保険証番号</th>
            <% } %>
            <th class="col-lastupdate">診察日</th>
            <th class="col-detail">カルテ表示</th>
            <% if ("clerk".equals(role) || "admin".equals(role)) { %>
                <th class="col-correction">患者情報修正</th>
                <% if ("admin".equals(role)) { %>
                    <th class="col-delete">削除</th>
                <% } %>
            <% } %>
        </tr>
    </thead>
    <tbody>
        <% if (records != null) {
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
               for (MRecord rec : records) {
        %>
        <tr>
            <td class="col-id"><%= rec.getP_id() %></td>
            <td class="col-name"><%= rec.getName() %></td>
            <td class="col-age"><%= rec.getAge() %></td>
            <td class="col-sex"><%= rec.getSex() %></td>
            <td class="col-birthday"><%= rec.getBirthday() %></td>
            <td class="col-department"><%= rec.getDep_name() %></td>
            <% if ("clerk".equals(role) || "admin".equals(role)) { %>
                <td class="col-insurance"><%= rec.getInsurance_num() %></td>
            <% } %>
            <td class="col-lastupdate"><%= rec.getLastupdate() != null ? sdf.format(rec.getLastupdate()) : "" %></td>
            <td class="col-detail"><a href="DetailListServlet?id=<%= rec.getP_id() %>" class="btn btn-info btn-sm">カルテ表示</a></td>
            <% if ("clerk".equals(role) || "admin".equals(role)) { %>
                <td class="col-correction"><a href="PatientInfoCorrection?id=<%= rec.getP_id() %>" class="btn btn-warning btn-sm">修正</a></td>
                <% if ("admin".equals(role)) { %>
                    <td class="col-delete">
                        <a href="DeletePatientServlet?id=<%= rec.getP_id() %>" 
                           class="btn btn-danger btn-sm" 
                           onclick="return confirm('本当に削除しますか？');">
                           削除
                        </a>
                    </td>
                <% } %>
            <% } %>
        </tr>
        <%      }
              }
        %>
    </tbody>
</table>


    <p>
        <a href="LogoutServlet" class="btn btn-secondary btn-sm">ログアウト</a>
        <% if ("admin".equals(role) || "clerk".equals(role)) { %>
            <a href="top.jsp" class="btn btn-primary btn-sm">トップページに戻る</a>
        <% } %>
    </p>
</div>
<!-- Bootstrap JavaScript -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
