package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**描述赋值语句的文法
 * 包含需要用到的产生式、终结符号集、非终结符号集等信息
 * 文法：
 * 			A→V=E
			E→E+T∣E-T∣T
          	T→T*F∣T/F∣F
          	F→(E)∣i
          	V→i
 * */

public class AssignmentGrammar {
	//手动置入的文法
	private String []grammarnArry = {
			"A->V=E",
			"E->E+T|E-T|T",
			"T->T*F|T/F|F",
			"F->(E)|i",
			"V->i"
	};
	private Character startSymbol = 'G'; //文法的 开始符号
	//去掉|后的基本文法
	private List<String> basicGrammar = new ArrayList<String>();
	
	//基于基本文法形成的第一批拓展文法，其中s'用符号G代替
	private static List<ProductionFormula> expendGrammar = new ArrayList<ProductionFormula>() ;
	
	//终结符号集
	private Set<Character> Vt = new HashSet<Character>();
	
	//非终结符号集
	private Set<Character> Vn = new HashSet<Character>();
	
	//所有符号集
	private Set<Character> V = new HashSet<Character>();
	
	
	public AssignmentGrammar() {
		//构造基本文法,终结符号集,非终结符号集
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
		
		//构造拓展文法
		for(int i = 0;i<basicGrammar.size();++i) {
			StringBuffer sb = new StringBuffer(basicGrammar.get(i));
			sb.insert(3, '.');
			expendGrammar.add(new ProductionFormula(sb.toString()));
		}
		Vn.add('G');
		V.addAll(Vn);
		V.addAll(Vt);
		
		System.out.println("基本文法构造如下：");
		for(int i = 0;i<basicGrammar.size();++i) {
			System.out.println(basicGrammar.get(i));
		}
		System.out.println("======================\n");
		
		//显示构造的文法
		System.out.println("拓展文法构造如下：");
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
