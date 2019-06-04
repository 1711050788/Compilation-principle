package syntaxdirectedtranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//构造文法的First集和Follow集
public class FirstAndFollow {
	//文法
	private AssignmentGrammar grammar = new AssignmentGrammar();
	//终结符号集
	private List<Character> Vt = new ArrayList<Character>();
	//非终结符号集
	private List<Character> Vn = new ArrayList<Character>();
	
	public static Character epsn = '$';
	public static Character start = 'G'; //开始符号
	public static Character end = '#';  //终止符号
	//用map来重新构造文法
	private Map<Character, String> mapGrammar = new HashMap<Character, String>();
	
	//First集
	private Map<Character,Set<Character>> First = new HashMap<Character,Set<Character>>();
	//Follow集
	private Map<Character,Set<Character>> Follow = new HashMap<Character,Set<Character>>();
	//产生式的First集
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
	
		//输入文法
		Vt.addAll(grammar.getVt());
		Vt.add(end);
		Vt.add(epsn);
		Vn.addAll(grammar.getVn());
		String []g = grammar.getGrammar();
		mapGrammar.put('G', "A");
		for(int i = 0;i < g.length;++i) {
			System.out.println(g[i].charAt(0)+"→"+ g[i].substring(3));
			mapGrammar.put(g[i].charAt(0), g[i].substring(3));
		}
		//非终结符号的First集和Follow集初始化
		for(int i = 0;i<Vn.size();++i) {
			First.put(Vn.get(i), new HashSet<Character>());
			Follow.put(Vn.get(i), new HashSet<Character>());
		}
		
		//根据文法初始化productionFirst集
		for(int i = 0;i<Vn.size();++i) {
			String str = mapGrammar.get(Vn.get(i));
			String []nArryStr = str.split("\\|");
			for(int j = 0;j<nArryStr.length;++j) {
				productionFirst.put(Vn.get(i)+"->"+nArryStr[j], new HashSet<Character>());
			}
		}
		
		//构造first集
		this.buildFirst();
		//显示first集
		//this.displayFirst();
		displayProductionFirst();
		
		//构造Follow集
		this.buildFollow();
		//显示Follow集
		this.displayFollow();
	}
	
	/**构造FIRST集
	 * 反复利用如下规则，直至FIRST集不再增大
	 * （1）若X属于Vt,则FIRST(X)={X};
	 * （2）若X属于Vn,且有X->aN(a属于Vt),则令a属于FIRST(X);若有X->$,则$属于FIRST(X);
	 * （3）若X->Y1Y2...Yk,
	 *		 ⅰ 将FIRST(Y1)中的一切非$的终结符加进FIRST(X);
			ⅱ若$属于FIRST(Y1),则将FIRST(Y2)中的一切非$的终结符加进FIRST(X);
			ⅲ 若$属于FIRST(Y1),并且$属于FIRST(Y2),则将FIRST(Y3)中的一切非$终结符加进FIRST(X);一次类推
			ⅲI若$都属于FIRST(Y1...YN),则将$加进FIRST(X)
	 * */
	private void buildFirst() {
		boolean bigger = true;
		while(bigger) {
			bigger = false;
			int setSize = 0;
			for(int i = 0;i<Vn.size();++i) {
				Character left = Vn.get(i); 				//产生式左部符号
				String right = mapGrammar.get(left);	 	//产生式右部
				String []rightnArry = right.split("\\|");	//分割产生式右部
				setSize = First.get(left).size();
				for(int k = 0;k<rightnArry.length;++k) {   //对右部的产生式一个一个处理
					int rightIndex = 0;
					Character cha = rightnArry[k].charAt(rightIndex);
					if (Vt.indexOf(cha) != -1) { 			// 是终结符号
						// 加入left的FIRST集
						First.get(left).add(cha);
						productionFirst.get(left+"->"+rightnArry[k]).add(cha);
						if(First.get(left).size()>setSize)
							bigger = true;
					} else if (Vn.indexOf(cha) != -1) { 	// 是非终结符号
						Set<Character> Y;
						do {
							if(rightIndex>=rightnArry[k].length()) {
								//说明到最后Y的first集中都有$,此时应该将$加入first集
								productionFirst.get(left+"->"+rightnArry[k]).add('$');
								First.get(left).add('$');
								break;
							}
							cha = rightnArry[k].charAt(rightIndex);
							Y = First.get(cha);

							//把Y的First集（除$）放入X的First集
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
	//显示FIRST集
	public void displayFirst() {
		System.out.println("文法的FIRST集如下：");
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
		System.out.println("文法的FIRST集如下：");
		Set<String> left = productionFirst.keySet();
		Iterator<String> it = left.iterator();
		while(it.hasNext()) {
			String str = (String) it.next();
			System.out.println(str+":"+productionFirst.get(str));
		}
	}
	
	/**构造FOLLOW集
	 * ①令# ∈FOLLOW(S)        S为文法开始符号
	 * ②对A→ αBβ,  且β ≠ ε
	  	则将 FIRST(β) -{ε}加入FOLLOW(B)中
	   ③反复, 直至每一个FOLLOW(A)不再增大 
	           对A→ αB或A→ αBβ(且ε ∈ FIRST(β)) 则FOLLOW(A)中的全部元素加入FOLLOW(B)
	 * */
	private void buildFollow() {
		//#属于FOLLOW(G)
		Follow.get('G').add(end);
		boolean bigger = true;
		while(bigger) {
			bigger=false;
			int setSize = 0;
			for(int i = 0;i < Vn.size();++i) {
				Character left = Vn.get(i); //产生式左部符号
				String right = mapGrammar.get(left); //产生式右部
				int rightIndex = 0;
				
				//对产生式的右部进行操作
				while(rightIndex<right.length()) {
					Character firstChar = right.charAt(rightIndex);
					if(Vt.indexOf(firstChar)!=-1 || firstChar.equals('|')) { //终结符号
						++rightIndex;
						continue;
					}
					if(right.length()>rightIndex+1) { //还可以继续识别符号
						Character secondChar = right.charAt(rightIndex+1);
						if(secondChar.equals('|')) {  //达到产生的尾部了
							//将left的Follow集加入到firstChar的Follow集
							setSize=Follow.get(firstChar).size();
							Follow.get(firstChar).addAll(Follow.get(left));
							if(Follow.get(firstChar).size()>setSize)
								bigger = true;
							rightIndex+=2;
							continue;
						}
						if(Vt.indexOf(secondChar)!=-1) { //终结符号，移入firstChar的Follow集
							//System.out.println(firstChar);
							setSize=Follow.get(firstChar).size();
							Follow.get(firstChar).add(secondChar);
							if(Follow.get(firstChar).size()>setSize)
								bigger = true;
						}else if(Vn.indexOf(secondChar)!=-1) { //非终结符号
							//将second的FIRST集元素除$移入firstChar的Follow集
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
							
							//如果$属于secondChar的First集，将left的Follow集全部加入firstChar的Follow集
							if(First.get(secondChar).contains(epsn)) {
								setSize=Follow.get(firstChar).size();
								Follow.get(firstChar).addAll(Follow.get(left));
								if(Follow.get(firstChar).size()>setSize)
									bigger = true;
							}
						}
					}else {	//没有符号了
						//将left的Follow集全部加入firstChar的Follow集
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
	
	// 显示Follow集
	public void displayFollow() {
		System.out.println("文法的FOLLOW集如下：");
		for (int i = 0; i < Vn.size(); ++i) {
			System.out.print(Vn.get(i) + ":");
			Iterator<Character> it = Follow.get(Vn.get(i)).iterator();
			while (it.hasNext()) {
				System.out.print(it.next() + " ");
			}
			System.out.println();
		}
	}
	//输出文法
	public void displayGrammar() {
		System.out.println("******文法如下******");
		for(int i = 0;i<mapGrammar.size();++i) {
			System.out.println(Vn.get(i)+"->"+mapGrammar.get(Vn.get(i)));
		}
	}
}
