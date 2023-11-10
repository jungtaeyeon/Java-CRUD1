package CRUD4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
	DBConnectionMgr dbMgr = null;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	CustomerDao() {
		dbMgr = DBConnectionMgr.getInstance();
	}

	public CustomerVO updateCVO(int c_id, String c_name, String c_birthday) { // 한 건 조회
//		CustomerVO rcVO = null;
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
	public CustomerVO selectCVO(int c_id) { // 한 건 조회
		CustomerVO rcVO = null;
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement("SELECT c_id, c_name, c_birthday FROM customerDao WHERE c_id=?");
			pstmt.setInt(1, c_id);
			rs = pstmt.executeQuery(); // 오라클 서버에게 select처리
			if (rs.next()) {
				rcVO = new CustomerVO();
				rcVO.setC_id(rs.getInt("c_id"));
				rcVO.setC_name(rs.getString("c_name"));
				rcVO.setC_birthday(rs.getString("c_birthday"));
			}
		} catch (Exception e) {
			e.printStackTrace(); // 디버깅 할때 꼭 필요한 메소드!!
		}
		return rcVO;
	}

	public List<CustomerVO> getCVOList() { // 전체 조회 // 먼저 연습 후 Map 연습 필수
		List<CustomerVO> list = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c_id, c_name, c_birthday FROM customerDao"); // customerDao 테이블 모두 조회
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			CustomerVO cvo = null;
			while (rs.next()) {
				// 아래 코드에서 반복문이 실행될 때마다 서로 다른 주소번지가 4개 만들어 지니까
				cvo = new CustomerVO(rs.getInt("c_id"), rs.getString("c_name"), rs.getString("c_birthday"));
				// 아래 코드를 작성하지 않으면 4개의 정보가 모두 유지되지 않음
				list.add(cvo);
			}

		} catch (SQLException se) {
			System.out.println(se.toString());
		} catch (Exception e) {
			e.printStackTrace(); // stack메모리에 쌓여있는 에러 메시지 히스토리를 볼 수 있다. (라인 번호와 함께 메시지가 출력된다.)
		}
		return list;
	}

	public CustomerVO insertCVOList(CustomerVO cVO) { // 입력
		con = dbMgr.getConnection();
		CustomerVO rcVO = null;
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO customerDao (c_id, c_name, c_birthday) VALUES (?, ?, ?)");
		try {
			rcVO = cVO;
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, rcVO.getC_id());
			pstmt.setString(2, rcVO.getC_name());
			pstmt.setString(3, rcVO.getC_birthday());
			pstmt.executeUpdate();
			con.close();

		} catch (Exception e) {
			System.out.println("이미 존재하는 no. 입니다.");
//			e.printStackTrace(); // stack메모리에 쌓여있는 에러 메시지 히스토리를 볼 수 있다. (라인 번호와 함께 메시지가 출력된다.)
		}
		return rcVO;
	}

	public CustomerVO deleteCVOList(CustomerVO cVO) { // 삭제
		con = dbMgr.getConnection();
		CustomerVO rcVO = null;
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM customerDao WHERE c_id = ?");
		try {
			rcVO = cVO;
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, rcVO.getC_id()); 
			pstmt.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace(); // stack메모리에 쌓여있는 에러 메시지 히스토리를 볼 수 있다. (라인 번호와 함께 메시지가 출력된다.)
		}
		return rcVO;
	}

	public static void main(String[] args) {

	}
}