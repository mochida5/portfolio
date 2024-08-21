package medical_record;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 患者情報を修正する
 */
@WebServlet("/PatientInfoCorrection")
public class PatientInfoCorrection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PatientInfoCorrection() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//リクエストパラメータから選択した患者IDを取得する
		int id = Integer.parseInt(request.getParameter("id"));
		
		MRecord dto = new MRecord();
		
		try (MRecordDAO dao = new MRecordDAO()) {
		//患者情報を取得
			dto = dao.id_patientSearch(id);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	
		//取得したデータをリクエスト属性へ格納する
		request.setAttribute("patient", dto);
		
		//患者情報修正ページを表示する
		RequestDispatcher rd = request.getRequestDispatcher("/patient_info_correction.jsp");
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
