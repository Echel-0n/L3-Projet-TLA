package application;

public class TypeCondition {	
	public TypeDeCondition type;
	private int num;
	
	public TypeCondition(TypeDeCondition type, int num) {
		this.type = type;
		this.num = num;
	}
	
	public TypeCondition(TypeDeCondition type) {
		this.type = type;
		this.num = -1;
	}
	
	public void setNum(int n) {
		num = n;
	}
	
	public int getNum() {
		return num;
	}
}
