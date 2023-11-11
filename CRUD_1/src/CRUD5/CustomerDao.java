package CRUD5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDao {
	DBConnectionMgr dbMgr = null;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	CustomerDao() {
		dbMgr = DBConnectionMgr.getInstance();
	}

	public List<Map<String, Object>> updateList(int c_id, String c_name, String c_birthday) { // 수정
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement("UPDATE customerDao SET c_name = ?, c_birthday = ? WHERE c_id = ?");
			pstmt.setString(1, c_name);
			pstmt.setString(2, c_birthday);
			pstmt.setInt(3, c_id);
			pstmt.executeUpdate(); 
		} catch (Exception e) {
			e.printStackTrace(); // 디버깅 할때 꼭 필요한 메소드!!
		}
		return null;
	}
	public List<Map<String, Object>> selectList(int c_id) { // 한 건 조회
		List<Map<String, Object>> selecList = new ArrayList<>();
		Map<String, Object> selecMap = null;
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement("SELECT c_id, c_name, c_birthday FROM customerDao WHERE c_id=?");
			pstmt.setInt(1, c_id);
			rs = pstmt.executeQuery(); // 오라클 서버에게 select처리
			if (rs.next()) {
				selecMap = new HashMap<>();
				selecMap.put("c_id", rs.getInt("c_id"));
				selecMap.put("c_name", rs.getString("c_name"));
				selecMap.put("c_birthday", rs.getString("c_birthday"));
				selecList.add(selecMap);
			}
			// IndexOutOfBoundsException 이 터져서 list1에 DB 값이 제대로 담기는지 확인해보았다.. 
//			System.out.println(list1.get(0)); // 단위테스트
		} catch (Exception e) {
			e.printStackTrace(); // 디버깅 할때 꼭 필요한 메소드!!
		}
		return selecList;
	}

	public List<Map<String, Object>> selectAllList() { // 전체 조회 // 먼저 연습 후 Map 연습 필수
		List<Map<String, Object>> selecAList = new ArrayList<>();
		Map<String, Object> selecAMap = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c_id, c_name, c_birthday FROM customerDao"); // customerDao 테이블 모두 조회
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				selecAMap = new HashMap<>();
				selecAMap.put("c_id", rs.getInt("c_id"));
				selecAMap.put("c_name", rs.getString("c_name"));
				selecAMap.put("c_birthday", rs.getString("c_birthday"));
				selecAList.add(selecAMap);
			}

		} catch (SQLException se) {
			System.out.println(se.toString());
		} catch (Exception e) {
			e.printStackTrace(); // stack메모리에 쌓여있는 에러 메시지 히스토리를 볼 수 있다. (라인 번호와 함께 메시지가 출력된다.)
		}
		return selecAList;
	}

	public List<Map<String, Object>> insertList(List<Map<String, Object>> list) { // 입력
		con = dbMgr.getConnection();
		List<Map<String, Object>> instList = list;
		Map<String, Object> instMap = null;
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO customerDao (c_id, c_name, c_birthday) VALUES (?, ?, ?)");
		try {
			pstmt = con.prepareStatement(sql.toString());
			instMap = instList.get(0);
			pstmt.setObject(1, instMap.get("c_id"));
			pstmt.setObject(2, instMap.get("c_name"));
			pstmt.setObject(3, instMap.get("c_birthday"));
			pstmt.executeUpdate();
			con.close();

		} catch (Exception e) {
//			System.out.println("이미 존재하는 no. 입니다.");
			e.printStackTrace(); // stack메모리에 쌓여있는 에러 메시지 히스토리를 볼 수 있다. (라인 번호와 함께 메시지가 출력된다.)
		}
		return instList;
	}

	public boolean deleteList(Map<String, Object> selectRowNum) { // 삭제
		con = dbMgr.getConnection();
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM customerDao WHERE c_id = ?");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setObject(1, selectRowNum.get("c_id")); 
			pstmt.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace(); // stack메모리에 쌓여있는 에러 메시지 히스토리를 볼 수 있다. (라인 번호와 함께 메시지가 출력된다.)
		}
		boolean isOk = false;
		return isOk;
	}

	public static void main(String[] args) {

	}
}