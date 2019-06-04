package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.List;

/**ʶ���ǰ׺��DFA״̬��
 * */
public class StateDFA {
	public static int StateID = 0; //ȫ��״̬�룬��ʾ��ǰ�Ľ������ʱ���õ�״̬��
	public static List<StateDFA> AllStateList = new ArrayList<StateDFA>(); //ȫ�ֵ�״̬����
	//״̬�������
	private int categoryID; 
	//����ʽ��
	private Set<ProductionFormula> productionSet = new HashSet<ProductionFormula>();
	//״̬ת����ϵ
	private Map<Character,StateDFA> transform = new HashMap<Character,StateDFA>();
	//��Ҫ������չ�ķ�����
	private List<ProductionFormula> grammar = AssignmentGrammar.getExpendGrammar();
	
	//�ڱ����Ļ����ϸ��ݴ������������һ��״̬��㲢����������
	public StateDFA createNewStateDFA(Character ch) {
		StateDFA node = new StateDFA();
		/////////////////////////////////////////////////////////////////////////
		//�����½��Ĳ���ʽ��
		//��������㣬�����в���ʽ����ch����һλ
		Iterator<ProductionFormula> it = productionSet.iterator();
		while(it.hasNext()) {
			ProductionFormula pf = it.next();
			pf = pf.movePoint(ch);
			if(pf!=null) {
				//�½��������ɵĲ���ʽ
				node.getProductionSet().add(pf);
			}
		}
		if(node.getProductionSet().isEmpty()) //�޷������µĲ���ʽ
			return null;
		/////////////////////////////////////////////////////////////////////////
		
		
		/////////////////////////////////////////////////////////////////////////
		//����������ɵĽ��֮��Ҫ���������еĲ���ʽ����.֮��ķ��ս���ŵĲ���ʽҲ�ӽ��������������Ǽ��ϳ��Ȳ�������
		//����հ�����
		
		int oldSize = 0;
		int newSize = node.getProductionSet().size();
		Set<ProductionFormula> temp = new HashSet<ProductionFormula>();
		while(newSize>oldSize) {
			oldSize = newSize;
			it = node.getProductionSet().iterator();
			while(it.hasNext()) {
				Character charAfterPoint = it.next().charAfterPoint();
				if(charAfterPoint==null) {
					continue;  //Ϊ�վͲ�����һ������ʽ
				}
				//ɨ�����ʽ����չ���ҵ���չ������charAfterPoint�Ĳ���ʽ���뵽���Ĳ���ʽ������
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
		//���ڲ�����״̬��㣬Ҫ������Ƿ����
		int ID = -1;
		if((ID=node.isExist())==-1) { //������
			//������������������
			node.setCategoryID(StateID);
			//�½����뵽����״̬����
			AllStateList.add(node);
			++StateID;
			//�������ɵĽ���뱾��㽨����ϵ
			this.buildRelation(ch, node);
			//System.out.println("--�����µ�״̬���--"+StateID);
		}else { //�ڱ����Ѿ�����
			//ȡ���Ǹ��Ѵ��ڵĽ����󣬽�����ϵ
			node = AllStateList.get(ID);
			this.buildRelation(ch, node);
			//System.out.println("--û�в����µ�״̬���--"+ID);
			return null; //��ʾû�в����µļ���״̬
		}
		return node;
	}
	//�������ɵ�״̬����뱾��㽨����ϵ
	public void buildRelation(Character c,StateDFA SDFA) {
		this.transform.put(c, SDFA);
	}
	
	//�ж����״̬�Ƿ���ڣ����ھͷ�������룬���򷵻�-1
	public int isExist() {
		int ID = -1;
		Iterator<StateDFA> iterAll = AllStateList.iterator();
		boolean exist = false;
		while(iterAll.hasNext()) {
			StateDFA allpf = iterAll.next();
			//�Ƚ�����״̬�еĲ���ʽ�Ƿ�һ��
			if(allpf.getProductionSet().size()!=this.productionSet.size()) { //���ϳ��Ȳ�һ�����϶������
				continue; //�����Ƚ���һ��
			}
			
			//����������ϳ�����ȣ����õ�һ��������Ƿ������һ��������Ԫ��
			//�Ѿ���д��hashCode��equals����
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
				break; //�ҵ��Ͳ��ü�������
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
	
	//��д״̬��toString����
	public String toString() {
		String str = "";
		str += "״̬";
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
