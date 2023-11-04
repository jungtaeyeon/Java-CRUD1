# # Java-CRUD_1
DB연동 CRUD 연습1

# # 프로젝트 목표
- Java의 JFrame을 활용하여 DB연동을 해 간단한 CRUD 연습하기 !!
- class 나누는 연습하기 !!

# # 주요 기능
💡 데이터 추가
  - JButton을 이용해 구현한 "추가" 버튼을 누르면 JTextFiled에 입력한 데이터가 입력되도록 구현

💡 데이터 삭제
  - 한 개의 row를 선택 후 JButton을 이용해 구현한 "삭제" 버튼을 누르면 선택한 row의 데이터가 삭제되도록 구현

💡 데이터 전체조회
  - JButton을 이용해 구현한 "전체조회" 버튼을 누르면 MySQL에 있는 테이블의 데이터 전체 조회가 되도록 구현

💡 데이터 1건 조회
  - JButton을 이용해 구현한 "한 건 조회" 버튼을 누르면 JTextFiled에 입력한 primaryKey의 데이터만 조회가 되도록 구현

💡 데이터 수정
  - 구현 예정!

# # CRUD1 문제점 및 개선해야할 점 -> 개선 후 CRUD2 커밋 예정
1. 선택한 row의 데이터를 삭제해줘야 하는데, 다른 row가 삭제됨
  - CustomerVO에 getSelectedRow()를 바로 넘겨주어, primaryKey와 getSelectedRow()의 인덱스 값이 달라서 선택한 row가 아닌 다른 row가 삭제되는 문제.
  - getSelectedRow()로 선택한 row의 고객 No를 가져와서 int로 선언 후 그 값을 CustomerVO의 set 메소드를 이용해 넘겨서 해결하기
  
2. CusomerVO에는 기본적인 getter, setter 메소드 외에 다른 getter, setter 메소드를 만들어 사용해서 코드가 재사용성이 떨어지고 유지보수가 어려움 (1과 같은 이슈도 발생함.)

3. 현재 선택한 row 삭제 시, DB에서 데이터가 삭제되는 코드 따로, DefaultTableModel에서 삭제되는 코드 따로 이렇게 구현함...
  - 데이터 수정까지 구현 한 다음 선택한 row 삭제 후 DB에서 테이블에 삭제된 데이터를 불러오는 방식으로 바꿀 예정 !!!


# # CRUD2 문제점 및 개선해야할 점 -> 개선 후 CRUD3 커밋 예정
1. CRUD1에서 2번 개선사항 개선 후 ClassCastException이 터졌다.. CustomerManager.java:197 라인 해결 필요


   






