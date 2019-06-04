package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//SLR1分析表类
public class SLR1AnalysisTable {
	//首先有状态转换DFA
	private DFAManager dfaM = null;
	
	//其次有状态非终结符号的Follow集
	private Map<Character,Set<Character>> Follow = new FirstAndFollow().getFollowSet();
	
	//SLR1分析表
	private Map<String,String> SLR1Table = new HashMap<String,String>();
	
	public SLR1AnalysisTable(DFAManager dfaM) {
		this.dfaM = dfaM;
	}
	//构建SLR1分析表
	public void buildSLR1Table() {
		List<StateDFA> stateList = dfaM.getStateList();
		//acc项目的产生式
		ProductionFormula acc = new ProductionFormula(dfaM.getGrammar().getStartSymbol()
													+"->"
													+dfaM.getGrammar().getGrammar()[0].charAt(0)
													+".");
		
		//遍历状态链，获取其所有的map信息
		for(int i = 0;i<stateList.size();++i) {
			StateDFA sdfa = stateList.get(i); //当前状态结点
			//检查map,如果map是空，必然是归约项目，按照Follow集归约，计算其归约的产生式序号
			//如果map不为空，也要查询其产生式集合中是否有归约产生式集，考察Follow集归约，否则状态转换
			if(sdfa.getTransform().isEmpty()) {
				//遍历产生式集
				Set<ProductionFormula> pfSet = sdfa.getProductionSet();
				Iterator<ProductionFormula> itPro = pfSet.iterator();
				while(itPro.hasNext()) {
					ProductionFormula pf = itPro.next();
					Character left = pf.getLeft(); //得到产生式的左部，用于查询Follow集
					String pro = pf.getformula().replace(".", ""); //去掉产生式中的.
					int index = dfaM.getGrammar().getBasicGrammar().indexOf(pro); //产生式的序号
					Set<Character> followSet = Follow.get(left);
					Iterator<Character> itFollow = followSet.iterator();
					while(itFollow.hasNext()) {
						Character symbol = itFollow.next();
						String tableLeft = sdfa.getCategoryID()+","+symbol;
						String tableRight = "r"+index;
						SLR1Table.put(tableLeft, tableRight); //添加归约
					}
				}
			}else { //有移进项目，也可能有归约项目，有移进-归约冲突
				//遍历产生式集，找出其中的归约项目进行归约
				Set<ProductionFormula> pfSet = sdfa.getProductionSet();
				Iterator<ProductionFormula> itPro = pfSet.iterator();
				while(itPro.hasNext()) { //遍历寻找归约项目
					
					ProductionFormula pf = itPro.next();
					Character left = pf.getLeft(); //得到产生式的左部，用于查询Follow集
					if(pf.getformula().indexOf('.')==(pf.getformula().length()-1)) { //.在末尾,归约
						String pro = pf.getformula().replace(".", ""); //去掉产生式中的.
						int index = dfaM.getGrammar().getBasicGrammar().indexOf(pro); //产生式的序号
						Set<Character> followSet = Follow.get(left);
						Iterator<Character> itFollow = followSet.iterator();
						while(itFollow.hasNext()) {
							Character symbol = itFollow.next();
							String tableLeft = sdfa.getCategoryID()+","+symbol;
							String tableRight = "r"+index;
							SLR1Table.put(tableLeft, tableRight); //添加归约
						}
					}
				}
				//移进项目,检索map
				Set<Character> relaLeft = sdfa.getTransform().keySet();
				Iterator<Character> itRela =relaLeft.iterator();
				while(itRela.hasNext()) {
					Character symbol = itRela.next();
					int index = sdfa.getTransform().get(symbol).getCategoryID();
					String tableLeft = sdfa.getCategoryID()+","+symbol;
					String tableRight = "";
					if(dfaM.getGrammar().getVt().contains(symbol))
						tableRight = "s"+index;
					else
						tableRight = ""+index;
					SLR1Table.put(tableLeft, tableRight);
				}
			}
			if(sdfa.getProductionSet().contains(acc)) {
				SLR1Table.put(sdfa.getCategoryID()+","+"#","acc");
			}
			
		}
	}
	
	//显示SLR1分析表
	public void displayTable() {
		List<Character> Vt = new ArrayList<Character>();
		List<Character> Vn = new ArrayList<Character>();
		//List<Character> V = new ArrayList<Character>();
		Vt.addAll(dfaM.getGrammar().getVt());
		Vt.add('#');
		Vn.addAll(dfaM.getGrammar().getVn());
		System.out.println("\t\t\t\t\tACTION\t\t\t\t\t\t\t\tGOTO");
		for(int i = 0;i<Vt.size();++i) {
			System.out.print("\t"+Vt.get(i));
		}
		System.out.print("\t|");
		for(int i = 0;i<Vn.size();++i) {
			System.out.print("\t"+Vn.get(i));
		}
		System.out.println();
		for(int i = 0;i<dfaM.getStateList().size();++i) {
			System.out.print(i+"\t");
			for(int jt = 0;jt<Vt.size();++jt) {
				String left = i+","+Vt.get(jt);
				if(SLR1Table.get(left)!=null) {
					System.out.print(SLR1Table.get(left)+"\t");
				}else {
					System.out.print("\t");
				}
			}
			System.out.print("|\t");
			for(int jn = 0;jn<Vn.size();++jn) {
				String left = i+","+Vn.get(jn);
				if(SLR1Table.get(left)!=null) {
					System.out.print(SLR1Table.get(left)+"\t");
				}else {
					System.out.print("\t");
				}
			}
			System.out.println();
		}
	}
	
	public Map<String,String> getSLR1Table(){
		return this.SLR1Table;
	}
}
