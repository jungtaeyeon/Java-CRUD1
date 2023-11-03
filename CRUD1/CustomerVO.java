package CRUD1;


public class CustomerVO {
	private int c_id;
	private String c_name;
	private String c_birthday;
	
	public CustomerVO() {}
	public CustomerVO(int c_id, String c_name, String c_birthday) {
		this.c_id = c_id;
		this.c_name = c_name;
		this.c_birthday = c_birthday;
	}
	public CustomerVO(String c_id, String c_name, String c_birthday) {
		this.c_id = Integer.parseInt(c_id);
		this.c_name = c_name;
		this.c_birthday = c_birthday;
	}
	public CustomerVO(int c_id) {
		this.c_id = c_id;
	}
	public CustomerVO(String c_id) { // jbtnSelect
		this.c_id = Integer.parseInt(c_id);
	}
	public int getC_id() {
		return c_id;
	}
	public void setC_id(int c_id) {
		this.c_id = c_id;
	}
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
	public String getC_birthday() {
		return c_birthday;
	}
	public void setC_birthday(String c_birthday) {
		this.c_birthday = c_birthday;
	}
}