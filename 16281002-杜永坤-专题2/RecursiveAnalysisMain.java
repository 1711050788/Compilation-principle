package grammaticalanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/**递归下降语法分析的入口类
 * 实验目标：
 * 		完成以下描述算术表达式的LL(1)文法的递归下降分析程序
	G[P]: 
        P→S|Q|; //S表示赋值表达式的开始符号，Q表示if语句的开始符号
		S→V=E;
		E→TR
		R→ATR|$
		T→FY
		Y→MFY|$
		F→CZ
		Z→OCZ|$
		C→BI
		I→XBI|$
		B→(E)|i
		A→+|-
		M→*|/
		X→a|o           //a表示逻辑符号&&，o表示逻辑符号||
		O→t|d|g|l|u|e  //t表示>=，d表示<=，g表示>,l表示<,e表示==，u表示!=
		V→i
		
		//if语句开始符号为Q
		Q→8JKH          //8表示if在符号表中序号
		H→fJKH|9K|$     //f 表示 else if符号的组合、9表示else在符号表中的序号
		J→(E)           //逻辑语句
		K→S|{U}|;       //if语句程序体
		U→PU|{U}U|$


 * 实验说明：
 * 		终结符号i为用户定义的简单变量，即标识符的定义。
 * 实验要求：
 * 		（1）输入串应是词法分析的输出二元式序列，即某算术表达式“专题1”的输出结果，输出为输入串是否为该文法定义的算术表达式的判断结果；
		（2）递归下降分析程序应能发现简单的语法错误；
		（3）设计两个测试用例（尽可能完备，正确和出错），并给出测试结果；

 * */
public class RecursiveAnalysisMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//1：构造递归下降语法分析类
		RecursiveDescentParsing rdp = new RecursiveDescentParsing("二元式文件.tys");
		//2：执行算法
		boolean k = rdp.grammaticalAnalysis();
		if(k==true)
			System.out.println("\n满足该文法");
		else 
			System.out.println("\n不满足该文法");
	}

}


/**识别文法
 *          


/**
 *	产生式			FIRST						
 *  P->S			[i]
 *  P->Q			[8]
 *  P->;			[;]
	S->V=E;			[i]
	V->i			[i]
	E->TR			[(, i]
	R->ATR			[+, -]
	R->$			[$]
	T->FY			[(, i]
	Y->MFY			[*, /]
	Y->$			[$]
	F->CZ			[(, i]
	Z->OCZ			[t, d, u, e, g, l]
	Z->$			[$]
	C->BI			[(, i]
	I->XBI			[a, o]
	I->$			[$]
	B->(E)			[(]
	B->i			[i]
	A->-			[-]
	A->+			[+]
	M->*			[*]
	M->/			[/]
	X->a			[a]
	X->o			[o]
	O->l			[l]
	O->e			[e]
	O->g			[g]
	O->t			[t]
	O->u			[u]
	O->d			[d]
	
	Q->8JKH			[8]
	H->fJKH			[f]
	H->9K			[9]
	H->$			[$]
	J->(E)			[(]
	K->{U}			[{]
	K->;			[;]
	K->S			[i]
	U->PU			[8, i, ;]
	U->$			[$]
	U->{U}U			[{]
--------非终结符号的FOLLOW集---------
	P 			# 8 i ; { } 
	Q 			# 8 i ; { } 
	J 			i { ; 
	K 			# f 8 9 i ; { } 
	H 			# 8 i ; { } 
	S 			# f 8 9 i ; { } 
	V 			= 
	E 			) ; 
	R 			) ; 
	T 			) + ; - 
	Y 			) + ; - 
	F 			) * + ; - / 
	A 			( i 
	M 			( i 
	Z 			) * + ; - / 
	C 			t d u e g ) * + ; l - / 
	I 			t d u e g ) * + ; l - / 
	B 			a d e g ) * + l - o / t u ; 
	X 			( i 
	O 			( i 
	U 			}

 * */
//递归下降分析类
class RecursiveDescentParsing {
	private BufferedReader br = null; //输入的二元式文件流
	private static List<String> InputStream = new ArrayList<String>(); //从二元式文件中拆解的符号穿输入流
	private int indexP = 0; //扫描指针，初始为0
	private int tab = 1; //制符表的数量，初始为1
	//构造函数
	public RecursiveDescentParsing(String fileName) {
		File fp = new File(fileName);
		if(!fp.getName().endsWith(".tys")) {
			System.out.println("文件格式不正确...");
			return;
		}
		//构造文件扫描
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fp.getName())));
			String erYuanShi = "";
			while((erYuanShi=br.readLine())!=null) {
				//截取符号串
				InputStream.add(erYuanShi.substring(erYuanShi.indexOf(",") + 1, erYuanShi.lastIndexOf(")")));
			}
			InputStream.add("#");  //末尾添加#号
			//输出一下序列
			
			System.out.println("二元式文件为："+fileName+"，其源程序为：");
			System.out.print("  ");
			for(int i = 0;i<InputStream.size()-1;++i) 
			{
				print_indexp(i);
			}
			System.out.println("\n\n\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(fileName+"文件不存在...");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//语法分析入口函数
	public boolean grammaticalAnalysis() {
		System.out.println("-------------分析过程-------------------");
		System.out.print("    ");
		return P();
	}
	/**
	 *  P->S			[i]
     *  P->Q			[8]
     *  P->;			[;]
     *  P不能推出空，不需要判断FOLLOW集
	 * @return
	 */
	private boolean P() { 
		System.out.print("P→");
		String s =get_indexP( indexP);
		//System.out.println(s);
		
		//System.out.println(sequals);
		if(s.equals("i")) {
			
			if(!S())
				return false;
		}else if (s.equals("8"))
		{
			if(!Q())
				return false;
		}
		else if (s.equals(";"))
		{
				print_indexp(indexP);indexP++;
				return true;
		}
		else 
			return false;
		return true;
	}
	/**
	 * Q->8JKH			[8]
	 * Q不能推出空，不需要判断FOLLOW集
	 * @return
	 */
	private boolean Q() {
		System.out.print("Q→");
		String s =get_indexP( indexP);
		//System.out.println(s);

		//System.out.println(sequals);
		if(s.equals("8")) {
			print_indexp(indexP);indexP++;
			if(!J())
				return false;
			if(!K())
				return false;
			if(!H())
				return false;
		}else
		{
			System.out.println("\nif语句非法开始");
			return false;
		}
			
		return true;
	}
	/**
	 *  H->fJKH			[f]
		H->9K			[9]
		H->$			[$]
		H 能推出空，需要判断FOLLOW集
		H 			# 8 i ; { } 
	 * @return
	 */
	private boolean H() {
		System.out.print("H→");
		String s =get_indexP( indexP);
		//System.out.println(s);

		//System.out.println(sequals);
		if(s.equals("f")) {
			print_indexp(indexP);indexP++;
			if(!J())
				return false;
			if(!K())
				return false;
			if(!H())
				return false;
		}else if(s.equals("9"))
		{
			print_indexp(indexP);indexP++;
			if(!K())
				return false;
		}else if(s.equals("i")||s.equals("#")||s.equals("8")||s.equals("{")||s.equals(";")||s.equals("}")) 
		{
			return true;
		}else
		{
			System.out.println("\n非法结构" );
			return false;
		}
			
		return true;
	}
	/**
	 *  K->{U}			[{]
		K->;			[;]
		K->S			[i]
		K不能推出空，不需要判断FOLLOW集
	 * @return
	 */
	private boolean K() {
		System.out.print("K→");
		String s =get_indexP( indexP);
		//System.out.println(s);

		//System.out.println(sequals);
		if(s.equals("i")) {
			if(!S())
				return false;
		}else if(s.equals("{"))
		{
			print_indexp(indexP);indexP++;
			if(!U())
			{
				 s =get_indexP( indexP);
				 if(s.equals("}")) {
						print_indexp(indexP);indexP++;
						return false;
					}else
					{
						System.out.println("\n缺少 } ");
						return false;
					}
			}
			else
			{
				 s =get_indexP( indexP);
				 if(s.equals("}")) {
						print_indexp(indexP);indexP++;
						return true;
					}else
					{
						System.out.println("\n缺少 } ");
						return false;
					}
			}
			
			
					
		}else if(s.equals(";")) 
		{
			print_indexp(indexP);indexP++;
			return true;
		}else
		{
			System.out.println("\nif语句程序体非法，缺少if语句程序体");
			return false;
		}
			
		return true;
	}
	/**
	 * 	U->PU			[8, i, ;]
		U->$			[$]
		U->{U}U			[{]
		U能推出空，需要判断FOLLOW集
		U 			}
	 * @return
	 */
	private boolean U() {
		System.out.print("U→");
		String s =get_indexP( indexP);
		//System.out.println(s);

		//System.out.println(sequals);
		if(s.equals("i")||s.equals("8")||s.equals(";")) {
			if(!P())
				return false;
			if(!U())
				return false;
		}else if(s.equals("{"))
		{
			print_indexp(indexP);indexP++;
			if(!U())
			{
				 s =get_indexP( indexP);
				 if(s.equals("}")) {
						print_indexp(indexP);indexP++;
						return false;
					}else
					{
						System.out.println("\n缺少 } ");
						return false;
					}
			}
			else
			{
				 s =get_indexP( indexP);
				 if(s.equals("}")) {
						print_indexp(indexP);indexP++;
						return true;
					}else
					{
						System.out.println("\n缺少 } ");
						return false;
					}
			}
					
		}else if(s.equals("}")) 
		{
			return true;
		}else
		{
			System.out.println("\n缺少 }");
			return false;
		}
			
		return true;
	}
	/**
	 * J->(E)			[(]
	 * @return
	 */
	private boolean J() {
		System.out.print("J→");
		
		//System.out.println(s);

		//System.out.println(sequals);
		String s =get_indexP( indexP);
		if(s.equals("(")){
			print_indexp(indexP);indexP++;
			if(!E())
			{	
				s = get_indexP( indexP);
				if(s.equals(")")) {
					print_indexp(indexP);indexP++;
					return false;
				}else
				{
					System.out.println("\n判断语句非法结束，缺少 )  ");
					return false;
				}
			}
			else
			{
				s = get_indexP( indexP);
				if(s.equals(")")) {
					print_indexp(indexP);indexP++;
					return true;
				}else
				{
					System.out.println("\n判断语句非法结束，缺少 )  ");
					return false;
				}
			}
				
		}else {
			System.out.println("\n判断语句非法结构，缺少 ( ");
			return false;
		}
		//return true;
	}
	
	/**
	 * S->V=E;			[i]
	 * @return
	 */
	private boolean S() { //S→V=E;:    i    #
		System.out.print("S→");
		String s =get_indexP( indexP);
		print_indexp(indexP);
		String sequals = get_indexP( ++indexP);
		//System.out.println(sequals);
		if(s.equals("i")) 
		{
			if(sequals.equals("="))
			{
				print_indexp(indexP);indexP++;
				if(!E())
				{
					s =get_indexP( indexP);
					 if (s.equals(";"))
					{
						 print_indexp(indexP);indexP++;
						return false;
					}
					 else
					 {
						 System.out.println("\n缺少  ;  ");
							return false;
					 }
				}
				else
				{
					s =get_indexP( indexP);
					 if (s.equals(";"))
					{
						 print_indexp(indexP);indexP++;
						return true;
					}
					 else
					 {
						 System.out.println("\n缺少  ;  ");
							return false;
					 }
				}

			}
			else {
				System.out.println("\n缺少 = ");
				return false;
			}
			
		}else
		{
			System.out.println("\n非法格式");
			return false;
		}
			
		//return true;
	}
	
	/**
	 * E->TR			[(, i]
	 * @return
	 */
	private boolean E() { //E→TR: (,i    ),#
		System.out.print("E→");
		String s =get_indexP( indexP);
		
		if(s.equals("(")||s.equals("i")) {
			if(!T())
				return false;
			if(!R())
				return false;
		}else
		{
			System.out.println("算术表达式格式错误");
			return false;
		}
			
		return true;
	}
	/**
	 *  R->ATR			[+, -]
		R->$			[$]
		
		R 			) ; 
	 * @return
	 */
	private boolean R() { //R→ATR|ε:+,－	        ),;
		String s =get_indexP( indexP);
		if(s.equals("+")||s.equals("-")) {
			if(!A())
				return false;
			if(!T())
				return false;
			if(!R())
				return false;
		}else if(s.equals(")")) {
			return true;
		}else if(s.equals(";")) {
			
			return true;
		}
		else
		{
			System.out.println("\n算术表达式格式错误");
			return false;
		}
		
		return true;
	}
	
	/**
	 * T->FY			[(, i]
	 * @return
	 */
	private boolean T() { //T→FY:(,i    
		System.out.print("T→");
		String s =get_indexP( indexP);
		if(s.equals("i")||s.equals("(")) {
			if(!F())
				return false;
			if(!Y())
				return false;
		}else
		{
			System.out.println("\n非法格式，操作符之间应有变量");
			return false;
		}
			
		return true;
	}
	
	/**
	 * Y→MFY|ε: *,/  
	 *  +,－,),;
	 * @return
	 */
	private boolean Y() { //Y→MFY|ε: *,/   +,－,),;
		System.out.print("Y→");
		String s =get_indexP( indexP);
		if(s.equals("*")||s.equals("/")) {
			if(!M())
				return false;
			if(!F())
				return false;
			if(!Y())
				return false;
		}else if(s.equals("+")||s.equals("-")||s.equals(")")||s.equals(";")) {
			return true;
		}else
		{
			System.out.println("\n非法结构");
			return false;
		}
			
		return true;
	}
    /**
     * //F->CZ			[(, i]    
     *  ) * + ; - /        
     * @return
     */
	private boolean F() { //F->CZ			[(, i]     ) * + ; - /      
		System.out.print("F→");
		String s =get_indexP( indexP);
		if(s.equals("i")||s.equals("(")) {
			if(!C())
				return false;
			if(!Z())
				return false;
		}else
		{
			System.out.println("\n非法格式，操作符之后应有变量");
			return false;
		}
			
		return true;
	}
	/**
	 * C->BI			[(, i]
	 * @return
	 */
	private boolean C() { //C->BI			[(, i]   
		System.out.print("C→");
		String s =  get_indexP( indexP);
		if(s.equals("i")||s.equals("(")) {
			if(!B())
				return false;
			if(!I())
				return false;
		}else
		{
			System.out.println("\n非法格式，操作符之后应有变量");
			return false;
		}
			
		return true;
	}
	/**
	 * Z->OCZ|ε:	[t, d, u, e, g, l]     
	 *   ) * + ; - /
	 * @return
	 */
	private boolean Z() { //Z->OCZ|ε:	[t, d, u, e, g, l]       ) * + ; - /  
		System.out.print("Z→");
		String s = get_indexP( indexP);
		if(s.equals("t")||s.equals("d")||s.equals("u")||s.equals("e")||s.equals("g")||s.equals("l")) {
			if(!O())
				return false;
			if(!C())
				return false;
			if(!Z())
				return false;
		}else if(s.equals("+")||s.equals("-")||s.equals(")")||s.equals(";")||s.equals("*")||s.equals("/")) {
			return true;
		}else 
		{
			System.out.println("非法格式 缺少 ;");
			return false;
		}
			
		return true;
	}
	/**
	 * I->XBI|ε:	[a, o]		
	 * t d u e g ) * + ; l - /
	 * @return
	 */
	private boolean I() { //I->XBI|ε:	[a, o]		t d u e g ) * + ; l - /  
		System.out.print("I→");
		String s =get_indexP( indexP);
		if(s.equals("a")||s.equals("o")) {
			if(!X())
				return false;
			if(!B())
				return false;
			if(!I())
				return false;
		}else if(s.equals("+")||s.equals("-")||s.equals(")")||s.equals(";")
				||s.equals("*")||s.equals("/")||s.equals("g")||s.equals("l")
				||s.equals("t")||s.equals("d")||s.equals("u")||s.equals("e")) {
			return true;
		}else
		{
			System.out.println("非法格式");
			return false;
		}
		return true;
	}
	/**
	 * B→ (E)|i:(,i
	 * @return
	 */
	private boolean B() { //	  
		System.out.print("B→");
		String s =get_indexP( indexP);
		if(s.equals("(")){
			print_indexp(indexP);indexP++;
			if(!E())
			{
				s =get_indexP( indexP);
				if(s.equals(")")) {
					print_indexp(indexP);indexP++;
					return false;
				}else
				{
					System.out.println("缺少 ) ");
					return false;
				}
			}
				
			else
			{
				s =get_indexP( indexP);
				if(s.equals(")")) {
					print_indexp(indexP);indexP++;
					return true;
				}else
				{
					System.out.println("缺少 ) ");
					return false;
				}
			}
			
				
		}else if(s.equals("i")) {
			print_indexp(indexP);indexP++;
			return true;
		}else {
			System.out.println("\n非法格式");
			return false;
		}
			
	}
	private boolean A() { //A→+|-
		System.out.print("A→");
		String s =get_indexP( indexP);
		if(s.equals("+")||s.equals("-")){
			print_indexp(indexP);indexP++;
			return true;
		}
		else
		{
			System.out.println("缺少 + 或  -  操作符 ");
			return false;
		}
		
	}
	private boolean X() { //X→a|o
		System.out.print("X→");
		String s =get_indexP( indexP);
		if(s.equals("a")||s.equals("o")){
			print_indexp(indexP);indexP++;
			return true;
		}
		else
		{
			System.out.println("缺少 && 或  ||  逻辑操作符 ");
			return false;
		}
	}
	private boolean M() { //M→*|/
		System.out.print("M→");
		String s =get_indexP( indexP);
		if(s.equals("*")||s.equals("/")) {
			print_indexp(indexP);indexP++;
			return true;
		}
		else
		{
			System.out.println("缺少 * 或  / 操作符 ");
			return false;
		}
		
	}
	private boolean O() { //O→t|d|g|l|u|e
		System.out.print("O→");
		String s =get_indexP( indexP);
		if(s.equals("t")||s.equals("d")||s.equals("g")||s.equals("l")||s.equals("u")||s.equals("e")) {
			print_indexp(indexP);indexP++;
			return true;
		}
		else
		{
			System.out.println("缺少 关系操作符 ");
			return false;
		}
		
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
				print_indexp(indexP);indexP++;
			}
			else
			{
				s="9";
			}
		}	
		else if(s.equals("if"))
		{
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
					System.out.println("未知符号："+s);
					s="unknow";
				}
			
			}
		}
		return  s;
	}
}

