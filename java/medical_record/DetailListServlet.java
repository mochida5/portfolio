package medical_record;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * カルテ一覧取得処理
 */
@WebServlet("/DetailListServlet")
public class DetailListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DetailListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//リクエストパラメータから選択した患者IDを取得する
		int id = Integer.parseInt(request.getParameter("id"));
		System.out.printf("患者ID：%d\n",id);
		MRecord dto;
			try (MRecordDAO dao = new MRecordDAO()) {
				//該当患者のカルテデータを取得
				List<MRecord> list = dao.detailList(id);
				//病名マスタから病名リストを取得
				List<MRecord> disease = dao.getDisease();

				//取得したpatient（患者）テーブルのデータと病名リストをリクエスト属性へ格納する
				request.setAttribute("detailList", list);
				request.setAttribute("diseaseList", disease);
				
			} catch (Exception e) {
				throw new ServletException(e);
			}
			
		//患者詳細を表示する(detail_list.jspへフォワード)
		RequestDispatcher rd = request.getRequestDispatcher("/detail_list.jsp");
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
