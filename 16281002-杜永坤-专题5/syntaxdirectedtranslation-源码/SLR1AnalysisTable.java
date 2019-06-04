package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//SLR1��������
public class SLR1AnalysisTable {
	//������״̬ת��DFA
	private DFAManager dfaM = null;
	
	//�����״̬���ս���ŵ�Follow��
	private Map<Character,Set<Character>> Follow = new FirstAndFollow().getFollowSet();
	
	//SLR1������
	private Map<String,String> SLR1Table = new HashMap<String,String>();
	
	public SLR1AnalysisTable(DFAManager dfaM) {
		this.dfaM = dfaM;
	}
	//����SLR1������
	public void buildSLR1Table() {
		List<StateDFA> stateList = dfaM.getStateList();
		//acc��Ŀ�Ĳ���ʽ
		ProductionFormula acc = new ProductionFormula(dfaM.getGrammar().getStartSymbol()
													+"->"
													+dfaM.getGrammar().getGrammar()[0].charAt(0)
													+".");
		
		//����״̬������ȡ�����е�map��Ϣ
		for(int i = 0;i<stateList.size();++i) {
			StateDFA sdfa = stateList.get(i); //��ǰ״̬���
			//���map,���map�ǿգ���Ȼ�ǹ�Լ��Ŀ������Follow����Լ���������Լ�Ĳ���ʽ���
			//���map��Ϊ�գ�ҲҪ��ѯ�����ʽ�������Ƿ��й�Լ����ʽ��������Follow����Լ������״̬ת��
			if(sdfa.getTransform().isEmpty()) {
				//��������ʽ��
				Set<ProductionFormula> pfSet = sdfa.getProductionSet();
				Iterator<ProductionFormula> itPro = pfSet.iterator();
				while(itPro.hasNext()) {
					ProductionFormula pf = itPro.next();
					Character left = pf.getLeft(); //�õ�����ʽ���󲿣����ڲ�ѯFollow��
					String pro = pf.getformula().replace(".", ""); //ȥ������ʽ�е�.
					int index = dfaM.getGrammar().getBasicGrammar().indexOf(pro); //����ʽ�����
					Set<Character> followSet = Follow.get(left);
					Iterator<Character> itFollow = followSet.iterator();
					while(itFollow.hasNext()) {
						Character symbol = itFollow.next();
						String tableLeft = sdfa.getCategoryID()+","+symbol;
						String tableRight = "r"+index;
						SLR1Table.put(tableLeft, tableRight); //��ӹ�Լ
					}
				}
			}else { //���ƽ���Ŀ��Ҳ�����й�Լ��Ŀ�����ƽ�-��Լ��ͻ
				//��������ʽ�����ҳ����еĹ�Լ��Ŀ���й�Լ
				Set<ProductionFormula> pfSet = sdfa.getProductionSet();
				Iterator<ProductionFormula> itPro = pfSet.iterator();
				while(itPro.hasNext()) { //����Ѱ�ҹ�Լ��Ŀ
					
					ProductionFormula pf = itPro.next();
					Character left = pf.getLeft(); //�õ�����ʽ���󲿣����ڲ�ѯFollow��
					if(pf.getformula().indexOf('.')==(pf.getformula().length()-1)) { //.��ĩβ,��Լ
						String pro = pf.getformula().replace(".", ""); //ȥ������ʽ�е�.
						int index = dfaM.getGrammar().getBasicGrammar().indexOf(pro); //����ʽ�����
						Set<Character> followSet = Follow.get(left);
						Iterator<Character> itFollow = followSet.iterator();
						while(itFollow.hasNext()) {
							Character symbol = itFollow.next();
							String tableLeft = sdfa.getCategoryID()+","+symbol;
							String tableRight = "r"+index;
							SLR1Table.put(tableLeft, tableRight); //��ӹ�Լ
						}
					}
				}
				//�ƽ���Ŀ,����map
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
	
	//��ʾSLR1������
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
