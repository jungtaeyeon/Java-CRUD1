package CRUD5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		System.out.println("initDisplay 호출");
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
		customerManager.customerUpdate(selectedRow);
		
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
			cDao.updateList(Integer.parseInt(c_id), c_name, c_biString);
			this.dispose();
			customerManager.customerSelectAll(); // "확인" 버튼 클릭 시, 창이 닫히면서 다시 DB 테이블의 데이터를 전체조회 해주기 !!
		}
	}


}

