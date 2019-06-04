package grammaticalanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/**�ݹ��½��﷨�����������
 * ʵ��Ŀ�꣺
 * 		������������������ʽ��LL(1)�ķ��ĵݹ��½���������
	G[P]: 
        P��S|Q|; //S��ʾ��ֵ���ʽ�Ŀ�ʼ���ţ�Q��ʾif���Ŀ�ʼ����
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
		
		//if��俪ʼ����ΪQ
		Q��8JKH          //8��ʾif�ڷ��ű������
		H��fJKH|9K|$     //f ��ʾ else if���ŵ���ϡ�9��ʾelse�ڷ��ű��е����
		J��(E)           //�߼����
		K��S|{U}|;       //if��������
		U��PU|{U}U|$


 * ʵ��˵����
 * 		�ս����iΪ�û�����ļ򵥱���������ʶ���Ķ��塣
 * ʵ��Ҫ��
 * 		��1�����봮Ӧ�Ǵʷ������������Ԫʽ���У���ĳ�������ʽ��ר��1���������������Ϊ���봮�Ƿ�Ϊ���ķ�������������ʽ���жϽ����
		��2���ݹ��½���������Ӧ�ܷ��ּ򵥵��﷨����
		��3��������������������������걸����ȷ�ͳ��������������Խ����

 * */
public class RecursiveAnalysisMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//1������ݹ��½��﷨������
		RecursiveDescentParsing rdp = new RecursiveDescentParsing("��Ԫʽ�ļ�.tys");
		//2��ִ���㷨
		boolean k = rdp.grammaticalAnalysis();
		if(k==true)
			System.out.println("\n������ķ�");
		else 
			System.out.println("\n��������ķ�");
	}

}


/**ʶ���ķ�
 *          


/**
 *	����ʽ			FIRST						
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
--------���ս���ŵ�FOLLOW��---------
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
//�ݹ��½�������
class RecursiveDescentParsing {
	private BufferedReader br = null; //����Ķ�Ԫʽ�ļ���
	private static List<String> InputStream = new ArrayList<String>(); //�Ӷ�Ԫʽ�ļ��в��ķ��Ŵ�������
	private int indexP = 0; //ɨ��ָ�룬��ʼΪ0
	private int tab = 1; //�Ʒ������������ʼΪ1
	//���캯��
	public RecursiveDescentParsing(String fileName) {
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
			
			System.out.println("��Ԫʽ�ļ�Ϊ��"+fileName+"����Դ����Ϊ��");
			System.out.print("  ");
			for(int i = 0;i<InputStream.size()-1;++i) 
			{
				print_indexp(i);
			}
			System.out.println("\n\n\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(fileName+"�ļ�������...");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//�﷨������ں���
	public boolean grammaticalAnalysis() {
		System.out.println("-------------��������-------------------");
		System.out.print("    ");
		return P();
	}
	/**
	 *  P->S			[i]
     *  P->Q			[8]
     *  P->;			[;]
     *  P�����Ƴ��գ�����Ҫ�ж�FOLLOW��
	 * @return
	 */
	private boolean P() { 
		System.out.print("P��");
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
	 * Q�����Ƴ��գ�����Ҫ�ж�FOLLOW��
	 * @return
	 */
	private boolean Q() {
		System.out.print("Q��");
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
			System.out.println("\nif���Ƿ���ʼ");
			return false;
		}
			
		return true;
	}
	/**
	 *  H->fJKH			[f]
		H->9K			[9]
		H->$			[$]
		H ���Ƴ��գ���Ҫ�ж�FOLLOW��
		H 			# 8 i ; { } 
	 * @return
	 */
	private boolean H() {
		System.out.print("H��");
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
			System.out.println("\n�Ƿ��ṹ" );
			return false;
		}
			
		return true;
	}
	/**
	 *  K->{U}			[{]
		K->;			[;]
		K->S			[i]
		K�����Ƴ��գ�����Ҫ�ж�FOLLOW��
	 * @return
	 */
	private boolean K() {
		System.out.print("K��");
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
						System.out.println("\nȱ�� } ");
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
						System.out.println("\nȱ�� } ");
						return false;
					}
			}
			
			
					
		}else if(s.equals(";")) 
		{
			print_indexp(indexP);indexP++;
			return true;
		}else
		{
			System.out.println("\nif��������Ƿ���ȱ��if��������");
			return false;
		}
			
		return true;
	}
	/**
	 * 	U->PU			[8, i, ;]
		U->$			[$]
		U->{U}U			[{]
		U���Ƴ��գ���Ҫ�ж�FOLLOW��
		U 			}
	 * @return
	 */
	private boolean U() {
		System.out.print("U��");
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
						System.out.println("\nȱ�� } ");
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
						System.out.println("\nȱ�� } ");
						return false;
					}
			}
					
		}else if(s.equals("}")) 
		{
			return true;
		}else
		{
			System.out.println("\nȱ�� }");
			return false;
		}
			
		return true;
	}
	/**
	 * J->(E)			[(]
	 * @return
	 */
	private boolean J() {
		System.out.print("J��");
		
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
					System.out.println("\n�ж����Ƿ�������ȱ�� )  ");
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
					System.out.println("\n�ж����Ƿ�������ȱ�� )  ");
					return false;
				}
			}
				
		}else {
			System.out.println("\n�ж����Ƿ��ṹ��ȱ�� ( ");
			return false;
		}
		//return true;
	}
	
	/**
	 * S->V=E;			[i]
	 * @return
	 */
	private boolean S() { //S��V=E;:    i    #
		System.out.print("S��");
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
						 System.out.println("\nȱ��  ;  ");
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
						 System.out.println("\nȱ��  ;  ");
							return false;
					 }
				}

			}
			else {
				System.out.println("\nȱ�� = ");
				return false;
			}
			
		}else
		{
			System.out.println("\n�Ƿ���ʽ");
			return false;
		}
			
		//return true;
	}
	
	/**
	 * E->TR			[(, i]
	 * @return
	 */
	private boolean E() { //E��TR: (,i    ),#
		System.out.print("E��");
		String s =get_indexP( indexP);
		
		if(s.equals("(")||s.equals("i")) {
			if(!T())
				return false;
			if(!R())
				return false;
		}else
		{
			System.out.println("�������ʽ��ʽ����");
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
	private boolean R() { //R��ATR|��:+,��	        ),;
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
			System.out.println("\n�������ʽ��ʽ����");
			return false;
		}
		
		return true;
	}
	
	/**
	 * T->FY			[(, i]
	 * @return
	 */
	private boolean T() { //T��FY:(,i    
		System.out.print("T��");
		String s =get_indexP( indexP);
		if(s.equals("i")||s.equals("(")) {
			if(!F())
				return false;
			if(!Y())
				return false;
		}else
		{
			System.out.println("\n�Ƿ���ʽ��������֮��Ӧ�б���");
			return false;
		}
			
		return true;
	}
	
	/**
	 * Y��MFY|��: *,/  
	 *  +,��,),;
	 * @return
	 */
	private boolean Y() { //Y��MFY|��: *,/   +,��,),;
		System.out.print("Y��");
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
			System.out.println("\n�Ƿ��ṹ");
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
		System.out.print("F��");
		String s =get_indexP( indexP);
		if(s.equals("i")||s.equals("(")) {
			if(!C())
				return false;
			if(!Z())
				return false;
		}else
		{
			System.out.println("\n�Ƿ���ʽ��������֮��Ӧ�б���");
			return false;
		}
			
		return true;
	}
	/**
	 * C->BI			[(, i]
	 * @return
	 */
	private boolean C() { //C->BI			[(, i]   
		System.out.print("C��");
		String s =  get_indexP( indexP);
		if(s.equals("i")||s.equals("(")) {
			if(!B())
				return false;
			if(!I())
				return false;
		}else
		{
			System.out.println("\n�Ƿ���ʽ��������֮��Ӧ�б���");
			return false;
		}
			
		return true;
	}
	/**
	 * Z->OCZ|��:	[t, d, u, e, g, l]     
	 *   ) * + ; - /
	 * @return
	 */
	private boolean Z() { //Z->OCZ|��:	[t, d, u, e, g, l]       ) * + ; - /  
		System.out.print("Z��");
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
			System.out.println("�Ƿ���ʽ ȱ�� ;");
			return false;
		}
			
		return true;
	}
	/**
	 * I->XBI|��:	[a, o]		
	 * t d u e g ) * + ; l - /
	 * @return
	 */
	private boolean I() { //I->XBI|��:	[a, o]		t d u e g ) * + ; l - /  
		System.out.print("I��");
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
			System.out.println("�Ƿ���ʽ");
			return false;
		}
		return true;
	}
	/**
	 * B�� (E)|i:(,i
	 * @return
	 */
	private boolean B() { //	  
		System.out.print("B��");
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
					System.out.println("ȱ�� ) ");
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
					System.out.println("ȱ�� ) ");
					return false;
				}
			}
			
				
		}else if(s.equals("i")) {
			print_indexp(indexP);indexP++;
			return true;
		}else {
			System.out.println("\n�Ƿ���ʽ");
			return false;
		}
			
	}
	private boolean A() { //A��+|-
		System.out.print("A��");
		String s =get_indexP( indexP);
		if(s.equals("+")||s.equals("-")){
			print_indexp(indexP);indexP++;
			return true;
		}
		else
		{
			System.out.println("ȱ�� + ��  -  ������ ");
			return false;
		}
		
	}
	private boolean X() { //X��a|o
		System.out.print("X��");
		String s =get_indexP( indexP);
		if(s.equals("a")||s.equals("o")){
			print_indexp(indexP);indexP++;
			return true;
		}
		else
		{
			System.out.println("ȱ�� && ��  ||  �߼������� ");
			return false;
		}
	}
	private boolean M() { //M��*|/
		System.out.print("M��");
		String s =get_indexP( indexP);
		if(s.equals("*")||s.equals("/")) {
			print_indexp(indexP);indexP++;
			return true;
		}
		else
		{
			System.out.println("ȱ�� * ��  / ������ ");
			return false;
		}
		
	}
	private boolean O() { //O��t|d|g|l|u|e
		System.out.print("O��");
		String s =get_indexP( indexP);
		if(s.equals("t")||s.equals("d")||s.equals("g")||s.equals("l")||s.equals("u")||s.equals("e")) {
			print_indexp(indexP);indexP++;
			return true;
		}
		else
		{
			System.out.println("ȱ�� ��ϵ������ ");
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
					System.out.println("δ֪���ţ�"+s);
					s="unknow";
				}
			
			}
		}
		return  s;
	}
}

