package syntaxdirectedtranslation;
/**产生式类，附带对产生式的一些操作
 * */
public class ProductionFormula {
	private String formula = null;
	private Character left;
	private String right;
	public ProductionFormula(String f) {
		this.formula = new String(f);
		this.left = f.charAt(0);
		this.right = f.substring(3);
	}
	public String getformula() {
		return this.formula;
	}
	
	public Character getLeft() {
		return this.left;
	}
	public String getRight() {
		return this.right;
	}
	//返回产生式中.之后的第一个字符
	public Character charAfterPoint() {
		int index = formula.indexOf('.');
		if(index == formula.length()-1) {
			//点在末尾
			return null;
		}
		return formula.charAt(index+1);
	}
	
	//产生式中的.右移一位产生新的产生式
	public ProductionFormula movePoint(Character ch) {
		int pIndex = this.formula.indexOf('.');
		if(pIndex==formula.length()-1) { //点在末尾
			return null;
		}
		char c = formula.charAt(pIndex+1);
		//不匹配不后移
		if(ch.charValue()!=c) {
			return null;
		}
		StringBuffer sb = new StringBuffer(this.formula);
		//否则，将.与后面的字符对调
		sb.setCharAt(pIndex, c);
		sb.setCharAt(pIndex+1, '.');
		return new ProductionFormula(sb.toString());
	}
	//重写toString
	public String toString() {
		return this.formula;
	}
	
	//重写产生式的hashCode方法
	@Override
	public int hashCode() {
		return this.formula.hashCode();
	}
	//重写产生式的equals方法
	@Override
	public boolean equals(Object obj) {
		if(this==obj)
			return true;
		if(obj==null)
			return false;
		if(!(obj instanceof ProductionFormula))
			return false;
		ProductionFormula pf = (ProductionFormula)obj;
		if(this.formula.equals(pf.getformula()))
			return true;
		else 
			return false;
	}
}
