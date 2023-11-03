package CRUD1;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CustomerManager extends JFrame implements ActionListener{
	List<CustomerVO> cList = null; // 전변은 언제 스캔이 되나요?
	CustomerDao cDao = null;
	CustomerVO cVO = null;
	
	List<Map<String, Object>> deptList = new ArrayList<>(); // 왜 전역변수로 하는가? 입력|수정|삭제|조회 유지되어야 하기 때문에
	String header[] = {"고객 No.", "고객 이름", "고객 생일"};
	String datas[][] = new String[0][0]; // 2차배열  - 대괄호가 2쌍이 필요하다.
	// 생성자의 파라미터를 통해서 서로 다른 클래스가 의존관계를 맺고 하나의 기능을 서비스 할 수 있다.
	// 생성자도 파라미터를 여러개 가질 수 있다. - 메소드오버로딩
	DefaultTableModel dtm = new DefaultTableModel(datas, header); // <table> - 양식 > 자바가 있어야 DataSet 구성함
	JTable jt_customer = new JTable(dtm);
	JScrollPane jsp_customer = new JScrollPane(jt_customer); // JScrollPane 위에 JTable위에 DefaultTableModel 넣음
	JPanel jp_north = new JPanel();
	JPanel jp_south = new JPanel();
	JButton jbtnAllSelect = new JButton("조회");
	JButton jbtnDelete = new JButton("삭제");
	JButton jbtnInsert = new JButton("추가");
	JButton jbtnUpdate = new JButton("수정");
	JButton jbtnSelect = new JButton("한 건 조회");
	JTextField se_c_id = new JTextField(6);
	JTextField jt_c_id = new JTextField(6);
	JTextField jt_c_name = new JTextField(6);
	JTextField jt_c_birthday = new JTextField(6);
	
	CustomerManager(){
		cList = new ArrayList<>();
		initDisplay();
	}
	CustomerManager(int i){
		
	}
	public void initDisplay() {
		jbtnAllSelect.addActionListener(this);
		jbtnDelete.addActionListener(this); // 삭제
		jbtnInsert.addActionListener(this); // 행 추가
		jbtnSelect.addActionListener(this); // 행 삭제
		jp_north.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		jp_north.add(jbtnAllSelect);
		jp_north.add(jbtnDelete);
		jp_north.add(jbtnUpdate);
		jp_north.add(se_c_id);
		jp_north.add(jbtnSelect);
		jp_south.add(jt_c_id);
		jp_south.add(jt_c_name);
		jp_south.add(jt_c_birthday);
		jp_south.add(jbtnInsert); // 행 추가 JPanel 붙이기
		se_c_id.setText("");
		jt_c_id.setText("");              // 이름 텍스트입력창 초기값
		jt_c_name.setText("");           // 주소 텍스트입력창 초기값
		jt_c_birthday.setText("");	
		this.add("North", jp_north);
		this.add("Center", jsp_customer);
		this.add("South", jp_south);
		this.setSize(500,400); // this -> 나 자신 -> DeptManager 
		this.setVisible(true);
	}
	/***********************************************************
	 * 고객정보 등록하기 구현 - 없는 것을 새로 등록하기
	 * INSERT INTO customer(c_id, c_name, c_birthday) VALUES(1,'키위', 2000-10-21')
	 * @param cVO - 한 번에 여러가지의 값을 넘겨 받을 수 있는 VO를 사용한다.
	 * @return
	 ***********************************************************/
	public boolean customerInsert(CustomerVO cVO) {  
		String input[] = new String[header.length];    //사용자값 저장할 배열 선언, col배열길이만큼
		input[0] = jt_c_id.getText();          
		input[1] 	= jt_c_name.getText();       	
		input[2]	= jt_c_birthday.getText();
		jt_c_id.setText("");              // 이름 텍스트입력창 초기값
		jt_c_name.setText("");           // 주소 텍스트입력창 초기값
		jt_c_birthday.setText("");
		dtm.addRow(input);
		cVO = new CustomerVO(input[0],input[1], input[2]);
		
		cDao = new CustomerDao();
		cDao.insertCVOList(cVO);
		
		boolean isOk = false;
		return isOk;
	}
	/************************************************************
	 * 고객정보 수정하기 구현 - 기존에 있는 정보를 변경하기
	 * select(1건) - rcVO(1건) or Map(1건) - pcVO - 1이면 수정 성공, 0이면 수정 실패 판정
	 * UPDATE customer SET c_name = ?, c_birthday = ? WHERE c_id = ?
	 * @param cVO
	 * @return
	 *************************************************************/
	public boolean customerUpdate(CustomerVO cVO) {
		boolean isOk = false;
		return isOk;
	}
	/**************************************************************
	 * 고객정보 삭제하기 구현
	 * DELETE FROM cutomer WHERE c_id = ? (primary key)
	 * @param int
	 * @return true이면 성공, false이면 실패 - 메소드 선언시에 파라미터타입에 대한 선택과 리턴타입에 대한 선택 기준
	 ***************************************************************/
	public boolean customerDelete(CustomerVO cVO) {
//		System.out.println("delet??");
		cDao = new CustomerDao();
		cDao.deleteCVOList(cVO);
		
		boolean isOk = false;
		return isOk;
	}
	/****************************************************************
	 * 고객정보 상세보기 구현
	 * SELECT c_id, c_name, c_birthday FROM customer WHERE c_id = ?
	 * @param c_id
	 * @return CustomerVO(한 테이블 일 때,) or Map(두 테이블 이상이여서 조인이 불가피 할 때)
	 ****************************************************************/
	public CustomerVO customerSelect(int c_id) {
		while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
		cDao = new CustomerDao();
		cVO = cDao.selectCVO(c_id);
		String input[] = new String[header.length];    //사용자값 저장할 배열 선언, col배열길이만큼
		input[0] =  se_c_id.getText();;          
		input[1] 	= cVO.getC_name();       	
		input[2]	= cVO.getC_birthday();
		dtm.addRow(input);
		cVO = new CustomerVO(input[0],input[1], input[2]);
		se_c_id.setText("");
		
		return null;
	}
	/*****************************************************************
	 * 고객정보 전체보기 구현 - 파라미터가 필요없다. 왜냐하면 - 조건절이 필요없으니까요.
	 * SELECT c_id, c_name, c_birthday FROM customer
	 * @return List<VO> or List<Map>
	 ******************************************************************/
	public List<CustomerVO> customerSelectAll() {
		cDao = new CustomerDao();
		List<CustomerVO> cList = cDao.getCVOList();
		while (dtm.getRowCount() > 0) { // dtm은 데이터셋(자바측)받는 클래스이다.
			dtm.removeRow(0); // 0번째 로우를 지우는 이유는 로우가 삭제 될때 마다 dtm의 로우수가 줄어든다. - 왜?
		}
		for (int i = 0; i < cList.size(); i++) {
			cVO = cList.get(i);
			Vector<Object> v = new Vector<>();
			v.add(0, cVO.getC_id());
			v.add(1, cVO.getC_name());
			v.add(2, cVO.getC_birthday());
			// addRow메소드의 오버로딩은 2가지 이다. - 1.) Vector 2.) Object[]
//			파라미터자리에 Vector를 사용해야하는 addRow를 불러왔기 때문에 인스턴스화 후 v 넣기.
			dtm.addRow(v); // 4번 반복되니까 - 로우에 추가하는 코드를 4번 실행함. - list.size() = 4
		}
		return cList;
	}
	public static void main(String[] args) {
		CustomerManager cm = null;
		cm = new CustomerManager();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource(); 
		 if (obj == jbtnAllSelect) {
				customerSelectAll();
			} else if (obj == jbtnInsert) {
				customerInsert(cVO);
			} else if (obj == jbtnDelete) {
				if(jt_customer.getSelectedRow()==-1) { //-1일때(아무것도 선택되지 않았을때					
															//if문 탈출할때 return;을 사용함	
					return;                            //따라서 삭제 버튼 눌렀을때 아무일도 일어나지 않는다.	                                    
				} else { //행이 선택되어있을때
					cVO = new CustomerVO(jt_customer.getSelectedRow());
					customerDelete(cVO);
					dtm.removeRow(jt_customer.getSelectedRow());   //선택된 행값을 가져와서 삭제메소드실행
				}
			} else if (obj == jbtnSelect) {
				cVO = new CustomerVO(se_c_id.getText());
				customerSelect(cVO.getC_id()); 
				
			}
		}
	}