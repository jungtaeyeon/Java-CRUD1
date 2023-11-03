package CRUD1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
	DBConnectionMgr dbMgr =  null;
	Connection con = null;
	PreparedStatement pstmt = null; // 쿼리문을 스캔. select:pstmt.executeQuery():ResultSet, insert | update | delete -> executeUpdate():int
	ResultSet rs = null;
	CustomerDao(){
		dbMgr = DBConnectionMgr.getInstance();
//		System.out.println("CustomerDao() : "+dbMgr);
//		selectCVO(20);
	}
	public CustomerVO selectCVO(int c_id) { // 한 건 조회
//		System.out.println("test() : " + dbMgr);
//		System.out.println(con);
		CustomerVO rcVO = null;
		try {
			// 인터페이스가 getConnection메소드 호출을 통해서 주소번지를 갖게 되었다.(메모리 상주하게됨) -> NullPointException
			con = dbMgr.getConnection();
//			System.out.println(con);
			pstmt = con.prepareStatement("SELECT c_id, c_name, c_birthday FROM customerDao WHERE c_id=?");
			pstmt.setInt(1, c_id);
			rs = pstmt.executeQuery(); // 오라클 서버에게 select처리해줘.
			if(rs.next()) {
				rcVO = new CustomerVO();
				rcVO.setC_id(rs.getInt("c_id"));
				rcVO.setC_name(rs.getString("c_name"));
				rcVO.setC_birthday(rs.getString("c_birthday"));
			}
//			System.out.println(rs.next());
//			System.out.println(rs.isFirst());
//			System.out.println(rs.next());
//			System.out.println(rs.isLast());
		} catch (Exception e) {
			e.printStackTrace();  // 디버깅 할때 꼭 필요한 메소드!! 외우기!!!
		}
		return rcVO;
	}
	public List<CustomerVO> getCVOList(){ // 전체 조회      //  먼저 연습 후 Map 연습 필수
//		System.out.println("제네릭 타입을 getter / setter로 처리할 때");
		List<CustomerVO> list = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c_id, c_name, c_birthday FROM customerDao"); // customerDao 테이블 모두 조회
		try {
			// 아래 코드에서 NullPointerException이 발생했다면 생성자에서 객체 주입이 안된 것.
			// dbMgr. 코드에서 직접적인 원인이 있음.
			// DBConnectionMgr이 생성되어야 getConnection메소드를 호출할 수 있을 것이고
			// 호출이 되어야 리턴값으로 Connection 객체를 주입 받음
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			CustomerVO cvo = null;
			while(rs.next()) {
				// 아래 코드에서 반복문이 실행될 때마다 서로 다른 주소번지가 4개 만들어 지니까
				// 문제점 - DeptDTO는 테이블 dept테이블을 클래스로 설계한 것이다.
				cvo = new CustomerVO(rs.getInt("c_id"), rs.getString("c_name"), rs.getString("c_birthday"));
				// 아래 코드를 작성하지 않으면 4개의 정보가 모두 유지되지 않음
				list.add(cvo);
			}
			// 자바를 통해 DB연동 후 후처리 하기.(자바 컬렉션 프레임워크를 JSON포맷으로 변경함.)
//			Gson g = new Gson();   //  해당 코드는 브라우저를 통해서 출력할 때만 사용하면 된다.
//			String temp = g.toJson(list); // JSON포맷이어야 javascript에서 꺼내기가 가능함
		} catch (SQLException se) {
			System.out.println(se.toString()); // 부적합한 식별자 - 컬럼명이 존재하지 않을 때 - SQLException 해당됨
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
		return rcVO; // 요거 사용하기 !!!!!!!!!!!! (사용 안하고 지금 insert됨)
	}
	public CustomerVO deleteCVOList(CustomerVO cVO) { // 삭제
		con = dbMgr.getConnection();
		CustomerVO rcVO = null;
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM customerDao WHERE c_id = ?"); 
		try {
			rcVO = cVO;
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, rcVO.getC_id()); // +1 안해주면 선택한 row의 위가 삭제됨....
			pstmt.executeUpdate();
			con.close();	
//			System.out.println("22delete");
		} catch (Exception e) {
			e.printStackTrace(); // stack메모리에 쌓여있는 에러 메시지 히스토리를 볼 수 있다. (라인 번호와 함께 메시지가 출력된다.)
		}
		return rcVO;
	}
	
	public static void main(String[] args) {
		
	}
}