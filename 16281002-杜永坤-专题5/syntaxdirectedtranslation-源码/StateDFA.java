package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.List;

/**识别活前缀的DFA状态类
 * */
public class StateDFA {
	public static int StateID = 0; //全局状态码，表示当前的结点生成时采用的状态码
	public static List<StateDFA> AllStateList = new ArrayList<StateDFA>(); //全局的状态集合
	//状态的类别码
	private int categoryID; 
	//产生式集
	private Set<ProductionFormula> productionSet = new HashSet<ProductionFormula>();
	//状态转换关系
	private Map<Character,StateDFA> transform = new HashMap<Character,StateDFA>();
	//需要参照拓展文法增补
	private List<ProductionFormula> grammar = AssignmentGrammar.getExpendGrammar();
	
	//在本结点的基础上根据传入参数生成下一个状态结点并返回其引用
	public StateDFA createNewStateDFA(Character ch) {
		StateDFA node = new StateDFA();
		/////////////////////////////////////////////////////////////////////////
		//构造新结点的产生式集
		//遍历本结点，将所有产生式根据ch右移一位
		Iterator<ProductionFormula> it = productionSet.iterator();
		while(it.hasNext()) {
			ProductionFormula pf = it.next();
			pf = pf.movePoint(ch);
			if(pf!=null) {
				//新结点加入生成的产生式
				node.getProductionSet().add(pf);
			}
		}
		if(node.getProductionSet().isEmpty()) //无法产生新的产生式
			return null;
		/////////////////////////////////////////////////////////////////////////
		
		
		/////////////////////////////////////////////////////////////////////////
		//添加完新生成的结点之后，要遍历集合中的产生式，将.之后的非终结符号的产生式也加进来，结束条件是集合长度不再增大
		//即求闭包操作
		
		int oldSize = 0;
		int newSize = node.getProductionSet().size();
		Set<ProductionFormula> temp = new HashSet<ProductionFormula>();
		while(newSize>oldSize) {
			oldSize = newSize;
			it = node.getProductionSet().iterator();
			while(it.hasNext()) {
				Character charAfterPoint = it.next().charAfterPoint();
				if(charAfterPoint==null) {
					continue;  //为空就操作下一个产生式
				}
				//扫描产生式的拓展表，找到拓展表左部是charAfterPoint的产生式加入到结点的产生式集合中
				for(int i = 0;i<grammar.size();++i) {
					if(grammar.get(i).getLeft().equals(charAfterPoint)) {
						temp.add(grammar.get(i)); 
					}
				}
			}
			node.getProductionSet().addAll(temp);
			newSize = node.getProductionSet().size();
			temp.clear();
		}
		/////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		//对于产生的状态结点，要检测其是否存在
		int ID = -1;
		if((ID=node.isExist())==-1) { //不存在
			//分类类编码给结点类别码
			node.setCategoryID(StateID);
			//新结点加入到所有状态表中
			AllStateList.add(node);
			++StateID;
			//将新生成的结点与本结点建立联系
			this.buildRelation(ch, node);
			//System.out.println("--产生新的状态结点--"+StateID);
		}else { //在表中已经存在
			//取出那个已存在的结点对象，建立联系
			node = AllStateList.get(ID);
			this.buildRelation(ch, node);
			//System.out.println("--没有产生新的状态结点--"+ID);
			return null; //表示没有产生新的几点状态
		}
		return node;
	}
	//将新生成的状态结点与本结点建立联系
	public void buildRelation(Character c,StateDFA SDFA) {
		this.transform.put(c, SDFA);
	}
	
	//判断这个状态是否存在，存在就返回类别码，否则返回-1
	public int isExist() {
		int ID = -1;
		Iterator<StateDFA> iterAll = AllStateList.iterator();
		boolean exist = false;
		while(iterAll.hasNext()) {
			StateDFA allpf = iterAll.next();
			//比较两个状态中的产生式是否一样
			if(allpf.getProductionSet().size()!=this.productionSet.size()) { //集合长度不一样，肯定不相等
				continue; //继续比较下一个
			}
			
			//如果两个集合长度相等，就用第一个结合中是否包含第一个集合中元素
			//已经重写了hashCode和equals方法
			ID = allpf.getCategoryID();
			Iterator<ProductionFormula> it = this.productionSet.iterator();
			exist = true;
			while(it.hasNext()) {
				if(!allpf.getProductionSet().contains(it.next())) {
					ID = -1;
					exist = false;
				}
			}
			if(exist) {
				break; //找到就不用继续找了
			}
		}
		
		return ID;
	}
	
	public void buildRelationship(Character c, StateDFA sd) {
		this.transform.put(c, sd);
	}
	
	public Set<ProductionFormula> getProductionSet(){
		return this.productionSet;
	}
	
	public int getCategoryID() {
		return this.categoryID;
	}
	public void setCategoryID(int ID) {
		this.categoryID = ID;
	}
	public Map<Character,StateDFA> getTransform(){
		return this.transform;
	}
	
	//重写状态的toString方法
	public String toString() {
		String str = "";
		str += "状态";
		str += this.categoryID;
		str += ":\n";
		Iterator<ProductionFormula> it = this.productionSet.iterator();
		while(it.hasNext()) {
			str += it.next().toString();
			str += "\t";
		}
		return str+"\n";
	}
}
