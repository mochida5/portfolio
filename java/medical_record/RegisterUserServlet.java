package medical_record;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * 新規ユーザ登録処理
 */
@WebServlet("/RegisterUserServlet")
public class RegisterUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUserServlet() {
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
        String userid = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String role_id = request.getParameter("role");
        
      //DTOに格納する
      		MRecord dto = new MRecord();
      		dto.setUserid(userid);
      		dto.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
      		dto.setName(name);
      		dto.setDep_id(Integer.parseInt(department));
      		dto.setRole(role_id);
      		
      		//登録処理
      		try(MRecordDAO dao = new MRecordDAO()) {
      			
                  // 登録処理
                  dao.userInsert(dto);
      	         
                  // 結果をリクエストに設定
                  request.setAttribute("user", dto);

                  // JSPへフォワード
                  RequestDispatcher dispatcher = request.getRequestDispatcher("registerUserResult.jsp");
                  dispatcher.forward(request, response);
      		} catch (Exception e) {
      			throw new ServletException(e);
      		}
      		
	}

}
