package medical_record;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MRecordDAO extends DAO {

	/*useridからハッシュ化されたパスワードを取得する*/
	public String getHashedPassword(String userid) throws Exception {
		String sql = "SELECT password FROM users WHERE userid = ?";

		// プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		statement.setString(1, userid);
		
		System.out.println("パスワード：");
		System.out.println(sql);
		
		// SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();
		
	    // ResultSetのカーソルを移動してデータを取得する
	    if (rs.next()) {
	        return rs.getString("password");
	    } else {
	        // ユーザーが存在しない場合はnullを返す
	        return null;
	    }
	    
	}
	
	/*該当のuseridとパスワードがあるかをチェックする*/
	public boolean loginCheck(String userid, String password) throws Exception {
		String sql = "SELECT userid, password FROM users WHERE userid = ? AND password = ?";

		// プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
	    statement.setString(1, userid);
	    statement.setString(2, password);
	    
		// SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

	    // 結果が存在する場合はtrue、存在しない場合はfalseを返す
	    boolean isValidUser = rs.next();
	    
		return isValidUser;
	}
	
	/*useridからroleを取得する*/
	public MRecord roleGet(String userid) throws Exception {
		String sql = "SELECT roles.role FROM roles "
				   + "LEFT JOIN users ON users.role_id = roles.role_id "
				   + "WHERE userid = ?";

		// プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		statement.setString(1, userid);
		
		System.out.println("ロール取得：");
		System.out.println(sql);
		
		// SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();
		
		// 検索結果をTodoクラスのインスタンスdtoへ格納する
		MRecord dto = new MRecord();
		while (rs.next()) {
			// クエリ結果をDTOへ格納
			dto.setRole(rs.getString("role"));
		}

		return dto;
	}
	
	/* 新患フラグを取得する*/
	public int new_flgGet(String p_id) throws Exception {
		String sql = "SELECT new_flg FROM patient "
				   + "WHERE p_id = ?";

		// プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		statement.setString(1, p_id);
		
		System.out.println("新患フラグ取得：");
		System.out.println(sql);
		
		// SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

	    // データが存在する場合にnew_flgを取得
	    if (rs.next()) {
	        return rs.getInt("new_flg");
	    } else {
	        // レコードが見つからなかった場合
	        throw new Exception("指定された患者Dに対応する新患フラグが見つかりませんでした: " + p_id);
	    }
	}
	
	/*診療科一覧を取得する*/
	public List<MRecord> getDepartment() throws Exception {
		List<MRecord> returnList = new ArrayList<MRecord>();

		String sql = "SELECT dep_name FROM department";

		//プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);

		System.out.println("診療科一覧取得：");
		System.out.println(sql);
        

		//SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

		//検索結果の行数分フェッチを行い、取得結果をMRecordクラスのインスタンスdtoのリストに格納する
		while (rs.next()) {
			MRecord dto = new MRecord();
			dto.setDep_name(rs.getString("dep_name"));
			returnList.add(dto);
		}

		return returnList;

	}
		
	/*全患者データを取得する*/
	public List<MRecord> all_patientList(String patientId, String patientName, String department) throws Exception {
		List<MRecord> returnList = new ArrayList<MRecord>();

		String sql = "SELECT pa.userid, pa.p_id, pa.name, pa.birthday, pa.sex, latest_m_rec.m_day, insurance_num, " +
	             "COALESCE(dep.dep_name, '') AS dep_name, " +
	             "TIMESTAMPDIFF(YEAR, pa.birthday, CURDATE()) - (DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(pa.birthday, '%m%d')) AS age " +
	             "FROM patient AS pa " +
	             "LEFT JOIN department AS dep ON dep.dep_id = pa.dep_id " +
	             "LEFT JOIN ( " +
	             "  SELECT p_id, m_day, ROW_NUMBER() OVER (PARTITION BY p_id ORDER BY m_day DESC) AS rn " +
	             "  FROM m_record " +
	             ") AS latest_m_rec " +
	             "ON pa.p_id = latest_m_rec.p_id AND latest_m_rec.rn = 1 " +
	             "WHERE 1=1 ";


	    // 検索条件
	    if (patientId != null && !patientId.isEmpty()) {
	        sql += " AND pa.p_id = ?";
	    }

	    if (patientName != null && !patientName.isEmpty()) {
	        sql += " AND pa.name LIKE ?";
	    }

	    if (department != null && !department.isEmpty()) {
	        sql += " AND dep.dep_name = ?";
	    }

		System.out.println("全件表示：");
		System.out.println(sql);

		//プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		
	    // パラメータの設定
	    int index = 1;
	    if (patientId != null && !patientId.isEmpty()) {
	        statement.setString(index++, patientId);
	    }
	    if (patientName != null && !patientName.isEmpty()) {
	        statement.setString(index++, "%" + patientName + "%");
	    }
	    if (department != null && !department.isEmpty()) {
	        statement.setString(index++, department);
	    }
	    
		//SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

		//検索結果の行数分フェッチを行い、取得結果をMRecordクラスのインスタンスdtoのリストに格納する
		while (rs.next()) {
			MRecord dto = new MRecord();
			dto.setUserid(rs.getString("userid"));
			dto.setP_id(rs.getInt("p_id"));
			dto.setName(rs.getString("name"));
			dto.setAge(rs.getInt("age"));
			dto.setSex(rs.getString("sex"));
			dto.setBirthday(rs.getString("birthday"));
			dto.setLastupdate(rs.getTimestamp("m_day"));
			dto.setDep_name(rs.getString("dep_name"));
			dto.setInsurance_num(rs.getString("insurance_num"));
			returnList.add(dto);
		}

		return returnList;

	}
	
	/*全患者データを取得する（引数なし）*/
	public List<MRecord> all_patientList() throws Exception {
		List<MRecord> returnList = new ArrayList<MRecord>();

		String sql = "SELECT pa.userid, pa.p_id, pa.name, pa.birthday, pa.sex, latest_m_rec.m_day, insurance_num, " +
	             "COALESCE(dep.dep_name, '') AS dep_name, " +
	             "TIMESTAMPDIFF(YEAR, pa.birthday, CURDATE()) - (DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(pa.birthday, '%m%d')) AS age " +
	             "FROM patient AS pa " +
	             "LEFT JOIN department AS dep ON dep.dep_id = pa.dep_id " +
	             "LEFT JOIN ( " +
	             "  SELECT p_id, m_day, ROW_NUMBER() OVER (PARTITION BY p_id ORDER BY m_day DESC) AS rn " +
	             "  FROM m_record " +
	             ") AS latest_m_rec " +
	             "ON pa.p_id = latest_m_rec.p_id AND latest_m_rec.rn = 1 ";

		//プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);

		System.out.println("全件表示：");
		System.out.println(sql);
        

		//SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

		//検索結果の行数分フェッチを行い、取得結果をMRecordクラスのインスタンスdtoのリストに格納する
		while (rs.next()) {
			MRecord dto = new MRecord();
			dto.setUserid(rs.getString("userid"));
			dto.setP_id(rs.getInt("p_id"));
			dto.setName(rs.getString("name"));
			dto.setAge(rs.getInt("age"));
			dto.setSex(rs.getString("sex"));
			dto.setBirthday(rs.getString("birthday"));
			dto.setLastupdate(rs.getTimestamp("m_day"));
			dto.setDep_name(rs.getString("dep_name"));
			dto.setInsurance_num(rs.getString("insurance_num"));
			returnList.add(dto);
		}

		return returnList;

	}
	
	/*担当患者情報のデータを取得する*/
	public List<MRecord> getAssignedPatients(String userid, String patientId, String patientName, String department) throws Exception {

		List<MRecord> returnList = new ArrayList<MRecord>();

		String sql = "SELECT pa.userid, pa.p_id, pa.name, pa.birthday, pa.sex, latest_m_rec.m_day, " +
	             "COALESCE(dep.dep_name, '') AS dep_name, " +
	             "TIMESTAMPDIFF(YEAR, pa.birthday, CURDATE()) - (DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(pa.birthday, '%m%d')) AS age " +
	             "FROM patient AS pa " +
	             "LEFT JOIN department AS dep ON dep.dep_id = pa.dep_id " +
	             "LEFT JOIN ( " +
	             "  SELECT p_id, m_day, ROW_NUMBER() OVER (PARTITION BY p_id ORDER BY m_day DESC) AS rn " +
	             "  FROM m_record " +
	             ") AS latest_m_rec " +
	             "ON pa.p_id = latest_m_rec.p_id AND latest_m_rec.rn = 1 " +
				 " WHERE userid = ?";

	    if (patientId != null && !patientId.isEmpty()) {
	        sql += " AND pa.p_id = ?";
	    }

	    if (patientName != null && !patientName.isEmpty()) {
	        sql += " AND pa.name LIKE ?";
	    }

	    if (department != null && !department.isEmpty()) {
	        sql += " AND dep.dep_name = ?";
	    }

	    // プリペアードステートメントを取得し、実行SQLを渡す
	    PreparedStatement statement = getPreparedStatement(sql);
	    statement.setString(1, userid);

	    // パラメータのインデックス
	    int parameterIndex = 2;

	    // 追加のパラメータがある場合、それらをプリペアードステートメントに設定する
	    if (patientId != null && !patientId.isEmpty()) {
	        statement.setString(parameterIndex++, patientId);
	    }

	    if (patientName != null && !patientName.isEmpty()) {
	        statement.setString(parameterIndex++, "%" + patientName + "%");
	    }

	    if (department != null && !department.isEmpty()) {
	        statement.setString(parameterIndex++, department);
	    }

	    System.out.println("担当患者情報：");
	    System.out.println(sql);
	    System.out.println(userid);
	    System.out.println(patientId);
	    System.out.println(patientName);
	    System.out.println(department);

	    // SQLを実行してその結果を取得する
	    ResultSet rs = statement.executeQuery();

		//検索結果の行数分フェッチを行い、取得結果をMRecordクラスのインスタンスdtoのリストに格納する
		while (rs.next()) {
			MRecord dto = new MRecord();
			dto.setP_id(rs.getInt("p_id"));
			dto.setName(rs.getString("name"));
			dto.setSex(rs.getString("sex"));
			dto.setAge(rs.getInt("age"));
			dto.setBirthday(rs.getString("birthday"));
			dto.setLastupdate(rs.getTimestamp("m_day"));
			dto.setDep_name(rs.getString("dep_name"));
			returnList.add(dto);
		}

		return returnList;

	}
	
	/*担当患者情報のデータを取得する（引数なし）*/
	public List<MRecord> getAssignedPatients(String userid) throws Exception {

		List<MRecord> returnList = new ArrayList<MRecord>();

		String sql = "SELECT pa.userid, pa.p_id, pa.name, pa.birthday, pa.sex, latest_m_rec.m_day, " +
	             "COALESCE(dep.dep_name, '') AS dep_name, " +
	             "TIMESTAMPDIFF(YEAR, pa.birthday, CURDATE()) - (DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(pa.birthday, '%m%d')) AS age " +
	             "FROM patient AS pa " +
	             "LEFT JOIN department AS dep ON dep.dep_id = pa.dep_id " +
	             "LEFT JOIN ( " +
	             "  SELECT p_id, m_day, ROW_NUMBER() OVER (PARTITION BY p_id ORDER BY m_day DESC) AS rn " +
	             "  FROM m_record " +
	             ") AS latest_m_rec " +
	             "ON pa.p_id = latest_m_rec.p_id AND latest_m_rec.rn = 1 " +
				 " WHERE userid = ?";

		
	    System.out.println("担当患者情報（引数ユーザIDのみ）：");
	    System.out.println(sql);

	    // プリペアードステートメントを取得し、実行SQLを渡す
	    PreparedStatement statement = getPreparedStatement(sql);
	    statement.setString(1, userid);

	    // SQLを実行してその結果を取得する
	    ResultSet rs = statement.executeQuery();

		//検索結果の行数分フェッチを行い、取得結果をMRecordクラスのインスタンスdtoのリストに格納する
		while (rs.next()) {
			MRecord dto = new MRecord();
			dto.setP_id(rs.getInt("p_id"));
			dto.setName(rs.getString("name"));
			dto.setSex(rs.getString("sex"));
			dto.setAge(rs.getInt("age"));
			dto.setBirthday(rs.getString("birthday"));
			dto.setLastupdate(rs.getTimestamp("m_day"));
			dto.setDep_name(rs.getString("dep_name"));
			returnList.add(dto);
		}

		return returnList;

	}
	
	/*所属診療科の患者情報のデータを取得する*/
	public List<MRecord> dep_patientList(String userid) throws Exception {

		List<MRecord> returnList = new ArrayList<MRecord>();

		String sql = "SELECT pa.p_id, name, birthday, sex, m_day, dep_name,"
				+ " TIMESTAMPDIFF(YEAR, birthday, CURDATE()) - (DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birthday, '%m%d')) AS age"
				+ " FROM patient AS pa"
				+ " LEFT JOIN m_record AS mre ON mre.p_id = pa.p_id"
				+ " LEFT JOIN department AS dep ON dep.dep_id = pa.dep_id"
				+ " WHERE pa.dep_id = (SELECT dep_id FROM users where userid = ?)"
				+ " AND mre.m_day = ("
				+ "    SELECT MAX(mr.m_day)"
				+ "    FROM m_record AS mr"
				+ "    WHERE mr.p_id = pa.p_id"
				+ " )";
		
		//プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		statement.setString(1, userid);

		//SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

	    System.out.println("所属診療科情報：");
	    System.out.println(sql);
	    
		//検索結果の行数分フェッチを行い、取得結果をMRecordクラスのインスタンスdtoのリストに格納する
		while (rs.next()) {
			MRecord dto = new MRecord();
			dto.setP_id(rs.getInt("p_id"));
			dto.setName(rs.getString("name"));
			dto.setSex(rs.getString("sex"));
			dto.setAge(rs.getInt("age"));
			dto.setBirthday(rs.getString("birthday"));
			dto.setLastupdate(rs.getTimestamp("m_day"));
			dto.setDep_name(rs.getString("dep_name"));
			returnList.add(dto);
		}

		return returnList;

	}
	
	/*指定されたキーワードを含む患者情報を取得する*/
	public List<MRecord> search_patientList(String patientId,String patientName,String department) throws Exception {

		List<MRecord> returnList = new ArrayList<MRecord>();

	    String sql = "SELECT pa.userid, pa.p_id, pa.name, pa.birthday, pa.sex, latest_m_rec.m_day, pa.Insurance_num," +
	            "COALESCE(dep.dep_name, '') AS dep_name, " +
	            "TIMESTAMPDIFF(YEAR, pa.birthday, CURDATE()) - (DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(pa.birthday, '%m%d')) AS age " +
	            "FROM patient AS pa " +
	            "LEFT JOIN department AS dep ON dep.dep_id = pa.dep_id " +
	            "LEFT JOIN ( " +
	            "  SELECT p_id, m_day, ROW_NUMBER() OVER (PARTITION BY p_id ORDER BY m_day DESC) AS rn " +
	            "  FROM m_record " +
	            ") AS latest_m_rec " +
	            "ON pa.p_id = latest_m_rec.p_id AND latest_m_rec.rn = 1 " +
	            "WHERE (pa.p_id LIKE ? OR ? IS NULL) " +
	            "AND (pa.name LIKE ? OR ? IS NULL) " +
	            "AND (dep.dep_name LIKE ? OR ? IS NULL)";
		
		System.out.println("キーワード検索：");
		System.out.println(sql);
		
		//プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
        statement.setString(1, "%" + patientId + "%");
        statement.setString(2, patientId.isEmpty() ? null : "%" + patientId + "%");
        statement.setString(3, "%" + patientName + "%");
        statement.setString(4, patientName.isEmpty() ? null : "%" + patientName + "%");
        statement.setString(5, "%" + department + "%");
        statement.setString(6, department.isEmpty() ? null : "%" + department + "%");
		//SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

		//検索結果の行数分フェッチを行い、取得結果をMRecordクラスのインスタンスdtoのリストに格納する
		while (rs.next()) {
			MRecord dto = new MRecord();
			dto.setUserid(rs.getString("userid"));
			dto.setP_id(rs.getInt("p_id"));
			dto.setName(rs.getString("name"));
			dto.setAge(rs.getInt("age"));
			dto.setSex(rs.getString("sex"));
			dto.setBirthday(rs.getString("birthday"));
			dto.setLastupdate(rs.getTimestamp("m_day"));
			dto.setDep_name(rs.getString("dep_name"));
			dto.setInsurance_num(rs.getString("insurance_num"));
			returnList.add(dto);
		}

		return returnList;

	}
	
	/*指定された患者IDの患者情報を取得する*/
	public MRecord id_patientSearch(int id) throws Exception {
		String sql = "SELECT p_id, name, sex, birthday, Insurance_num FROM patient"
				+ " WHERE p_id = ?";
		
		System.out.println("患者ID検索：");
		System.out.println(sql);
		
		// プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		statement.setInt(1, id);
		
		// SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

		// 検索結果をTodoクラスのインスタンスdtoへ格納する
		MRecord dto = new MRecord();
		while (rs.next()) {
			// クエリ結果をDTOへ格納
			dto.setP_id(rs.getInt("p_id"));
			dto.setName(rs.getString("name"));
			dto.setSex(rs.getString("sex"));
			dto.setBirthday(rs.getString("birthday"));
			dto.setInsurance_num(rs.getString("insurance_num"));
		}
		return dto;
	}
	
	/*指定されたユーザの診療科IDを取得する*/
	public int getDepartmentId(String username) throws Exception {
		String sql = "SELECT dep.dep_id FROM users " +
	            	 "LEFT JOIN department AS dep ON dep.dep_id = users.dep_id " +
	            	 "WHERE userid = ?";
		
		System.out.println("診療科ID取得：");
		System.out.println(sql);
		
		// プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		statement.setString(1, username);
		
		// SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

	    if (rs.next()) {
	        // dep_id を取得して返す
	        return rs.getInt("dep_id");
	    } else {
	        // dep_id が見つからなかった場合、適切な値を返すか例外を投げる
	        throw new Exception("指定されたユーザの診療科IDが見つかりませんでした: " + username);
	    }
	}
	
	/*指定されたユーザの診療科を取得する*/
	public MRecord getDepartment(String username) throws Exception {
		String sql = "SELECT dep_name FROM users " +
	            	 "LEFT JOIN department AS dep ON dep.dep_id = users.dep_id " +
	            	 "WHERE userid = ?";
		
		System.out.println("診療科取得：");
		System.out.println(sql);
		
		// プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		statement.setString(1, username);
		
		// SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

		// 検索結果をTodoクラスのインスタンスdtoへ格納する
		MRecord dto = new MRecord();
		while (rs.next()) {
			// クエリ結果をDTOへ格納
			dto.setDep_name(rs.getString("dep_name"));
		}
		return dto;
	}
	
	/*指定された患者カルテデータを取得する*/
	public MRecord detail(int id) throws Exception {
		String sql = "SELECT pa.p_id, name, birthday, sex, userid, m_rec.m_id, dis_name, details, m_day FROM patient AS pa"
				+ " LEFT JOIN m_record AS m_rec ON m_rec.p_id = pa.p_id"
				+ " LEFT JOIN disease AS dis ON dis.dis_id = pa.dis_id"
				+ " where pa.p_id = ?";

		System.out.println("カルテ情報取得：");
		System.out.println(sql);
		
		// プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		statement.setInt(1, id);
		// SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

		// 検索結果をTodoクラスのインスタンスdtoへ格納する
		MRecord dto = new MRecord();
		while (rs.next()) {
			// クエリ結果をDTOへ格納
			dto.setP_id(rs.getInt("p_id"));
			dto.setName(rs.getString("name"));
			dto.setSex(rs.getString("sex"));
			dto.setBirthday(rs.getString("birthday"));
			dto.setLastupdate(rs.getTimestamp("m_day"));
			dto.setDis_name(rs.getString("dis_name"));
			dto.setUserid(rs.getString("userid"));

		}
		return dto;
	}

	/*指定された患者カルテデータを取得する*/
	public List<MRecord> detailList(int id) throws Exception {
		
		List<MRecord> returnList = new ArrayList<MRecord>();

		String sql = "SELECT pa.p_id, pa.name, birthday, sex, m_rec.m_id, dis_name, details, m_day, treatment, inusers.name AS 主治医, exusers.name AS 診察医, image,"
				+ " TIMESTAMPDIFF(YEAR, pa.birthday, CURDATE()) - (DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(pa.birthday, '%m%d')) AS age"
				+ " FROM patient AS pa"
				+ " LEFT JOIN m_record AS m_rec ON m_rec.p_id = pa.p_id"
				+ " LEFT JOIN disease AS dis ON dis.dis_id = m_rec.dis_id"
				+ " LEFT JOIN users AS inusers ON inusers.userid = pa.userid"
				+ " LEFT JOIN users AS exusers ON exusers.userid = m_rec.ex_doctor"
				+ " WHERE pa.p_id = ?"
				+ " ORDER BY m_day DESC";

		System.out.println("カルテ情報取得：");
		System.out.println(sql);
		
		// プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		statement.setInt(1, id);
		// SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

		//検索結果の行数分フェッチを行い、取得結果をMRecordクラスのインスタンスdtoのリストに格納する
		while (rs.next()) {
			MRecord dto = new MRecord();
			dto.setP_id(rs.getInt("p_id"));
			dto.setName(rs.getString("name"));
			dto.setSex(rs.getString("sex"));
			dto.setBirthday(rs.getString("birthday"));
			dto.setLastupdate(rs.getTimestamp("m_day"));
			dto.setDis_name(rs.getString("dis_name"));
			dto.setDoctor_name(rs.getString("主治医"));
			dto.setEx_doctor(rs.getString("診察医"));
			dto.setTreatment(rs.getString("treatment"));
			dto.setDetail(rs.getString("details"));
			dto.setAge(rs.getInt("age"));
			dto.setFile_path(rs.getString("image"));
			returnList.add(dto);
		}
		return returnList;
	}
	
	/*病名リストを取得する*/
	public List<MRecord> getDisease() throws Exception {
		List<MRecord> returnList = new ArrayList<MRecord>();

		String sql = "SELECT dis_id, dis_name FROM disease ";
		
		System.out.println("病名リスト取得：");
		System.out.println(sql);
		
		// プリペアードステートメントを取得し、実行SQLを渡す
		PreparedStatement statement = getPreparedStatement(sql);
		
		// SQLを実行してその結果を取得する
		ResultSet rs = statement.executeQuery();

		// 検索結果をTodoクラスのインスタンスdtoへ格納する
		while (rs.next()) {
			MRecord dto = new MRecord();
			dto.setDis_id(rs.getInt("dis_id"));
			dto.setDis_name(rs.getString("dis_name"));
			returnList.add(dto);
		}
		return returnList;
	}
	
	/*ユーザ新規登録処理*/
	public int userInsert(MRecord dto) throws Exception {
		String sql = "INSERT INTO users (userid,password,name,dep_id,role_id) VALUES (?,?,?,?,?)";
		System.out.println("ユーザ新規登録");
		System.out.println(sql);

		
	    //sqlを実行　結果取得
		int result = 0;
		try {
			//プリペアードステートメント取得　実行SQLを渡す
	        PreparedStatement statement = getPreparedStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			statement.setString(1, dto.getUserid());
			statement.setString(2, dto.getPassword());
			statement.setString(3, dto.getName());
			statement.setInt(4, dto.getDep_id());
			statement.setString(5, dto.getRole());
			result = statement.executeUpdate();
	        
			//コミット
			super.commit();

			return result;

		} catch (Exception e) {
			//例外の場合ロールバックを行いスローした例外はデータオブジェクトから脱出する
			super.rollback();
			throw e;
		}
	}
	
	/*患者新規登録処理*/
	public int patientInsert(MRecord dto) throws Exception {
		String sql = "INSERT INTO patient (name,birthday,sex,Insurance_num,new_flg) VALUES (?,?,?,?,0)";
		System.out.println("患者新規登録");
		System.out.println(sql);

		// オートインクリメントされたIDを格納する変数
	    int generatedId = 0;
		
	    //sqlを実行　結果取得
		int result = 0;
		try {
			//プリペアードステートメント取得　実行SQLを渡す
	        PreparedStatement statement = getPreparedStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			statement.setString(1, dto.getName());
			statement.setString(2, dto.getBirthday());
			statement.setString(3, dto.getSex());
			statement.setString(4, dto.getInsurance_num());
			result = statement.executeUpdate();
			
	        // オートインクリメントされたIDを取得
	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                generatedId = generatedKeys.getInt(1);
	            }
	        }
	        
			//コミット
			super.commit();

			return generatedId;

		} catch (Exception e) {
			//例外の場合ロールバックを行いスローした例外はデータオブジェクトから脱出する
			super.rollback();
			throw e;
		}
	}
	
	/*カルテ新規登録処理*/
	public int detailInsert(MRecord dto) throws Exception {
	    
		// 現在の日時を取得
	    Timestamp currentTimestamp = new Timestamp(new Date().getTime());
	    
		String sql = "INSERT INTO m_record (details, p_id, ex_doctor, treatment, dis_id, m_day) VALUES (?,?,?,?,?,?)";
		System.out.println("カルテ新規登録");
		System.out.println(sql);

        System.out.println(dto.getDetail());
        System.out.println(dto.getP_id());
        System.out.println(dto.getEx_doctor());
        System.out.println(dto.getTreatment());
        System.out.println(dto.getDis_id());
        System.out.println(currentTimestamp);

        
		// オートインクリメントされたカルテIDを格納する変数
	    int generatedId = 0;
	    
	    //sqlを実行　結果取得
		int result = 0;
		try {
			//プリペアードステートメント取得　実行SQLを渡す
	        PreparedStatement statement = getPreparedStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			statement.setString(1, dto.getDetail());
			statement.setInt(2, dto.getP_id());
			statement.setString(3, dto.getEx_doctor());
			statement.setString(4, dto.getTreatment());
			statement.setInt(5, dto.getDis_id());
	        statement.setTimestamp(6, currentTimestamp);  // 現在の日時を設定

			result = statement.executeUpdate();
	        
	        // オートインクリメントされたIDを取得
	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                generatedId = generatedKeys.getInt(1);
	            }
	        }
	        
			//コミット
			super.commit();

			return generatedId;

		} catch (Exception e) {
			//例外の場合ロールバックを行いスローした例外はデータオブジェクトから脱出する
			super.rollback();
			throw e;
		}
	}
	
	/*患者情報更新処理*/
	public int patientUpdate(MRecord dto) throws Exception {
        String sql = "UPDATE patient SET name = ?, birthday = ?, sex = ?, insurance_num = ? WHERE p_id = ?";
		System.out.println("患者情報更新");
		System.out.println(sql);
		
	    //sqlを実行　結果取得
		int result = 0;
		try {
			//プリペアードステートメント取得　実行SQLを渡す
	        PreparedStatement statement = getPreparedStatement(sql);
			statement.setString(1, dto.getName());
			statement.setString(2, dto.getBirthday());
			statement.setString(3, dto.getSex());
			statement.setString(4, dto.getInsurance_num());
	        statement.setInt(5, dto.getP_id());
			result = statement.executeUpdate();
	        
			//コミット
			super.commit();

			return result;

		} catch (Exception e) {
			//例外の場合ロールバックを行いスローした例外はデータオブジェクトから脱出する
			super.rollback();
			throw e;
		}
	}
	
	/*初診時更新処理（診療科、主治医、新患フラグ）*/
	public int first_patientUpdate(MRecord dto) throws Exception {
        String sql = "UPDATE patient SET dep_id = ?, userid = ?, new_flg = 1"
        		   + " WHERE p_id = ?";
        
		System.out.println("初診時情報更新");
		System.out.println(sql);
		
	    //sqlを実行　結果取得
		int result = 0;
		try {
			//プリペアードステートメント取得　実行SQLを渡す
	        PreparedStatement statement = getPreparedStatement(sql);
			statement.setInt(1, dto.getDep_id());
			statement.setString(2, dto.getEx_doctor());
			statement.setInt(3, dto.getP_id());
			result = statement.executeUpdate();
	        
			//コミット
			super.commit();

			return result;

		} catch (Exception e) {
			//例外の場合ロールバックを行いスローした例外はデータオブジェクトから脱出する
			super.rollback();
			throw e;
		}
	}
	
	/*画像ファイルパス更新処理*/
	public int updateFilePath(int m_id, String file_path) throws Exception {
        String sql = "UPDATE m_record SET image = ? WHERE m_id = ?";
		System.out.println("画像パス更新");
		System.out.println(sql);
		
	    //sqlを実行　結果取得
		int result = 0;
		try {
			//プリペアードステートメント取得　実行SQLを渡す
	        PreparedStatement statement = getPreparedStatement(sql);
			statement.setString(1, file_path);
			statement.setInt(2, m_id);
			result = statement.executeUpdate();
	        
			//コミット
			super.commit();

			return result;

		} catch (Exception e) {
			//例外の場合ロールバックを行いスローした例外はデータオブジェクトから脱出する
			super.rollback();
			throw e;
		}
	}
	
	/* 指定された患者IDのデータを削除する */
	public int patientDelete(int id) throws Exception {
		String deletePatientSql = "DELETE FROM patient WHERE p_id = ?";
	    String deleteRecordsSql = "DELETE FROM m_record WHERE p_id = ?";

		// sqlを実行してその結果を取得する
		int result = 0;
		try {
			 // プリペアステートメントを取得し、実行SQLを渡す
	        PreparedStatement deleteRecordsStmt = getPreparedStatement(deleteRecordsSql);
	        PreparedStatement deletePatientStmt = getPreparedStatement(deletePatientSql);
	        
	        // IDをセット
	        deleteRecordsStmt.setInt(1, id);
	        deletePatientStmt.setInt(1, id);
	        
	        // まずm_recordのレコードを削除
	        deleteRecordsStmt.executeUpdate();
	        
	        // 次にpatientのレコードを削除
	        result = deletePatientStmt.executeUpdate();
	        
	        // コミット
	        super.commit();
		} catch (Exception e) {
			//例外の場合ロールバックを行いスローした例外はデータオブジェクトから脱出する
			super.rollback();
			throw e;
		}
		return result;
	}
}
