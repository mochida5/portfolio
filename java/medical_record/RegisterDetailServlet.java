package medical_record;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * カルテデータ登録処理
 */
@WebServlet("/RegisterDetailServlet")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024, // 1MB
	    maxFileSize = 1024 * 1024 * 10,  // 10MB
	    maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class RegisterDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterDetailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		// フォームからデータを取得
	    String username = request.getParameter("username");
	    String p_id = request.getParameter("p_id");
	    String dis_id = request.getParameter("dis_id");
	    String detail = request.getParameter("detail");
	    String treatment = request.getParameter("treatment");

		HttpSession session = request.getSession();
		String department = (String) session.getAttribute("department");

     	//DTOに格納する
    	MRecord dto = new MRecord();
  	    
  	    dto.setP_id(Integer.parseInt(p_id));
  		dto.setDis_id(Integer.parseInt(dis_id));
  		dto.setDetail(detail);
  		dto.setTreatment(treatment);
  		dto.setEx_doctor(username);
  		dto.setDep_name(department);

  	    // 患者テーブルからnew_flgを取得
  		int new_patient;
  		try(MRecordDAO dao = new MRecordDAO()) {
  			new_patient = dao.new_flgGet(p_id);
  		} catch (Exception e) {
  			throw new ServletException(e);
  		}

  		// 新患だった場合、患者テーブルに主治医（診察医）と診療科登録を行う
  		int DepartmentId;
  		if (new_patient == 0) {
  			try(MRecordDAO dao = new MRecordDAO()) {
  				// 診療科IDを取得
  				DepartmentId = dao.getDepartmentId(username);
  		  		dto.setDep_id(DepartmentId);
  				System.out.println("診療科：");
  				System.out.println(dto.getDep_id());
  				
  				// 更新処理
  	  			dao.first_patientUpdate(dto);			
  	  		} catch (Exception e) {
  	  			throw new ServletException(e);
  	  		}
  		}
	    
  		int m_id;	//カルテID
  		try(MRecordDAO dao = new MRecordDAO()) {
  			//カルテ新規登録処理をおこなう
  			m_id = dao.detailInsert(dto);
  		} catch (Exception e) {
  			throw new ServletException(e);
  		}
  		
  		// カルテIDが確定した後に画像を保存処理
        Part filePart = request.getPart("file");
        if (filePart != null && filePart.getSize() > 0) {
        	String fileName = getFileName(filePart);
            
            String appPath = request.getServletContext().getRealPath("/"); // Webアプリケーションのルートディレクトリ
            String finalDir = appPath + "images" + File.separator + m_id;
            File finalDirFile = new File(finalDir);
            //保存フォルダを作成するimages/カルテID
            if (!finalDirFile.exists()) {
                if (!finalDirFile.mkdirs()) {
                    throw new ServletException("Failed to create directories: " + finalDir);
                }
            }
            
            String finalFilePath = finalDir + File.separator + fileName;

            System.out.printf("カルテID：");
            System.out.println(m_id);
            
            System.out.printf("保存フォルダ：");
            System.out.println(finalFilePath);
            
            //画像保存
            File file = new File(finalFilePath);
            filePart.write(file.getAbsolutePath());
            
            //dto.setFile_path("images/" + fileName);  // 相対パスを保存
            String relative_path = "images/" + m_id + "/" + fileName;

            System.out.printf("相対パス：");
            System.out.println(relative_path);
            
            
      		try(MRecordDAO dao = new MRecordDAO()) {
      			//カルテIDが確定してから画像ファイルパスを書き込む
      			dao.updateFilePath(m_id, relative_path);
      		} catch (Exception e) {
      			throw new ServletException(e);
      		}
        }
  		
  		//更新または登録後のタスク一覧を画面表示する
//  		RequestDispatcher rd = request.getRequestDispatcher("/DetailListServlet");
//  		rd.forward(request, response);
  	    // p_id をクエリパラメータとしてリダイレクト
  	    response.sendRedirect("DetailListServlet?id=" + p_id);
	}
    
	// ファイル名取得
    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}
