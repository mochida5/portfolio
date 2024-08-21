package medical_record;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 患者情報の新規、更新登録処理
 */
@WebServlet("/RegisterPatientServlet")
public class RegisterPatientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterPatientServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8"); // リクエストのエンコーディングを設定

		// パラメータを取得
        String action = request.getParameter("action"); // "new" または "update"
        String p_idStr = request.getParameter("p_id");
        Integer p_id = p_idStr != null && !p_idStr.isEmpty() ? Integer.parseInt(p_idStr) : null; //p_idの中身をチェック
        String name = request.getParameter("name");
        String birthday = request.getParameter("birthday");
        String sex = request.getParameter("sex");
        String insurance = request.getParameter("insurance");
        
        
        // 生年月日を yyyymmdd 形式に変換する
        String formattedBirthday = "";
        if (birthday != null && !birthday.isEmpty()) {
            try {
                SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyyMMdd");
                java.util.Date date = sdfInput.parse(birthday);
                formattedBirthday = sdfOutput.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "生年月日の形式が無効です。");
                request.setAttribute("name", name);
                request.setAttribute("birthday", birthday);
                request.setAttribute("sex", sex);
                request.setAttribute("insurance", insurance);
                RequestDispatcher dispatcher = request.getRequestDispatcher("registerPatient.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }

        // 生年月日の検証
        if (!isValidDate(formattedBirthday)) {
            request.setAttribute("errorMessage", "無効な生年月日が入力されました。");
            request.setAttribute("name", name);
            request.setAttribute("birthday", birthday);
            request.setAttribute("sex", sex);
            request.setAttribute("insurance", insurance);
            request.setAttribute("p_id", p_id);

            // フォワードで登録画面に戻す
            // action によって戻すページを分岐
            String jspPage = "registerPatient.jsp";
            if ("update".equals(action)) {
        		MRecord dto = new MRecord();
        		
        		try (MRecordDAO dao = new MRecordDAO()) {
        			//患者情報を取得
        			dto = dao.id_patientSearch(p_id);
        		} catch (Exception e) {
        			throw new ServletException(e);
        		}
        	
        		//取得したデータをリクエスト属性へ格納する
        		request.setAttribute("patient", dto);
        		
                jspPage = "patient_info_correction.jsp";
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher(jspPage);
            dispatcher.forward(request, response);
            return;
        }

        //DTOに格納する
		MRecord dto = new MRecord();
		dto.setName(name);
		dto.setBirthday(birthday);
		dto.setSex(sex);
		dto.setInsurance_num(insurance);
		
		//登録処理
		try(MRecordDAO dao = new MRecordDAO()) {
			
			if (p_id != null) {
                // 更新処理
                dto.setP_id(p_id);
                dao.patientUpdate(dto);
            } else {
                // 新規登録処理
                int generatedId = dao.patientInsert(dto);
                dto.setP_id(generatedId);
            }
	         
            // 結果をリクエストに設定
            request.setAttribute("patient", dto);

            // JSPへフォワード
            RequestDispatcher dispatcher = request.getRequestDispatcher("registerPatientResult.jsp");
            dispatcher.forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
	}
	
	private boolean isValidDate(String dateStr) {
	    // yyyyMMdd形式かどうかをチェック
	    if (dateStr == null || !dateStr.matches("\\d{8}")) {
	        return false;
	    }

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    sdf.setLenient(false);
	    
	    try {
	        // 文字列を日付に変換
	        java.util.Date date = sdf.parse(dateStr);

	        // カレンダーを使用して、年1900以上かどうかをチェック
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        int year = cal.get(Calendar.YEAR);
	        if (year < 1900) {
	            return false;
	        }

	        // 入力された日付が正しいかを確認
	        return dateStr.equals(sdf.format(date));
	    } catch (ParseException e) {
	        return false;
	    }
	}

}
