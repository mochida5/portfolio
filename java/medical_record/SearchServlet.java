package medical_record;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 患者一覧を取得する（list.jspへフォワード）
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. 
	 */
	public SearchServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		
		//ログインユーザ、ロールをセッションより取得
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
		//MRecordDAOのインスタンス生成
		try (MRecordDAO dao = new MRecordDAO()) {
		
            // 診療科リストを取得し、リクエスト属性へ格納する
            List<MRecord> departmentRecords = dao.getDepartment();
            List<String> departments = new ArrayList<>();
            for (MRecord record : departmentRecords) {
                departments.add(record.getDep_name());
            }
            request.setAttribute("departments", departments);
            
			if (role.equals("doctor")) {
				// 担当患者データを取得し、リクエスト属性へ格納する
				List<MRecord> list = dao.getAssignedPatients(username);
				request.setAttribute("patientList", list);
			} else if (role.equals("nurse")) {
				// 所属診療科患者データを取得し、リクエスト属性へ格納する ※未実装のため全患者表示
//				List<MRecord> list = dao.dep_patientList(username);
				List<MRecord> list = dao.all_patientList();
				request.setAttribute("patientList", list);
			} else {
				// 全患者リストを取得し、リクエスト属性へ格納する
				List<MRecord> list = dao.all_patientList();
				request.setAttribute("patientList", list);
			}
				
		} catch (Exception e) {
			throw new ServletException(e);
		}

		//検索一覧を表示する（list.jspへフォワード）
		RequestDispatcher rd = request.getRequestDispatcher("/list.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(javax.servlet.http.HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
