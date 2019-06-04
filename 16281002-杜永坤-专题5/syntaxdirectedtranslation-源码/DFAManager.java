package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**������DFA������
 * */
public class DFAManager {
	private AssignmentGrammar grammar = new AssignmentGrammar(); //�����״̬�����ķ�
	private int listP = 0;     //ָ��״̬���б��ָ��
	private List<StateDFA> StateList = new ArrayList<StateDFA>(); //ȫ�ֵ�״̬��
	public void buildDFA() {	//״̬������
		//����״̬���ĳ�ʼ���
		////////////////////////////////////////////////////////
		StateDFA firstState = new StateDFA();
		Character startSymbol = grammar.getStartSymbol();
		//�����ķ������йؿ�ʼ���ŵĲ���ʽ�����ʼ���Ĳ���ʽ������
		for(int i = 0;i<AssignmentGrammar.getExpendGrammar().size();++i) {
			ProductionFormula pf = AssignmentGrammar.getExpendGrammar().get(i);
			if(pf.getLeft().equals(startSymbol)) {
				firstState.getProductionSet().add(pf);
			}
		}
		///////////////////////////////////////////////////////
		
		//////////////////////////////////////////////////////////////////////////
		// ����������ɵĽ��֮��Ҫ���������еĲ���ʽ����.֮��ķ��ս���ŵĲ���ʽҲ�ӽ��������������Ǽ��ϳ��Ȳ�������
		// ����հ�����
		int oldSize = 0;
		int newSize = firstState.getProductionSet().size();
		Iterator<ProductionFormula> it = firstState.getProductionSet().iterator();
		while (newSize > oldSize) {
			it = firstState.getProductionSet().iterator();
			while (it.hasNext()) {
				Character charAfterPoint = it.next().charAfterPoint();
				if (charAfterPoint == null) {
					continue; // Ϊ�վͲ�����һ������ʽ
				}
				// ɨ�����ʽ����չ���ҵ���չ������charAfterPoint�Ĳ���ʽ���뵽���Ĳ���ʽ������
				for (int i = 0; i < AssignmentGrammar.getExpendGrammar().size(); ++i) {
					if (AssignmentGrammar.getExpendGrammar().get(i).getLeft().equals(charAfterPoint)) {
						firstState.getProductionSet().add(AssignmentGrammar.getExpendGrammar().get(i));
					}
				}
				oldSize = newSize;
				newSize = firstState.getProductionSet().size();
			}
		}
		/////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////����ȫ����Ϣ////////////////////////////
		firstState.setCategoryID(StateDFA.StateID);
		++StateDFA.StateID;
		StateDFA.AllStateList.add(firstState);
		////////////////////////////////////////////////////////////////////
		
		////////////////�ӵ�һ��״̬��ʼ��������״̬///////////////////////
		while(listP<StateDFA.AllStateList.size()) {
			//��AllStateList��ȡ��δ�����״̬
			StateDFA sd = StateDFA.AllStateList.get(listP);
			//���������ķ�����
			Iterator<Character> itV = grammar.getV().iterator();
			while(itV.hasNext()) {
				Character sy = itV.next();
				sd.createNewStateDFA(sy); //�����µ�״̬
			}
			//��������֮��sd����StateList;
			StateList.add(sd);
			++listP;
		}
		//////////////////////////////////////////////////////////
		
	}
	
	public void displayDFA() {	//���������״̬��
		//����Statelist�������������map
		Iterator<StateDFA> itStateList = StateList.iterator();
		while(itStateList.hasNext()) {
			StateDFA sdfa = itStateList.next();
			System.out.print(sdfa);
			//���״̬ת����Ϣ
			if(sdfa.getTransform().isEmpty()) {
				System.out.println("�����Ϊ�ն˽��");
			}else {
				Iterator<Character> itmap = sdfa.getTransform().keySet().iterator();
				while(itmap.hasNext()) {
					Character c = itmap.next();
					System.out.printf("״̬%d--"+c+"-->״̬%d\n",sdfa.getCategoryID(),sdfa.getTransform().get(c).getCategoryID());
				}
			}
			System.out.println();
		}
	}
	
	public List<StateDFA> getStateList(){
		return this.StateList;
	}
	
	public AssignmentGrammar getGrammar() {
		return this.grammar;
	}
}
