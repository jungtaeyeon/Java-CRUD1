package CRUD3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CustomerDialog extends JFrame implements ActionListener {
	// 선언부
	CustomerVO cVO = null;
	CustomerDao cDao = null;
//	JDialog jdg_customer = new JDialog();  // JFrame 을 상속받아서 인스턴스화 안해도 된다.
	String header[] = { "고객 No.", "고객 이름", "고객 생일" };
	String datas[][] = new String[0][0];
	DefaultTableModel dtm = new DefaultTableModel(datas, header);
	JTable jt_customer = new JTable(dtm);
	JScrollPane jsp_customer = new JScrollPane(jt_customer); // JScrollPane 위에 JTable위에 DefaultTableModel 넣음
	JButton jbtnUpdateOk = new JButton("확인");
	JButton jbtnUpdateClose = new JButton("닫기");
	JPanel jp_north = new JPanel();
	JPanel jp_south = new JPanel();
	
	// 생성자
	CustomerDialog(){}
	CustomerManager customerManager = null;
	public CustomerDialog(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}

	// 화면 처리부
	public void initDisplay(int selectedRow) {
		cVO = new CustomerVO();
		cDao = new CustomerDao();
		jbtnUpdateOk.addActionListener(this);
		jbtnUpdateClose.addActionListener(this);
		jp_south.add(jbtnUpdateOk);
		jp_south.add(jbtnUpdateClose);
		this.add("North", jp_north);  // JFrame 을 상속받아서 jdg_customer 대신 this로 바꿨다. 아래도 전부 동일
		this.add("Center", jsp_customer);
		this.add("South", jp_south); 
		this.setTitle("수정"); // 메소드 설계, 파라미터 결정함.
		this.setSize(400,200);
		this.setVisible(true);
		// 방법 1   ->  요거 말고 한 건 조회의 DB 데이터를 받아오는 방법도 생각해보기!!
//		Vector<Object> v = new Vector<>(); 
//		v.add(0, customerManager.dtm.getValueAt(selectedRow,0));
//		v.add(1, customerManager.dtm.getValueAt(selectedRow,1));
//		v.add(2, customerManager.dtm.getValueAt(selectedRow,2));
//		dtm.addRow(v);
		
		// 방법 2 -> VO에서 받아와 Select한 데이터가 조회되는걸로 바꿈  // 여기 한 건 조회 후 ClassCastException 터진다.. 해결하기..
		// 아래와 같이 .toString() 을 붙여 String 타입으로 바꾼 후 그 아래도 c_id를 Integer.parseInt() 사용으로 int타입으로 바꿨다..
//		int c_id = (int) customerManager.dtm.getValueAt(selectedRow,0);  
		String c_id = customerManager.dtm.getValueAt(selectedRow,0).toString();  
		cVO.setC_id(Integer.parseInt(c_id));
		cVO = cDao.selectCVO(Integer.parseInt(c_id));
		String input[] = new String[header.length]; // 사용자값 저장할 배열 선언, col배열길이만큼
		input[0] = Integer.toString(cVO.getC_id()); // cVO.getC_id()가 int타입이라 Integer.toString()을 사용하여 바꿔줌
		input[1] = cVO.getC_name();
		input[2] = cVO.getC_birthday();
		dtm.addRow(input);
//		cVO.setC_id(Integer.parseInt(input[0]));  // input[0] 이 String타입이라 Integer.parseInt() 를 사용하여 바꿔줌
//		cVO.setC_name(input[1]);
//		cVO.setC_birthday(input[2]);
	}
	
	// 메인 메소드
	public static void main(String[] args) {
//		CustomerDialog  cd = new CustomerDialog();
//		cd.initDisplay(0); 
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == jbtnUpdateClose) {
			this.dispose();
		} else if(obj == jbtnUpdateOk) {
			String c_id = dtm.getValueAt(0, 0).toString();
			String c_name = dtm.getValueAt(0, 1).toString();
			String c_biString = dtm.getValueAt(0, 2).toString();
			cDao.updateCVO(Integer.parseInt(c_id), c_name, c_biString);
			this.dispose();
		}
	}


}

