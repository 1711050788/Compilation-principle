
 package grammaticalanalysis;

 import java.io.BufferedReader;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;
 import java.util.Stack;
 import java.util.HashSet;


/**LL(1)�﷨��������ں���
 * ʵ��Ŀ�꣺
 * 		������������������ʽ��LL(1)�ķ��ķ�����
		G[P]: 
        P��S|Q|; 
		S��V=E;
		E��TR
		R��ATR|$
		T��FY
		Y��MFY|$
		F��CZ
		Z��OCZ|$
		C��BI
		I��XBI|$
		B��(E)|i
		A��+|-
		M��*|/
		X��a|o           //a��ʾ�߼�����&&��o��ʾ�߼�����||
		O��t|d|g|l|u|e  //t��ʾ>=��d��ʾ<=��g��ʾ>,l��ʾ<,e��ʾ==��u��ʾ!=
		V��i
		Q��8JKH          //8��ʾif�ڷ��ű������
		H��fJKH|9K|$     //f ��ʾ else if���ŵ���ϡ�9��ʾelse�ڷ��ű��е����
		J��(E)           //�߼����
		K��S|{U}|;       //if��������
		U��PU|{U}U|$

 * ʵ��˵����
 * 		�ս����iΪ�û�����ļ򵥱���������ʶ���Ķ��塣
 * ʵ��Ҫ��
 * 		1�����봮Ӧ�Ǵʷ������������Ԫʽ���У���ĳ�������ʽ��ר��1������������
 * 			���Ϊ���봮�Ƿ�Ϊ���ķ�������������ʽ���жϽ����
 * 		2���ݹ��½���������Ӧ�ܷ������봮����
 * 		3��������������������������걸����ȷ�ͳ��������������Խ����
 * 
 * */
public class LL1PredictionAnalysisMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//FirstAndFollow faf = new FirstAndFollow();
		//faf.displayGrammar();
		//1������Ԥ�������
		LL1PredictionAnalysis pa = new LL1PredictionAnalysis("��Ԫʽ�ļ�.tys");
		//2������Ԥ�����
		pa.analysisProcessing();
	}

}






 //
  class LL1PredictionAnalysis {
 	private BufferedReader br = null; //����Ķ�Ԫʽ�ļ���
 	private static List<String> InputStream = new ArrayList<String>(); //�Ӷ�Ԫʽ�ļ��в��ķ��Ŵ�������
 	private int indexP = 0; //ɨ��ָ�룬��ʼΪ0
 	private Map<String,String> LL1Table = new HashMap<String,String>();
 	private FirstAndFollow faf = new FirstAndFollow(); //�ķ�G��FIRST����FOLLOW��
 	
 	private Stack<Character> analysisStack = new Stack<Character>(); //����ջ
 	private int tab = 1; //�Ʒ������������ʼΪ1
 	//���캯��
 	public LL1PredictionAnalysis(String fileName) {
 		File fp = new File(fileName);
 		if(!fp.getName().endsWith(".tys")) {
 			System.out.println("�ļ���ʽ����ȷ...");
 			return;
 		}
 		//�����ļ�ɨ��
 		try {
 			br = new BufferedReader(new InputStreamReader(new FileInputStream(fp.getName())));
 			String erYuanShi = "";
 			while((erYuanShi=br.readLine())!=null) {
 				//��ȡ���Ŵ�
 				
 					InputStream.add(erYuanShi.substring(erYuanShi.indexOf(",") + 1, erYuanShi.lastIndexOf(")")));
 				
 				
 			}
 			InputStream.add("#");  //ĩβ���#��
 			//���һ������
 			System.out.print("�����Դ����Ϊ��");
 			int l=InputStream.size();
 			for(int i = 0;i<l-1;++i) {
 				print_indexp(i);
 			}
 			System.out.println();
 		} catch (FileNotFoundException e) {
 			// TODO Auto-generated catch block
 			System.out.println(fileName+"�ļ�������...");
 			e.printStackTrace();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		
 		//���������
 		buildLLR1Table();
 		//��ʾ������
 		displayLLR1Table();
 		
 		//����ջ�������ս���ź��ķ��Ŀ�ʼ����
 		analysisStack.push(FirstAndFollow.end);
 		analysisStack.push(FirstAndFollow.start);
 	}
 	
 	//����LLR(1)�ķ���Ԥ���
 	private void buildLLR1Table() {
 		//LLR1Table����ʽ��[E,i]��E->TR
 		
 		//����ÿһ������ʽ
 		Set<String> X = faf.getProductionFirstSet().keySet();
 		Iterator<String> it = X.iterator();
 		while(it.hasNext()) {
 			String pro = (String) it.next();
 			String temp = pro.substring(3); //��ȡ����ʽ->�ұߵ��ַ��ж��ǲ���$,�Ӷ�ѡ��FIRST������FOLLOW��
 			Set<Character> symbol;
 			if(temp.equals("$")) {
 				symbol = faf.getFollowSet().get(pro.charAt(0));
 			}
 			else{
 				symbol = faf.getProductionFirstSet().get(pro);
 			}
 			Iterator<Character> itSymbol = symbol.iterator();
 			while(itSymbol.hasNext()) {
 				Character sy = (Character) itSymbol.next();
 				LL1Table.put(pro.charAt(0)+","+sy, pro);
 			}
 		}
 	}
 	
 	//��ʾ������
 	private void displayLLR1Table() {
 		System.out.println("\n\n\t\t\t\tLL(1)������\t\t\t");
 		for(int i = 0;i<faf.getVt().size();++i) {
 			System.out.print("\t"+faf.getVt().get(i));
 		}
 		System.out.println();
 		for(int i = 0;i<faf.getVn().size();++i) {
 			System.out.print(faf.getVn().get(i));
 			for(int j = 0;j<faf.getVt().size();++j) {
 				String str = faf.getVn().get(i)+","+faf.getVt().get(j);
 				String pro = LL1Table.get(str);
 				if(pro!=null) {
 					System.out.print("\t"+pro);
 				}
 				else {
 					System.out.print("\t");
 				}
 			}
 			System.out.println();
 		}
 	}
 	
 	//Ԥ�����ִ��
 	public void analysisProcessing() {
 		System.out.println("\n\nԤ��������̱�");
 		//System.out.println("����ջ\t\t\t\t�������봮\t\t\t\t���ò���ʽ");
 		while(!analysisStack.isEmpty() && indexP<InputStream.size()) {
 			String Input = get_indexP(indexP);
 		//	System.out.println(Input);
 			//if(Input.length()>1||Input.length()==1&&Character.isLetter(Input.charAt(0))) { 
 				//���ų��ȴ���1ʱ����ʾ��һ������������ת����i
 				//�����ǳ��ȵ���1����ĸ������Ҳת����i
 			//	Input = "i";
 			//}
 			//����ջջջ��Ԫ�ص�ջ
 			displayProcessing("");
 			Character analysisSymbol = (Character) analysisStack.pop();
 			
 			
 			
 			if(faf.getVt().indexOf(analysisSymbol)!=-1&&!analysisSymbol.equals(FirstAndFollow.end)) { //���ս���ţ�ʶ�����������
 				if(analysisSymbol==Input.charAt(0))
 				{
 					++indexP;
 					continue;
 				}
 				else if(analysisSymbol=='$'&&Input.charAt(0)=='#')
 				{
 					++indexP;
 					continue;
 					
 				}else
 				{
 					System.out.println("����ʧ�ܣ�"+"�������ʽӦ�ð����ַ���"+analysisSymbol);
 					return ;
 				}
 				
 			}
 			
 			//System.out.println("��ջԪ�أ�"+analysisSymbol+"\n"+"����Ԫ�أ�"+Input);
 			
 			if(analysisSymbol==(FirstAndFollow.end)&&Input.equals("#")) {
 				++indexP;
 				continue;
 			}
 			if(analysisSymbol=='$'&&Input.equals("#")) {
 				++indexP;
 				
 				continue;
 			}
 			//System.out.println(analysisSymbol+Input);
 			//����ջ��Ԫ��������Ԫ�ص���ϲ��ҷ������ҵ���Ӧ�Ĳ���ʽ
 			
 			String temp = LL1Table.get(analysisSymbol+","+Input);
 			//System.out.println(analysisSymbol+","+Input);
 			if(temp==null) {
 				System.out.println("LL1�������� ["+analysisSymbol+","+Input+"] �޲���ʽ");
 				if(analysisSymbol=='K')
 				{
 					System.out.println("ȱ�ٳ�����");
 					System.out.println("����ʧ��");
 	 				return;
 				}
 				
 			}
 			else
 				
 			{
 				String production = temp.substring(3);
 	 			if(production.equals("$")) {
 	 				displayProcessing(temp);
 	 				continue;
 	 			}
 	 			char [] nArry = new StringBuffer(production).reverse().toString().toCharArray();
 	 			for(int i = 0;i<nArry.length;++i) {
 	 				analysisStack.push(nArry[i]);
 	 			}
 	 			displayProcessing(temp);
 			}
 			//����ʽ�Ҳ�������ջ
 			
 		}
 		
 		if((analysisStack.isEmpty()||analysisStack.pop()=='#'))
 		{
 			System.out.println("�����ɹ�");
 			System.out.println("Դ����Ϊ��");
 			int l=InputStream.size();
 			for(int i = 0;i<l-1;++i) {
 				print_indexp(i); 
 			}
 			System.out.println();
 		}
 			
 		else
 			System.out.println("����ʧ��");
 	}
 	
 	//����������м����
 	private void displayProcessing(String pro) {
 		System.out.print("����ջ��"+analysisStack);
 		System.out.print("\t\t��������"+InputStream.subList(+indexP, InputStream.size()));
 		System.out.println("\t\t����ʽ��"+pro);
 	}
 	
 	public void print_indexp(int i) {
		String s_curr=InputStream.get(i);
		if(i>=1)
		{
			if((InputStream.get(i-1).equals(";")&&!InputStream.get(i).equals("}"))
					||(InputStream.get(i-1).equals("else")&&!InputStream.get(i).equals("if")))
			{
				System.out.print("\n");
				for(int j=0;j<tab;j++)
				{
					System.out.print("    ");
				}
				//System.out.print("\n");
			}
			else
				if(InputStream.get(i-1).equals("}")) {
					System.out.print("\n");
					if(InputStream.get(i).equals("}"))
					{
						for(int j=0;j<tab-1;j++)
						{
							System.out.print("    ");
						}
					}
					else 
					{
						
						for(int j=0;j<tab;j++)
						{
							System.out.print("    ");
						}
					}
				}
				else if(InputStream.get(i-1).equals("{"))
				{
					if(!InputStream.get(i).equals("{"))
					{
						System.out.print("\n");
						for(int j=0;j<tab;j++)
						{
							System.out.print("    ");
						}
					}
					else
					{
						System.out.print("\n");
					}
				}
			
		}
		
		if(s_curr.equals(";"))
			System.out.print(s_curr+"\n");
		else if(s_curr.equals("{"))
		{
			if(InputStream.get(i-1).equals(")"))
			{
				System.out.print("\n");
			}
			if(!InputStream.get(i-1).equals("else"))
			{
				for(int j=0;j<tab;j++)
				{
					System.out.print("    ");
				}
			}
		
			System.out.print(s_curr);
			tab++;
		}
		else if(s_curr.equals("}"))
		{
			tab--;
			
			if(!InputStream.get(i-1).equals("}"))
			{
				
				for(int j=0;j<tab;j++)
				{
					System.out.print("    ");
				}
			}
			System.out.print(s_curr);
			
		}
		else
		{
			System.out.print(s_curr+" ");
		}
	}
	public String get_indexP(int index) {
		String s = InputStream.get(index);
		
		if(s.equals("{"))
		{
			s="{";
		}	
		else if(s.equals("}"))
		{
			s="}";
		}	
		else if(s.equals("else"))
		{
			String s1 = InputStream.get(index+1);
			if (s1.equals("if"))
			{
				s="f";
				indexP++;
			}
			else
			{
				s="9";
			}
		}	
		else if(s.equals("if"))
		{
			if(index>=1)
			{
				String s1 = InputStream.get(index-1);
				if (s1.equals("else"))
				{
					s="f";
					
				}
				else
				{
					s="8";
				}
			}
			else
			s="8";
			
		}
		else if(s.equals("&&"))
			s="a";	
		else if(s.equals("||"))
			s="o";	
		else if(s.equals("<"))
			s="l";	
		else if(s.equals("=="))
			s="e";	
		else if(s.equals("!="))
			s="u";	
		else if(s.equals(">="))
			s="t";	
		else if(s.equals("<="))
			s="d";	
		else if(s.equals(">"))
			s="g";	
		else if(s.equals(";"))
			s=";";	
		else	if((s.length()==1)&&Character.isLetterOrDigit(s.charAt(0))){
			s="i";
		}else if(s.length()>1) {
			s="i";
			for(int i = 0;i < s.length();++i) {
				if(!Character.isLetterOrDigit(s.charAt(i)))
				{
					System.out.println("δ֪���ţ�"+s);
					s="unknow";
				}
			
			}
		}
		return  s;
	}

 }
  
  /**�ķ�
   * G[P]: 
          P��S|Q|; 
  		S��V=E;
  		E��TR
  		R��ATR|$
  		T��FY
  		Y��MFY|$
  		F��CZ
  		Z��OCZ|$
  		C��BI
  		I��XBI|$
  		B��(E)|i
  		A��+|-
  		M��*|/
  		X��a|o           //a��ʾ�߼�����&&��o��ʾ�߼�����||
  		O��t|d|g|l|u|e  //t��ʾ>=��d��ʾ<=��g��ʾ>,l��ʾ<,e��ʾ==��u��ʾ!=
  		V��i
  		Q��8JKH          //8��ʾif�ڷ��ű������
  		H��fJKH|9K|$     //f ��ʾ else if���ŵ���ϡ�9��ʾelse�ڷ��ű��е����
  		J��(E)           //�߼����
  		K��S|{U}|;       //if��������
  		U��PU|{U}U|$

   * */

  //�����ķ���First����Follow��
   class FirstAndFollow {
  	//�ս���ż�
  	private List<Character> Vt = new ArrayList<Character>();
  	//���ս���ż�
  	private List<Character> Vn = new ArrayList<Character>();
  	
  	public static Character epsn = '$';//���ÿ�
  	public static Character start = 'P'; //��ʼ����
  	public static Character end = '#';  //��ֹ����
  	//��map�������ķ�
  	private Map<Character, String> grammar = new HashMap<Character, String>();
  	
  	//First��
  	private Map<Character,Set<Character>> First = new HashMap<Character,Set<Character>>();
  	//Follow��
  	private Map<Character,Set<Character>> Follow = new HashMap<Character,Set<Character>>();
  	//����ʽ��First��
  	private Map<String,Set<Character>> productionFirst = new HashMap<String,Set<Character>>();  	
  	public Map<Character,Set<Character>> getFollowSet(){
  		return Follow;
  	}
  	public Map<String,Set<Character>> getProductionFirstSet(){
  		return productionFirst;
  	}
  	public List<Character> getVt(){
  		return this.Vt;
  	}
  	public List<Character> getVn(){
  		return this.Vn;
  	}
  	public FirstAndFollow() {
  		//��ӷ��ս����
  		Vn.add('P');
  		
  		Vn.add('Q');
  		Vn.add('J');
  		Vn.add('K');
  		Vn.add('H');
  	
  		Vn.add('S');
  		Vn.add('V');
  		Vn.add('E');
  		Vn.add('R');
  		Vn.add('T');
  		Vn.add('Y');
  		Vn.add('F');
  		Vn.add('A');
  		Vn.add('M');
  		Vn.add('Z');
  		Vn.add('C');
  		Vn.add('I');
  		Vn.add('B');
  		Vn.add('X');
  		Vn.add('O');
  		Vn.add('U');

  		//����ս����
  		Vt.add('8');
  		Vt.add('9');
  		Vt.add('f');
  		Vt.add('=');
  		Vt.add('i');
  		Vt.add('+');
  		Vt.add('-');
  		Vt.add('*');
  		Vt.add('/');
  		Vt.add('(');
  		Vt.add(')');
  		Vt.add('{');
  		Vt.add('}');
  		Vt.add(';');
  		Vt.add('a');
  		Vt.add('o');
  		Vt.add('t');
  		Vt.add('d');
  		Vt.add('g');
  		Vt.add('l');
  		Vt.add('e');
  		Vt.add('u');
  		Vt.add(end);
  		Vt.add(epsn);
  	
  		//�����ķ�
  		grammar.put('P', "S|Q|;");
  		grammar.put('S', "V=E;");
  		grammar.put('E', "TR");
  		grammar.put('R', "ATR|"+epsn);
  		grammar.put('T', "FY");
  		grammar.put('Y', "MFY|"+epsn);
  		grammar.put('F', "CZ");
  		grammar.put('Z', "OCZ|"+epsn);
  		grammar.put('C', "BI");
  		grammar.put('I', "XBI|"+epsn);
  		grammar.put('B', "(E)|i");
  		grammar.put('A', "+|-");
  		grammar.put('M', "*|/");
  		grammar.put('V', "i");
  		grammar.put('Q', "8JKH");
  		grammar.put('H', "fJKH|9K|"+epsn);
  		grammar.put('J', "(E)");
  		grammar.put('K', "S|{U}|;");
  		grammar.put('U', "PU|{U}U|"+epsn);
  		grammar.put('X', "a|o");
  		grammar.put('O', "t|d|g|l|u|e");

  		//���ս���ŵ�First����Follow����ʼ��
  		for(int i = 0;i<Vn.size();++i) {
  			First.put(Vn.get(i), new HashSet<Character>());
  			Follow.put(Vn.get(i), new HashSet<Character>());
  		}
  		
  		//�����ķ���ʼ��productionFirst��
  		for(int i = 0;i<Vn.size();++i) {
  			String str = grammar.get(Vn.get(i));
  			String []nArryStr = str.split("\\|");
  			for(int j = 0;j<nArryStr.length;++j) {
  				productionFirst.put(Vn.get(i)+"->"+nArryStr[j], new HashSet<Character>());
  			}
  		}
  		
  		//����first��
  		this.buildFirst();
  		//��ʾfirst��
  		//this.displayFirst();
  		displayFirst();
  		
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
  			��i��$������FIRST(Y1...YN),��$�ӽ�FIRST(X)
  	 * */
  	private void buildFirst() {
  		boolean bigger = true;
  		while(bigger) {
  			bigger = false;
  			int setSize = 0;
  			for(int i = 0;i<Vn.size();++i) {
  				Character left = Vn.get(i); //����ʽ�󲿷���
  				String right = grammar.get(left); //����ʽ�Ҳ�
  				String []rightnArry = right.split("\\|");  //�ָ����ʽ�Ҳ�
  				setSize = First.get(left).size();
  				for(int k = 0;k<rightnArry.length;++k) {   //���Ҳ��Ĳ���ʽһ��һ������
  					int rightIndex = 0;
  					Character cha = rightnArry[k].charAt(rightIndex);
  					if (Vt.indexOf(cha) != -1) { // ���ս����
  						// ����left��FIRST��
  						First.get(left).add(cha);
  						productionFirst.get(left+"->"+rightnArry[k]).add(cha);
  						if(First.get(left).size()>setSize)
  							bigger = true;
  					} else if (Vn.indexOf(cha) != -1) { // �Ƿ��ս����
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
  	/**����FOLLOW��
  	 * ����# ��FOLLOW(S)        SΪ�ķ���ʼ����
  	 * �ڶ�A�� ��B��,  �Ҧ� �� ��
  	  	�� FIRST(��) -{��}����FOLLOW(B)��
  	   �۷���, ֱ��ÿһ��FOLLOW(A)�������� 
  	           ��A�� ��B��A�� ��B��(�Ҧ� �� FIRST(��)) ��FOLLOW(A)�е�ȫ��Ԫ�ؼ���FOLLOW(B)
  	 * */
  	private void buildFollow() {
  		//#����FOLLOW(E)
  		Follow.get(start).add(end);
  		boolean bigger = true;
  		while(bigger) {
  			bigger=false;
  			int setSize = 0;
  			for(int i = 0;i < Vn.size();++i) {
  				Character left = Vn.get(i); //����ʽ�󲿷���
  				String right = grammar.get(left); //����ʽ�Ҳ�
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

  }
