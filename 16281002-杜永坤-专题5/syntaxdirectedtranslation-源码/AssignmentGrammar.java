package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**������ֵ�����ķ�
 * ������Ҫ�õ��Ĳ���ʽ���ս���ż������ս���ż�����Ϣ
 * �ķ���
 * 			A��V=E
			E��E+T�OE-T�OT
          	T��T*F�OT/F�OF
          	F��(E)�Oi
          	V��i
 * */

public class AssignmentGrammar {
	//�ֶ�������ķ�
	private String []grammarnArry = {
			"A->V=E",
			"E->E+T|E-T|T",
			"T->T*F|T/F|F",
			"F->(E)|i",
			"V->i"
	};
	private Character startSymbol = 'G'; //�ķ��� ��ʼ����
	//ȥ��|��Ļ����ķ�
	private List<String> basicGrammar = new ArrayList<String>();
	
	//���ڻ����ķ��γɵĵ�һ����չ�ķ�������s'�÷���G����
	private static List<ProductionFormula> expendGrammar = new ArrayList<ProductionFormula>() ;
	
	//�ս���ż�
	private Set<Character> Vt = new HashSet<Character>();
	
	//���ս���ż�
	private Set<Character> Vn = new HashSet<Character>();
	
	//���з��ż�
	private Set<Character> V = new HashSet<Character>();
	
	
	public AssignmentGrammar() {
		//��������ķ�,�ս���ż�,���ս���ż�
		basicGrammar.add(startSymbol+"->"+grammarnArry[0].charAt(0));
		for(int i = 0;i<grammarnArry.length;++i) {
			Character left = grammarnArry[i].charAt(0);
			Vn.add(left);
			String right = grammarnArry[i].substring(3);
			String []rightnArry = right.split("\\|");
			for(int j = 0;j < rightnArry.length; ++j) {
				basicGrammar.add(left+"->"+rightnArry[j]);
				for(int k = 0;k<rightnArry[j].length();++k) {
					char sy = rightnArry[j].charAt(k);
					if(sy>='A'&&sy<='Z')
						Vn.add(sy);
					else
						Vt.add(sy);
				}
			}
		}
		
		//������չ�ķ�
		for(int i = 0;i<basicGrammar.size();++i) {
			StringBuffer sb = new StringBuffer(basicGrammar.get(i));
			sb.insert(3, '.');
			expendGrammar.add(new ProductionFormula(sb.toString()));
		}
		Vn.add('G');
		V.addAll(Vn);
		V.addAll(Vt);
		
		System.out.println("�����ķ��������£�");
		for(int i = 0;i<basicGrammar.size();++i) {
			System.out.println(basicGrammar.get(i));
		}
		System.out.println("======================\n");
		
		//��ʾ������ķ�
		System.out.println("��չ�ķ��������£�");
		for(int i = 0;i<expendGrammar.size();++i) {
			System.out.println(expendGrammar.get(i).getformula());
		}
		System.out.println("======================\n");
	
	}
	
	public static List<ProductionFormula> getExpendGrammar(){
		return expendGrammar;
	}
	
	public Character getStartSymbol() {
		return startSymbol;
	}
	
	public Set<Character> getVt(){
		return this.Vt;
	}
	
	public Set<Character> getVn(){
		return this.Vn;
	}
	
	public Set<Character> getV(){
		return this.V;
	}
	
	public String [] getGrammar() {
		return this.grammarnArry;
	}
	
	public List<String> getBasicGrammar(){
		return this.basicGrammar;
	}
}
