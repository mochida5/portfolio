package medical_record;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * ログイン処理
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // ユーザ認証
        if (authenticate(username, password)) {
            // 認証成功時の処理
            // ログイン情報をセッションに保存
            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            // 現在のロールを取得、セッションに保存
            String role = getRole(username);
            session.setAttribute("role", role);

            System.out.println(username);
            System.out.println(role);


            HttpSession sessionObject = request.getSession(false);
            
            System.out.println(sessionObject);

            if (role.equals("doctor") || role.equals("nurse")) {
            	
            	//診療科取得
                String department = getDepartment(username);
                session.setAttribute("Department", department);
            	
                //リダイレクト
                response.sendRedirect("SearchServlet");

            } else {
            	response.sendRedirect("top.jsp");
            }
            
        } else {
            // 認証失敗時の処理            
            RequestDispatcher rd = request.getRequestDispatcher("login_error.jsp");
            rd.forward(request, response);
        }
	}
	
    // ユーザ認証のためのメソッド（ハッシュ化）
    private boolean authenticate(String username, String password) {
		boolean result = false;
    	try (MRecordDAO dao = new MRecordDAO()) {
    		String hashpass = dao.getHashedPassword(username);
            if (hashpass != null) {
                result = BCrypt.checkpw(password, hashpass); // パスワードの検証
            } else {
                System.out.println("Failed to retrieve hashed password for user: " + username);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // 現在のユーザのロールを取得するメソッド
    private String getRole(String username) {
    	MRecord dto;
    	try (MRecordDAO dao = new MRecordDAO()) {
    		dto = dao.roleGet(username);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

        return dto.getRole();
    }
    // 現在のユーザの診療科を取得するmethod
    private String getDepartment(String username) {
    	MRecord dto;
        try (MRecordDAO dao = new MRecordDAO()) {
            dto = dao.getDepartment(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dto.getDep_name();
    }

}
