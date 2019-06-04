 package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

//��ֵ���ķ����ķ�
//��Ҫ�ķ����ű�
//��������ʽ���﷨���붯��
//����һЩ����ʵ��
public class AssignmentTranslationGrammar {
	//���ű���ԼʱҪ���ݷ��ű��еǼǵı�ʶ����ѯ��ֵ������Ԫʽ
	//private Map<String,String> symbolValue = new HashMap<String,String>();
	//����ջ�ķ��ű���Ϊ���ڸ��ӵ��������ʽ�ڹ�Լʱ�����ڹ�ԼΪ��ͬ���󲿶�������ֵ����Ӱ�죬��˽�Map�Ķ�Ӧ���Ƴ�ջ�Ľṹ
	private Map<String,Stack<String>> symbolValue = new HashMap<String,Stack<String>>();
	//�﷨����Ķ�Ӧ�����ķ�
	private List<String> grammar = new AssignmentGrammar().getBasicGrammar();
	//�﷨�Ƶ�����õ�����Ԫʽ����
	private List<String> fourYuanShi = new ArrayList<String>();
	//��ʱ�������
	private int indexTemp = 0;
	//��ʼ��symbolValue��
	public AssignmentTranslationGrammar() {
		for(int i = 0;i<grammar.size();++i) {
			symbolValue.put(grammar.get(i).charAt(0)+"", new Stack<String>());
		}
		symbolValue.put("i", new Stack<String>());
	}
	//���ݹ�Լ�Ĳ���ʽ��ѯ����ʽ���ϣ���ȡ��Ӧ�ķ��붯��
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
		//��Լ��֮��V��E��ջ��Ԫ�ؾ�û���ˣ���pop
		String temp = "("+"=,"+symbolValue.get("E").pop()+",0,"+symbolValue.get("V").pop()+")";
		System.out.println("������Ԫʽ��"+temp+"");
		fourYuanShi.add(temp);
		
	}
	//E->E+T
	private void EtoEplusT() {
		String tempE = symbolValue.get("E").pop();
		
		String top = NewTemp();
		symbolValue.get("E").push(top); 

		String temp = "(+,"+tempE+","+symbolValue.get("T").pop()+","+symbolValue.get("E").peek()+")";
		System.out.println("������Ԫʽ��"+temp+"");
		fourYuanShi.add(temp);
	}
	
	//E->E-T
	private void EtoEminusT() {
		String tempE = symbolValue.get("E").pop();
		
		String top = NewTemp();
		symbolValue.get("E").push(top);
		
		String temp = "(-,"+tempE+","+symbolValue.get("T").pop()+","+symbolValue.get("E").peek()+")";
		System.out.println("������Ԫʽ��"+temp);
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
		System.out.println("������Ԫʽ��"+temp);
		fourYuanShi.add(temp);
	}
	
	//T->T/F
	private void TtoTdivideF() {
		
		String tempT = symbolValue.get("T").pop();
		String top = NewTemp();
		symbolValue.get("T").push(top);
		
		String temp = "(/,"+tempT+","+symbolValue.get("F").pop()+","+symbolValue.get("T").peek()+")";
		System.out.println("������Ԫʽ��"+temp);
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
