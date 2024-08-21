package medical_record;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 検索された患者情報を取得処理
 */
@WebServlet("/KeywordSearchServlet")
public class KeywordSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KeywordSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 検索条件を取得
        String patientId = request.getParameter("patientId");
        String patientName = request.getParameter("patientName");
        String department = request.getParameter("department");
        String patientView = request.getParameter("patientView"); // ラジオボタンの値を正しく取得

        // ラジオボタンの値を確認
        boolean showAllPatients = "all".equals(patientView);
        boolean showAssignedPatients = "assigned".equals(patientView);
        
        System.out.println(showAllPatients);
        System.out.println(showAssignedPatients);
        
        // セッションからログインユーザ名を取得
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        
        System.out.println(username);
        List<MRecord> patientList = new ArrayList<>();

		//MRecordDAOのインスタンス生成
		try (MRecordDAO dao = new MRecordDAO()) {
			
            // 診療科リストを取得し、リクエスト属性へ格納する
            List<MRecord> departmentRecords = dao.getDepartment();
            List<String> departments = new ArrayList<>();
            for (MRecord record : departmentRecords) {
                departments.add(record.getDep_name());
            }
            request.setAttribute("departments", departments);
            
            if (showAllPatients) {
                // 全患者を表示
                patientList = dao.all_patientList(patientId, patientName, department);
            } else if (showAssignedPatients) {
                // 担当患者のみを表示
                patientList = dao.getAssignedPatients(username, patientId, patientName, department);
            } else {
                // ロールがadminとclerkのとき（ラジオボタン非表示時）
            	patientList = dao.all_patientList(patientId, patientName, department);
            }
		} catch (Exception e) {
			throw new ServletException(e);
		}

		//検索一覧を表示する（list.jspへフォワード）
		request.setAttribute("patientList", patientList);
        request.setAttribute("patientView", patientView);
		RequestDispatcher rd = request.getRequestDispatcher("/list.jsp");
		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
