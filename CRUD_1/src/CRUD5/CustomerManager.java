package CRUD5;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
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

public class CustomerManager extends JFrame implements ActionListener {
	CustomerDialog customerDialog = new CustomerDialog(this);
	List<CustomerVO> cList = null;
	CustomerDao cDao = null;
	CustomerVO cVO = null;

	List<Map<String, Object>> deptList = new ArrayList<>(); // 왜 전역변수로 하는지?? 입력|수정|삭제|조회 유지되어야 하기 때문에
	String header[] = { "고객 No.", "고객 이름", "고객 생일" };
	String datas[][] = new String[0][0];
	DefaultTableModel dtm = new DefaultTableModel(datas, header);
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
	int sRow = 0;

	CustomerManager() {
		cList = new ArrayList<>();
		initDisplay();
	}

	CustomerManager(int i) {

	}

	public void initDisplay() {
		jbtnAllSelect.addActionListener(this);
		jbtnDelete.addActionListener(this);
		jbtnUpdate.addActionListener(this);
		jbtnInsert.addActionListener(this);
		jbtnSelect.addActionListener(this);
		jp_north.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp_north.add(jbtnAllSelect);
		jp_north.add(jbtnDelete);
		jp_north.add(jbtnUpdate);
		jp_north.add(se_c_id);
		jp_north.add(jbtnSelect);
		jp_south.add(jt_c_id);
		jp_south.add(jt_c_name);
		jp_south.add(jt_c_birthday);
		jp_south.add(jbtnInsert);
		se_c_id.setText("");
		jt_c_id.setText("");
		jt_c_name.setText("");
		jt_c_birthday.setText("");
		this.add("North", jp_north);
		this.add("Center", jsp_customer);
		this.add("South", jp_south);
		this.setSize(500, 400);
		this.setVisible(true);
	}

	/***********************************************************
	 * 고객정보 등록하기 구현 - 없는 것을 새로 등록하기 INSERT INTO customer(c_id, c_name, c_birthday)
	 * VALUES(1,'키위', 2000-10-21')
	 * 
	 * @param cVO - 한 번에 여러가지의 값을 넘겨 받을 수 있는 VO를 사용한다.
	 * @return
	 ***********************************************************/
	public boolean customerInsert() {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		String input[] = new String[header.length]; // 사용자값 저장할 배열 선언, col배열길이만큼
		input[0] = jt_c_id.getText();
		input[1] = jt_c_name.getText();
		input[2] = jt_c_birthday.getText();
		jt_c_id.setText("");
		jt_c_name.setText("");
		jt_c_birthday.setText("");
		dtm.addRow(input);
		map.put("c_id", input[0]);
		map.put("c_name", input[1]);
		map.put("c_birthday", input[2]);
		list.add(map);
		cDao = new CustomerDao();
		cDao.insertList(list);

		boolean isOk = false;
		return isOk;
	}

	/************************************************************
	 * 고객정보 수정하기 구현 - 기존에 있는 정보를 변경하기 select(1건) - rcVO(1건) or Map(1건) - pcVO - 1이면
	 * 수정 성공, 0이면 수정 실패 판정 UPDATE customer SET c_name = ?, c_birthday = ? WHERE c_id = ?
	 * 
	 * @param cVO
	 * @return
	 *************************************************************/
	public boolean customerUpdate(int selectedRow) {
		String c_id = dtm.getValueAt(selectedRow,0).toString();  
		List<Map<String, Object>> list = null;
		Map<String, Object> map = null;
		
		list = cDao.selectList(Integer.parseInt(c_id));
		map = list.get(0);
		Vector<Object> v = new Vector<>();
		v.add(0, map.get("c_id"));
		v.add(1, map.get("c_name"));
		v.add(2, map.get("c_birthday"));
//		System.out.println(v); // 수정버튼 클릭 시, vector에 값이 담기는지 단위테스트로 확인...
//		dtm.addRow(v); // customerDialog의 dtm에 값을 담아야 하는데 현재 클래스의 dtm에 담고있었다.
		customerDialog.dtm.addRow(v);
		
		boolean isOk = false;
		return isOk;
	}

	/**************************************************************
	 * 고객정보 삭제하기 구현 DELETE FROM cutomer WHERE c_id = ? (primary key)
	 * 
	 * @param int
	 * @return true이면 성공, false이면 실패 - 메소드 선언시에 파라미터타입에 대한 선택과 리턴타입에 대한 선택 기준
	 ***************************************************************/
	public boolean customerDelete(int selectRowNum) {
		cDao = new CustomerDao();
//		cDao.deleteCVOList(selectRowNum);
		// CustomerDao클래스 -> deleteCVOList() 메소드 파라미터에 int selectRowNum를 넣고 위 코드를 살리면 간단하지만
		// 굳이 파라미터에 Map을 사용해보았다.
		cDao.deleteList(cDao.selectList(selectRowNum).get(0));

		boolean isOk = false;
		return isOk;
	}

	/****************************************************************
	 * 고객정보 상세보기 구현 SELECT c_id, c_name, c_birthday FROM customer WHERE c_id = ?
	 * 
	 * @param c_id
	 * @return CustomerVO(한 테이블 일 때,) or Map(두 테이블 이상이여서 조인이 불가피 할 때)
	 ****************************************************************/
	public List<Map<String, Object>> customerSelect(int c_id) {
		cDao = new CustomerDao();
		List<Map<String, Object>> cList = cDao.selectList(c_id);
		Map<String, Object> cMap = null;
		while (dtm.getRowCount() > 0) {
			dtm.removeRow(0);
		}
		// 단일조회 이기 때문에 cList의 0번 Index에 값이 담기는데,  0번째 인덱스 값이 아닌 
		// 텍스트필드에 입력한 숫자 c_id의 인덱스 값을 가져오려해서 계속 
		// IndexOutOfBoundsException가 터졌었다..
//		cMap = cList.get(c_id); 
		cMap = cList.get(0);
		Vector<Object> v = new Vector<>();
		v.add(0, cMap.get("c_id"));
		v.add(1, cMap.get("c_name"));
		v.add(2, cMap.get("c_birthday"));
		dtm.addRow(v);
		se_c_id.setText("");
		
		return cList;
	}

	/*****************************************************************
	 * 고객정보 전체보기 구현 - 파라미터가 필요없다. 왜냐하면 - 조건절이 필요없으니까요. SELECT c_id, c_name, c_birthday FROM customer
	 * 
	 * @return List<VO> or List<Map>
	 ******************************************************************/
	public List<Map<String, Object>> customerSelectAll() {
		cDao = new CustomerDao();
		List<Map<String, Object>> cList = cDao.selectAllList();
		Map<String, Object> cMap = null;
		while (dtm.getRowCount() > 0) {
			dtm.removeRow(0); // 0번째 로우를 지우는 이유는 로우가 삭제 될때 마다 dtm의 로우수가 줄어든다.
		}
		for (int i = 0; i < cList.size(); i++) {
			cMap = cList.get(i);
			Vector<Object> v = new Vector<>();
			v.add(0, cMap.get("c_id"));
			v.add(1, cMap.get("c_name"));
			v.add(2, cMap.get("c_birthday"));
			dtm.addRow(v); 
		}
		return null;
	}

	public static void main(String[] args) {
		CustomerManager cm = null;
		cm = new CustomerManager();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int sRow = jt_customer.getSelectedRow();
		Object obj = e.getSource();

		if (obj == jbtnAllSelect) { // 전체조회
			customerSelectAll();
			
		} else if (obj == jbtnInsert) { // 추가(입력)
			customerInsert();
			
		} else if (obj == jbtnDelete) {
			if (sRow == -1) { // -1일때(아무것도 선택되지 않았을때
														// if문 탈출할때 return;을 사용함
				return; // 따라서 삭제 버튼 눌렀을때 아무일도 일어나지 않는다.
			} else { // 행이 선택되어있을때
				int selectRowNum = (int)dtm.getValueAt(sRow,0);  // getValueAt가 Object 타입이라 int타입으로 바꿔줌
				customerDelete(selectRowNum);
				dtm.removeRow(sRow); // 선택된 행값을 가져와서 삭제메소드실행
			}
			
		} else if (obj == jbtnSelect) { // 한 건 조회
			int search = Integer.parseInt(se_c_id.getText());
			customerSelect(search);
			
		} else if (obj == jbtnUpdate) { // 수정
			if (sRow == -1) { // -1일때(아무것도 선택되지 않았을때
				// if문 탈출할때 return;을 사용함
				return; // 따라서 삭제 버튼 눌렀을때 아무일도 일어나지 않는다.
			} else { // 행이 선택되어있을때
				while (customerDialog.dtm.getRowCount() > 0) {
					customerDialog.dtm.removeRow(0); // 0번째 로우를 지우는 이유는 로우가 삭제 될때 마다 dtm의 로우수가 줄어든다.
				}
				customerDialog.initDisplay(sRow);
				
			}
		}
	}
}