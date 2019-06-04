package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//�����ķ���First����Follow��
public class FirstAndFollow {
	//�ķ�
	private AssignmentGrammar grammar = new AssignmentGrammar();
	//�ս���ż�
	private List<Character> Vt = new ArrayList<Character>();
	//���ս���ż�
	private List<Character> Vn = new ArrayList<Character>();
	
	public static Character epsn = '$';
	public static Character start = 'G'; //��ʼ����
	public static Character end = '#';  //��ֹ����
	//��map�����¹����ķ�
	private Map<Character, String> mapGrammar = new HashMap<Character, String>();
	
	//First��
	private Map<Character,Set<Character>> First = new HashMap<Character,Set<Character>>();
	//Follow��
	private Map<Character,Set<Character>> Follow = new HashMap<Character,Set<Character>>();
	//����ʽ��First��
	private Map<String,Set<Character>> productionFirst = new HashMap<String,Set<Character>>();
	
	public Map<Character,Set<Character>> getFirstSet(){
		return First;
	}
	
	public Map<Character,Set<Character>> getFollowSet(){
		return Follow;
	}
	
	public Map<Character, String> getGrammar(){
		return mapGrammar;
	}
	public Map<String,Set<Character>> getProductionFirstSet(){
		return productionFirst;
	}
	public List<Character> getVt(){
		return this.Vt;
	}
	public FirstAndFollow() {
	
		//�����ķ�
		Vt.addAll(grammar.getVt());
		Vt.add(end);
		Vt.add(epsn);
		Vn.addAll(grammar.getVn());
		String []g = grammar.getGrammar();
		mapGrammar.put('G', "A");
		for(int i = 0;i < g.length;++i) {
			System.out.println(g[i].charAt(0)+"��"+ g[i].substring(3));
			mapGrammar.put(g[i].charAt(0), g[i].substring(3));
		}
		//���ս���ŵ�First����Follow����ʼ��
		for(int i = 0;i<Vn.size();++i) {
			First.put(Vn.get(i), new HashSet<Character>());
			Follow.put(Vn.get(i), new HashSet<Character>());
		}
		
		//�����ķ���ʼ��productionFirst��
		for(int i = 0;i<Vn.size();++i) {
			String str = mapGrammar.get(Vn.get(i));
			String []nArryStr = str.split("\\|");
			for(int j = 0;j<nArryStr.length;++j) {
				productionFirst.put(Vn.get(i)+"->"+nArryStr[j], new HashSet<Character>());
			}
		}
		
		//����first��
		this.buildFirst();
		//��ʾfirst��
		//this.displayFirst();
		displayProductionFirst();
		
		//����Follow��
		this.buildFollow();
		//��ʾFollow��
		this.displayFollow();
	}
	
	/**����FIRST��
	 * �����������¹���ֱ��FIRST����������
	 * ��1����X����Vt,��FIRST(X)={X};
	 * ��2����X����Vn,����X->aN(a����Vt),����a����FIRST(X);����X->$,��$����FIRST(X);
	 * ��3����X->Y1Y2...Yk,
	 *		 �� ��FIRST(Y1)�е�һ�з�$���ս���ӽ�FIRST(X);
			����$����FIRST(Y1),��FIRST(Y2)�е�һ�з�$���ս���ӽ�FIRST(X);
			�� ��$����FIRST(Y1),����$����FIRST(Y2),��FIRST(Y3)�е�һ�з�$�ս���ӽ�FIRST(X);һ������
			��I��$������FIRST(Y1...YN),��$�ӽ�FIRST(X)
	 * */
	private void buildFirst() {
		boolean bigger = true;
		while(bigger) {
			bigger = false;
			int setSize = 0;
			for(int i = 0;i<Vn.size();++i) {
				Character left = Vn.get(i); 				//����ʽ�󲿷���
				String right = mapGrammar.get(left);	 	//����ʽ�Ҳ�
				String []rightnArry = right.split("\\|");	//�ָ����ʽ�Ҳ�
				setSize = First.get(left).size();
				for(int k = 0;k<rightnArry.length;++k) {   //���Ҳ��Ĳ���ʽһ��һ������
					int rightIndex = 0;
					Character cha = rightnArry[k].charAt(rightIndex);
					if (Vt.indexOf(cha) != -1) { 			// ���ս����
						// ����left��FIRST��
						First.get(left).add(cha);
						productionFirst.get(left+"->"+rightnArry[k]).add(cha);
						if(First.get(left).size()>setSize)
							bigger = true;
					} else if (Vn.indexOf(cha) != -1) { 	// �Ƿ��ս����
						Set<Character> Y;
						do {
							if(rightIndex>=rightnArry[k].length()) {
								//˵�������Y��first���ж���$,��ʱӦ�ý�$����first��
								productionFirst.get(left+"->"+rightnArry[k]).add('$');
								First.get(left).add('$');
								break;
							}
							cha = rightnArry[k].charAt(rightIndex);
							Y = First.get(cha);

							//��Y��First������$������X��First��
							Iterator<Character> it = Y.iterator();
							while (it.hasNext()) {
								Character tempc = (Character) it.next();
								if (!tempc.equals('$')) {
									productionFirst.get(left+"->"+rightnArry[k]).add(tempc);
									First.get(left).add(tempc);
								}
							}
							++rightIndex;
						} while (Y.contains('$'));
						
						if(First.get(left).size()>setSize)
							bigger = true;
					}
				}
			}
			
		}
	}
	//��ʾFIRST��
	public void displayFirst() {
		System.out.println("�ķ���FIRST�����£�");
		for(int i = 0;i < Vn.size();++i) {
			System.out.print(Vn.get(i)+":");
			Iterator<Character> it = First.get(Vn.get(i)).iterator();
			while(it.hasNext()) {
				System.out.print(it.next()+" ");
			}
			System.out.println();
		}
	}
	
	public void displayProductionFirst() {
		System.out.println("�ķ���FIRST�����£�");
		Set<String> left = productionFirst.keySet();
		Iterator<String> it = left.iterator();
		while(it.hasNext()) {
			String str = (String) it.next();
			System.out.println(str+":"+productionFirst.get(str));
		}
	}
	
	/**����FOLLOW��
	 * ����# ��FOLLOW(S)        SΪ�ķ���ʼ����
	 * �ڶ�A�� ��B��,  �Ҧ� �� ��
	  	�� FIRST(��) -{��}����FOLLOW(B)��
	   �۷���, ֱ��ÿһ��FOLLOW(A)�������� 
	           ��A�� ��B��A�� ��B��(�Ҧ� �� FIRST(��)) ��FOLLOW(A)�е�ȫ��Ԫ�ؼ���FOLLOW(B)
	 * */
	private void buildFollow() {
		//#����FOLLOW(G)
		Follow.get('G').add(end);
		boolean bigger = true;
		while(bigger) {
			bigger=false;
			int setSize = 0;
			for(int i = 0;i < Vn.size();++i) {
				Character left = Vn.get(i); //����ʽ�󲿷���
				String right = mapGrammar.get(left); //����ʽ�Ҳ�
				int rightIndex = 0;
				
				//�Բ���ʽ���Ҳ����в���
				while(rightIndex<right.length()) {
					Character firstChar = right.charAt(rightIndex);
					if(Vt.indexOf(firstChar)!=-1 || firstChar.equals('|')) { //�ս����
						++rightIndex;
						continue;
					}
					if(right.length()>rightIndex+1) { //�����Լ���ʶ�����
						Character secondChar = right.charAt(rightIndex+1);
						if(secondChar.equals('|')) {  //�ﵽ������β����
							//��left��Follow�����뵽firstChar��Follow��
							setSize=Follow.get(firstChar).size();
							Follow.get(firstChar).addAll(Follow.get(left));
							if(Follow.get(firstChar).size()>setSize)
								bigger = true;
							rightIndex+=2;
							continue;
						}
						if(Vt.indexOf(secondChar)!=-1) { //�ս���ţ�����firstChar��Follow��
							//System.out.println(firstChar);
							setSize=Follow.get(firstChar).size();
							Follow.get(firstChar).add(secondChar);
							if(Follow.get(firstChar).size()>setSize)
								bigger = true;
						}else if(Vn.indexOf(secondChar)!=-1) { //���ս����
							//��second��FIRST��Ԫ�س�$����firstChar��Follow��
							setSize=Follow.get(firstChar).size();
							Iterator<Character> it = First.get(secondChar).iterator();
							while (it.hasNext()) {
								Character tempc = (Character) it.next();
								if (!tempc.equals('$')) {
									Follow.get(firstChar).add(tempc);
								}
							}
							if(Follow.get(firstChar).size()>setSize)
								bigger = true;
							
							//���$����secondChar��First������left��Follow��ȫ������firstChar��Follow��
							if(First.get(secondChar).contains(epsn)) {
								setSize=Follow.get(firstChar).size();
								Follow.get(firstChar).addAll(Follow.get(left));
								if(Follow.get(firstChar).size()>setSize)
									bigger = true;
							}
						}
					}else {	//û�з�����
						//��left��Follow��ȫ������firstChar��Follow��
						setSize=Follow.get(firstChar).size();
						Follow.get(firstChar).addAll(Follow.get(left));
						if(Follow.get(firstChar).size()>setSize)
							bigger = true;
					}
					++rightIndex;
				}
			}
		}
	}
	
	// ��ʾFollow��
	public void displayFollow() {
		System.out.println("�ķ���FOLLOW�����£�");
		for (int i = 0; i < Vn.size(); ++i) {
			System.out.print(Vn.get(i) + ":");
			Iterator<Character> it = Follow.get(Vn.get(i)).iterator();
			while (it.hasNext()) {
				System.out.print(it.next() + " ");
			}
			System.out.println();
		}
	}
	//����ķ�
	public void displayGrammar() {
		System.out.println("******�ķ�����******");
		for(int i = 0;i<mapGrammar.size();++i) {
			System.out.println(Vn.get(i)+"->"+mapGrammar.get(Vn.get(i)));
		}
	}
}
