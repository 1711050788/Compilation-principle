 package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

//赋值语句的翻译文法
//需要文法符号表
//各个产生式的语法翻译动作
//其他一些方法实现
public class AssignmentTranslationGrammar {
	//符号表，归约时要根据符号表中登记的标识符查询其值生成四元式
	//private Map<String,String> symbolValue = new HashMap<String,String>();
	//带有栈的符号表，因为对于复杂的算术表达式在归约时会由于归约为相同的左部而对其他值产生影响，因此将Map的对应项制成栈的结构
	private Map<String,Stack<String>> symbolValue = new HashMap<String,Stack<String>>();
	//语法翻译的对应基本文法
	private List<String> grammar = new AssignmentGrammar().getBasicGrammar();
	//语法制导翻译得到的四元式序列
	private List<String> fourYuanShi = new ArrayList<String>();
	//临时变量序号
	private int indexTemp = 0;
	//初始化symbolValue表
	public AssignmentTranslationGrammar() {
		for(int i = 0;i<grammar.size();++i) {
			symbolValue.put(grammar.get(i).charAt(0)+"", new Stack<String>());
		}
		symbolValue.put("i", new Stack<String>());
	}
	//根据归约的产生式查询产生式集合，采取相应的翻译动作
	public void translateAction(String pro) {
		int translation = grammar.indexOf(pro);
		switch(translation) {
		case 0:
			GtoA();
			break;
		case 1:
			AtoVequalsE();
			break;
		case 2:
			EtoEplusT();
			break;
		case 3:
			EtoEminusT();
			break;
		case 4:
			EtoT();
			break;
		case 5:
			TtoTmultiplyF();
			break;
		case 6:
			TtoTdivideF();
			break;
		case 7:
			TtoF();
			break;
		case 8:
			FtobraEcket();
			break;
		case 9:
			FtoI();
			break;
		case 10:
			VtoI();
			break;
		default:
			break;
		}
	}
	//G->A
	private void GtoA() {
		
	}
	//A->V=E
	private void AtoVequalsE() {
		//归约完之后，V和E的栈顶元素就没用了，用pop
		String temp = "("+"=,"+symbolValue.get("E").pop()+",0,"+symbolValue.get("V").pop()+")";
		System.out.println("产生四元式："+temp+"");
		fourYuanShi.add(temp);
		
	}
	//E->E+T
	private void EtoEplusT() {
		String tempE = symbolValue.get("E").pop();
		
		String top = NewTemp();
		symbolValue.get("E").push(top); 

		String temp = "(+,"+tempE+","+symbolValue.get("T").pop()+","+symbolValue.get("E").peek()+")";
		System.out.println("产生四元式："+temp+"");
		fourYuanShi.add(temp);
	}
	
	//E->E-T
	private void EtoEminusT() {
		String tempE = symbolValue.get("E").pop();
		
		String top = NewTemp();
		symbolValue.get("E").push(top);
		
		String temp = "(-,"+tempE+","+symbolValue.get("T").pop()+","+symbolValue.get("E").peek()+")";
		System.out.println("产生四元式："+temp);
		fourYuanShi.add(temp);
	}
	
	//E->T
	private void EtoT() {
		symbolValue.get("E").push(symbolValue.get("T").pop());
	}
	
	//T->T*F
	private void TtoTmultiplyF() {
		String tempT = symbolValue.get("T").pop();
		String top = NewTemp();
		symbolValue.get("T").push(top);
		
		String temp = "(*,"+tempT+","+symbolValue.get("F").pop()+","+symbolValue.get("T").peek()+")";
		System.out.println("产生四元式："+temp);
		fourYuanShi.add(temp);
	}
	
	//T->T/F
	private void TtoTdivideF() {
		
		String tempT = symbolValue.get("T").pop();
		String top = NewTemp();
		symbolValue.get("T").push(top);
		
		String temp = "(/,"+tempT+","+symbolValue.get("F").pop()+","+symbolValue.get("T").peek()+")";
		System.out.println("产生四元式："+temp);
		fourYuanShi.add(temp);
	}
	
	//T->F
	private void TtoF() {
		symbolValue.get("T").push(symbolValue.get("F").pop());
	}
	
	//F->(E)
	private void FtobraEcket() {
		symbolValue.get("F").push(symbolValue.get("E").pop());
	}
	
	//F->i
	private void FtoI() {
		symbolValue.get("F").push(symbolValue.get("i").pop());
	}
	
	//V->i
	private void VtoI() {
		//symbolValue.get("V").pop();
		symbolValue.get("V").push(symbolValue.get("i").pop());
	}
	
	private String NewTemp() {
		++indexTemp;
		return "T"+indexTemp;
	}
	
	public Map<String,Stack<String>> getSymbolValue(){
		return this.symbolValue;
	}
	
	public void displayFourYuanShi() {
		for(int i = 0;i<fourYuanShi.size();++i) {
			System.out.println(fourYuanShi.get(i));
		}
	}
}
