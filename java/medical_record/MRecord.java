package medical_record;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class MRecord implements Serializable {
	// フィールド
	private String userid; 		//ユーザＩＤ
	private int p_id; 			//患者ID
	private String name; 		//患者名
	private String birthday; 	//患者生年月日
	private String sex; 		//患者性別
	private int dep_id;			//診療科ID
	private int dis_id; 		//病名ID
	private String dis_name; 	//病名
	private Timestamp lastupdate; //前回受診日	
	private int m_id; 			//カルテID
	private String details; 	//カルテ内容
	private String impressions; //検査所見
	private String image; 		//画像ファイルパス
	private int age; 			//患者年齢
	private String role; 		//ユーザのロール
	private String dep_name; 	//診療科名
	private List<String> errorMessages;  //errorメッセージ
	private String insurance_num; //保険証番号
	private String Doctor_name; //医者名
	private String Ex_doctor; 	//診察医
	private String Treatment; 	//処置内容
	private String Detail; 		//医師所見
	private String file_path; 	//画像ファイル名
	private String hash_pass; 	//ハッシュ化パスワード
	private String password; 	//パスワード
	private int new_patient; 	//新患フラグ
	
	//アクセッサ
	public int getNew_patient() {
		return new_patient;
	}

	public void setNew_patient(int new_patient) {
		this.new_patient = new_patient;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getHash_pass() {
		return hash_pass;
	}

	public void setHash_pass(String hash_pass) {
		this.hash_pass = hash_pass;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	
	public String getEx_doctor() {
		return Ex_doctor;
	}

	public void setEx_doctor(String Ex_doctor) {
		this.Ex_doctor = Ex_doctor;
	}
	
	public String getDetail() {
		return Detail;
	}

	public void setDetail(String Detail) {
		this.Detail = Detail;
	}
	
	public String getTreatment() {
		return Treatment;
	}

	public void setTreatment(String Treatment) {
		this.Treatment = Treatment;
	}
	
	public String getDoctor_name() {
		return Doctor_name;
	}

	public void setDoctor_name(String Doctor_name) {
		this.Doctor_name = Doctor_name;
	}

	public String getInsurance_num() {
		return insurance_num;
	}

	public void setInsurance_num(String insurance_num) {
		this.insurance_num = insurance_num;
	}
	
	public String getDep_name() {
		return dep_name;
	}

	public void setDep_name(String dep_name) {
		this.dep_name = dep_name;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public int getM_id() {
		return m_id;
	}

	public void setM_id(int m_id) {
		this.m_id = m_id;
	}
	
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public String getImpressions() {
		return impressions;
	}

	public void setImpressions(String impressions) {
		this.impressions = impressions;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public int getP_id() {
		return p_id;
	}

	public void setP_id(int p_id) {
		this.p_id = p_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Timestamp getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(Timestamp lastupdate) {
		this.lastupdate = lastupdate;
	}

	public int getDep_id() {
		return dep_id;
	}

	public void setDep_id(int dep_id) {
		this.dep_id = dep_id;
	}

	public int getDis_id() {
		return dis_id;
	}

	public void setDis_id(int dis_id) {
		this.dis_id = dis_id;
	}
	
	public String getDis_name() {
		return dis_name;
	}

	public void setDis_name(String dis_name) {
		this.dis_name = dis_name;
	}
	
	//errorメッセージを返却する
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	//errorメッセージを設定する
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	
}
