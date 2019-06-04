package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**最外层的DFA管理类
 * */
public class DFAManager {
	private AssignmentGrammar grammar = new AssignmentGrammar(); //构造该状态机的文法
	private int listP = 0;     //指导状态机列表的指针
	private List<StateDFA> StateList = new ArrayList<StateDFA>(); //全局的状态链
	public void buildDFA() {	//状态机生成
		//构造状态机的初始结点
		////////////////////////////////////////////////////////
		StateDFA firstState = new StateDFA();
		Character startSymbol = grammar.getStartSymbol();
		//检索文法，将有关开始符号的产生式加入初始结点的产生式集合中
		for(int i = 0;i<AssignmentGrammar.getExpendGrammar().size();++i) {
			ProductionFormula pf = AssignmentGrammar.getExpendGrammar().get(i);
			if(pf.getLeft().equals(startSymbol)) {
				firstState.getProductionSet().add(pf);
			}
		}
		///////////////////////////////////////////////////////
		
		//////////////////////////////////////////////////////////////////////////
		// 添加完新生成的结点之后，要遍历集合中的产生式，将.之后的非终结符号的产生式也加进来，结束条件是集合长度不再增大
		// 即求闭包操作
		int oldSize = 0;
		int newSize = firstState.getProductionSet().size();
		Iterator<ProductionFormula> it = firstState.getProductionSet().iterator();
		while (newSize > oldSize) {
			it = firstState.getProductionSet().iterator();
			while (it.hasNext()) {
				Character charAfterPoint = it.next().charAfterPoint();
				if (charAfterPoint == null) {
					continue; // 为空就操作下一个产生式
				}
				// 扫描产生式的拓展表，找到拓展表左部是charAfterPoint的产生式加入到结点的产生式集合中
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
		
		////////////////////////////////更新全局信息////////////////////////////
		firstState.setCategoryID(StateDFA.StateID);
		++StateDFA.StateID;
		StateDFA.AllStateList.add(firstState);
		////////////////////////////////////////////////////////////////////
		
		////////////////从第一个状态开始产生其他状态///////////////////////
		while(listP<StateDFA.AllStateList.size()) {
			//从AllStateList中取出未处理的状态
			StateDFA sd = StateDFA.AllStateList.get(listP);
			//遍历所有文法符号
			Iterator<Character> itV = grammar.getV().iterator();
			while(itV.hasNext()) {
				Character sy = itV.next();
				sd.createNewStateDFA(sy); //产生新的状态
			}
			//处理完了之后将sd置入StateList;
			StateList.add(sd);
			++listP;
		}
		//////////////////////////////////////////////////////////
		
	}
	
	public void displayDFA() {	//输出产生的状态机
		//遍历Statelist表依次输出所有map
		Iterator<StateDFA> itStateList = StateList.iterator();
		while(itStateList.hasNext()) {
			StateDFA sdfa = itStateList.next();
			System.out.print(sdfa);
			//输出状态转换信息
			if(sdfa.getTransform().isEmpty()) {
				System.out.println("本结点为终端结点");
			}else {
				Iterator<Character> itmap = sdfa.getTransform().keySet().iterator();
				while(itmap.hasNext()) {
					Character c = itmap.next();
					System.out.printf("状态%d--"+c+"-->状态%d\n",sdfa.getCategoryID(),sdfa.getTransform().get(c).getCategoryID());
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
