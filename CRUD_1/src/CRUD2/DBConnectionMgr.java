package CRUD2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnectionMgr {
	// null로 초기화하는 이유는 null 체크를 해서 null일 때만 새로 인스턴스화 하고
	// null 아니면 그냥 계속 사용하자
	static DBConnectionMgr dbMgr = null;  //  왜 static 인가요?? 싱글톤 패턴으로 흉내내보기
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	public static final String _DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String _URL = "jdbc:mysql://localhost:3306/blog?serverTimezone=Asia/Seoul";
	public static final String _USER = "myblog";
	public static final String _PW = "abcd1234";

	// Calendar 클래스처럼 메소드를 통해서 객체 주입 받기 (세련된 표현이다.) -> 객체에 대한 라이프 사이클 관리 - 개발자가 -> Spring 넘김 -> 제어역전이라고 한다.
	public static DBConnectionMgr getInstance() { // 공유 -> 하나이다. -> 복제본을 만들지 않겠다.
		// 메소드를 통해서 객체생성을 하므로 if문 사용해서 조건별 객체생성이 가능해짐
		if (dbMgr == null)
			dbMgr = new DBConnectionMgr(); // 전역변수에 대한 null체크 후 객체를 생성함.
		// 메소드 앞에 static을 붙여서 추가 인스턴스화 없이 직접 메소드 호출이 가능하도록 설계하였다. -> 싱글톤 패턴과 직접적인 관련
		return dbMgr;
	}

	// 리턴 타입으로 연결통로를 확보한 con을 얻는다
	// Connection(url, 계정 정보 일치 - 인증실패, 에러 코드) -> PrepareStatement(쿼리문 전달, 처리 요청함 ->
	// 앞에 객체가 주입되지 않으면 나머지 뒤에는 모두 null인 상태에 놓인다.
	// 메소드 구현의 리턴 타입을 참조형으로 구현할 수 있다.|없다.? -> 공통팀 -> 메소드는 대체로 객체 주입인 경우가 많음
	public Connection getConnection() { // 리턴타입이 인터페이스 이다. -> 확장성이 좋다. -> 결합도를 낮춰준다. -> 독립적이다. -> 단위테스트와 통합테스트가 가능하다.(각 브랜치 머지)
		// 예외처리시 try..catch블록을 사용하는데 멀티 블록이 가능함 : 단 하위에서 상위클래스로 처리함
		try {
			// 각 제조사의 드라이버 클래스를 로딩하기 -> 오라클(mysql) jar -> 문자열로써 객체 주입 받아낸다. -> 인터페이스가 필요함
			// 그 인터페이스를 제조사가 제공해야 한다. - 노출 시  핵심기술 유출
			Class.forName("com.mysql.cj.jdbc.Driver");  // java reflection api공부 - F/W만들 수 있다.
			// 물리적으로 떨어져있는 오라클 서버와 연결통로 확보
			// getConnection메소드의 원형도 static이 붙어있다. -> 하나다. -> 복제 허용 x  -> 하나로 사용하고 반납하고 또 사용한다. -> 싱글톤
			// getConnection메소드 호출때문에 예외처리를 하였다. - try...catch 블록
			// 이유는 URL이 존재하지 않을 경우 (예외사항이 존재할 경우) -> 런타임에러(실행에러) -> 논리의 오류도 포함(비교연산자)
			// 이런 사이드 이펙트가 발생하지 않도록 (예방코드) -> 신뢰도 -> 지역변수를 사용하는게 좋다. -> 람다식, arrow function
			con = DriverManager.getConnection(_URL, _USER, _PW); // 파라미터가 3개이다.
		} catch (ClassNotFoundException ce) {
			System.out.println("ojdbc6.jar를 설정하지 않았다. 그래서 클래스를 못찾는다.");
		} catch (Exception e) {// 비번이 틀린경우,
			e.printStackTrace();
		}
		return con;
	}

	// 위 코드에서 22번과 24번 호출시 에러가 없다면 catch블록은 경유하지 않는다.
	public static void freeConnection(ResultSet rs, PreparedStatement pstmt, Connection con) {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void freeConnection(PreparedStatement pstmt, Connection con) {
		try {
			if (pstmt != null)
				pstmt.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void freeConnection(ResultSet rs, CallableStatement cstmt, Connection con) {
		try {
			if (rs != null)
				rs.close();
			if (cstmt != null)
				cstmt.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void freeConnection(CallableStatement cstmt, Connection con) {
		try {
			if (cstmt != null)
				cstmt.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
