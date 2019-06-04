package syntaxdirectedtranslation;
/**����ʽ�࣬�����Բ���ʽ��һЩ����
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
	//���ز���ʽ��.֮��ĵ�һ���ַ�
	public Character charAfterPoint() {
		int index = formula.indexOf('.');
		if(index == formula.length()-1) {
			//����ĩβ
			return null;
		}
		return formula.charAt(index+1);
	}
	
	//����ʽ�е�.����һλ�����µĲ���ʽ
	public ProductionFormula movePoint(Character ch) {
		int pIndex = this.formula.indexOf('.');
		if(pIndex==formula.length()-1) { //����ĩβ
			return null;
		}
		char c = formula.charAt(pIndex+1);
		//��ƥ�䲻����
		if(ch.charValue()!=c) {
			return null;
		}
		StringBuffer sb = new StringBuffer(this.formula);
		//���򣬽�.�������ַ��Ե�
		sb.setCharAt(pIndex, c);
		sb.setCharAt(pIndex+1, '.');
		return new ProductionFormula(sb.toString());
	}
	//��дtoString
	public String toString() {
		return this.formula;
	}
	
	//��д����ʽ��hashCode����
	@Override
	public int hashCode() {
		return this.formula.hashCode();
	}
	//��д����ʽ��equals����
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
