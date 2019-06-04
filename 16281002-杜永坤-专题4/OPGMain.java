package opganalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OPGMain {
	private int  N =100;
	
	//�ķ�
	private Map<String, String> grammar = new HashMap<String, String>();
	
	//�ս���ż�
	private List<Character> Vt = new ArrayList<Character>();
	
	//���ս���ż�
	private List<Character> Vn = new ArrayList<Character>();
	
	//FIRSTVT��
	private Map<Character,Set<Character>> FIRSTVT = new HashMap<Character,Set<Character>>();
	
	//LASTVT��
	private Map<Character,Set<Character>> LASTVT = new HashMap<Character,Set<Character>>();
	
	//�������Ȩ��ϵ��
	/**
	 * ��OPGtable�У���-1��0,1,2��ʾ����Ȩ��ϵ
	 * 0 ��ʾ���ȹ�ϵ����
	 * 1 ��ʾ���ȹ�ϵС��
	 * 2 ��ʾ���ȹ�ϵ����
	 * -1 ��ʾ����������Ȩ��ϵ
	 * *
	 */
	private int [][]OPGtable = new int [N][N];
	
	private int   FIRSTVTAllNochange = 1;
	
	private BufferedReader br = null; //����Ķ�Ԫʽ�ļ���
	
	
	public OPGMain(){
		//������������ķ�
		grammar.put("S", "#E#");
		grammar.put("E", "E+T|E-T|T");
		grammar.put("T", "T*F|T/F|F");
		grammar.put("F", "(E)|i");
		//grammar.put("S", "#E#");
//		grammar.put("E", "E+T|T");
//		grammar.put("T", "T*F|F");
//		grammar.put("F", "P��F|P");
//		grammar.put("P", "(E)|i");
		
		//���÷��ս����
		Vn.add('S');
		Vn.add('E');
		Vn.add('T');
		Vn.add('F');
	//	Vn.add('P');
		
		//�����ս����
		Vt.add('#');
		Vt.add('+');
		Vt.add('-');
		Vt.add('*');
		Vt.add('/');
	//	Vt.add('��');
		Vt.add('(');
		Vt.add(')');
		Vt.add('i');
		
	/*	Vt.add('(');
		Vt.add('i');
		Vt.add('*');
		Vt.add('+');
		Vt.add(')');
		Vt.add('#');
		*/
		
		//��ʼ��������ȹ�ϵ����
		for(int l=0;l<Vt.size();l++)
		{
			for(int r =0 ;r<Vt.size();r++)
			{
				OPGtable[l][r]=-1;
			}
		}
		
		System.out.println("�ķ����£�");
		
		grammar.forEach((k,v)->{
		    System.out.println(k + "��" + v);
		    if(k.equals("E")){
		        System.out.println("Hello E");
		    }
		});
		//ΪFIRSTVT��LASTVT����ռ�
		for(int i = 0;i<Vn.size();++i) {
			FIRSTVT.put(Vn.get(i), new HashSet<Character>());
			LASTVT.put(Vn.get(i), new HashSet<Character>());
		}
		Create_FIRSTVT();
		Create_LASTVT();
		Create_OPGTable();
	}
	//�������ս���ŵ�FIRSTVT��
	public void Create_FIRSTVT() {
		System.out.println("Create_FIRSTVT");
		//�����ķ������з��ս���ŵ�FIRSTVT�ĳ�ʼ��
		grammar.forEach((k,v)->{
			String []nArryStr = v.split("\\|");
			for(int i=0 ;i<nArryStr.length;i++)
				
			{
				//System.out.println(nArryStr[i]);
				if(Vt.contains(nArryStr[i].charAt(0)))
				{
					char b= nArryStr[i].charAt(0);
					FIRSTVT.get(k.charAt(0)).add(b);
				}
				if(nArryStr[i].length()>=2)
				if(Vn.contains(nArryStr[i].charAt(0))&&Vt.contains(nArryStr[i].charAt(1)))
				{
					char b=nArryStr[i].charAt(1);
					FIRSTVT.get(k.charAt(0)).add(b);
				}
			}
			
			
		});
		
		do 
		{
			FIRSTVTAllNochange=1;
			grammar.forEach((k,v)->{
				String []nArryStr = v.split("\\|");
				for(int i=0 ;i<nArryStr.length;i++)
					
				{
					char U=k.charAt(0);
					char V=nArryStr[i].charAt(0);
					if(Vn.contains(U)&&U!=V&&Vn.contains(V))
					{
						
							FIRSTVT.get(V).forEach(values->{
								if(!FIRSTVT.get(U).contains(values))
								{
								//	System.out.println(values);
								 FIRSTVT.get(U).add(values);
								 FIRSTVTAllNochange = 0;
								}
							
							});
							
						
					}
					
				}
				
				
			});
			if(FIRSTVTAllNochange==1)
				break;
		}while(true);
		
		//��ʾFIRSTVT��
		FIRSTVT.forEach((k,v)->{
		    System.out.println(k + ":" + v);
		   
		});
		
	}
	public void Create_LASTVT() {
		System.out.println("Create_LASTVT");
		
		//�����ķ������з��ս���ŵ�LASTVT�ĳ�ʼ��
		grammar.forEach((k,v)->{
			String []nArryStr = v.split("\\|");
			for(int i=0 ;i<nArryStr.length;i++)
				
			{
				//System.out.println(nArryStr[i]);
				int len= nArryStr[i].length();
				if(Vt.contains(nArryStr[i].charAt(len-1)))
				{
					char b= nArryStr[i].charAt(len-1);
					LASTVT.get(k.charAt(0)).add(b);
				}
				if(nArryStr[i].length()>=2)
				if(Vt.contains(nArryStr[i].charAt(len-2))&&Vn.contains(nArryStr[i].charAt(len-1)))
				{
					char b=nArryStr[i].charAt(len-2);
					LASTVT.get(k.charAt(0)).add(b);
				}
			}
			
			
		});
		
		do 
		{
			FIRSTVTAllNochange=1;
			grammar.forEach((k,v)->{
				String []nArryStr = v.split("\\|");
				for(int i=0 ;i<nArryStr.length;i++)
					
				{
					int len =nArryStr[i].length();
					char U=k.charAt(0);
					char V=nArryStr[i].charAt(len-1);
					if(Vn.contains(U)&&U!=V&&Vn.contains(V))
					{
						
						LASTVT.get(V).forEach(values->{
								if(!LASTVT.get(U).contains(values))
								{
									//System.out.println(values);
									LASTVT.get(U).add(values);
								 FIRSTVTAllNochange = 0;
								}
							
							});
							
						
					}
					
				}
				
				
			});
			if(FIRSTVTAllNochange==1)
				break;
		}while(true);
		
		
		
		LASTVT.forEach((k,v)->{
		    System.out.println(k + ":" + v);
		   
		});
	}
	
	public void Create_OPGTable() {
		System.out.println("Create_OPGTable");
		
		/**
		 * ��OPGtable�У���-1��0,1,2��ʾ����Ȩ��ϵ
		 * 0 ��ʾ���ȹ�ϵ����
		 * 1 ��ʾ���ȹ�ϵС��
		 * 2 ��ʾ���ȹ�ϵ����
		 * -1 ��ʾ����������Ȩ��ϵ
		 * *
		 */
		grammar.forEach((k,v)->{
			String []nArryStr = v.split("\\|");
			
			for(int i=0 ;i<nArryStr.length;i++)
				
			{
				
				String ruleRight=nArryStr[i];
				int  len = ruleRight.length();
				if(len>=2)
				{
					for(int i1= 0 ;i1<len-1 ;i1++)
					{
						char X1=ruleRight.charAt(i1);
						char X2=ruleRight.charAt(i1+1);
						
						if(Vt.contains(X1)&&Vt.contains(X2))				
							OPGtable[Vt.indexOf(X1)][Vt.indexOf(X2)]=0;//0 ��ʾ���ȹ�ϵ����
						if(Vt.contains(X1)&&Vn.contains(X2))
						{
							FIRSTVT.get(X2).forEach(values->{
								OPGtable[Vt.indexOf(X1)][Vt.indexOf(values)]=1;//1 ��ʾ���ȹ�ϵС��
							});
						}
						if(Vn.contains(X1)&&Vt.contains(X2))					
						{
							LASTVT.get(X1).forEach(values->{
			
								OPGtable[Vt.indexOf(values)][Vt.indexOf(X2)]=2;//2 ��ʾ���ȹ�ϵ����
							});
						}
						if(len>=3&&i1<len-2)
						{
							char X3=ruleRight.charAt(i1+2);
							if(Vt.contains(X1)&&Vn.contains(X2)&&Vt.contains(X3))
							{
								
								OPGtable[Vt.indexOf(X1)][Vt.indexOf(X3)]=0; //0 ��ʾ���ȹ�ϵ����
							}
						}
							
							
					}
					
				}
				
			}
			
			
		});
		
		//���OPGtable
		System.out.print("\t");
		for(int r=0;r<Vt.size();r++)
		{
			System.out.print("\t"+Vt.get(r));
		}
		System.out.print("\n");
		
		
		for(int l=0 ;l<Vt.size();l++)
		{
			System.out.print(Vt.get(l)+"\t");
			for(int r=0;r<Vt.size();r++)
			{
				
				if(OPGtable[l][r]==0)
					System.out.print("\t=��");
				if(OPGtable[l][r]==1)
					System.out.print("\t<��");
				if(OPGtable[l][r]==2)
					System.out.print("\t>��");
				if(OPGtable[l][r]==-1)
					System.out.print("\t");
			}
			System.out.print("\n");
		}
	}
	public void OPGanalysis(String fileName) {
		
		File fp = new File(fileName);
		if(!fp.getName().endsWith(".tys")) {
			System.out.println("�ļ���ʽ����ȷ...");
			return ;
		}
		//�����ļ�ɨ��
		String s="";//�����������ʽ
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fp.getName())));
			String erYuanShi = "";
			while((erYuanShi=br.readLine())!=null) {
				//��ȡ���Ŵ�
				String substr=erYuanShi.substring(erYuanShi.indexOf("(") + 1, erYuanShi.lastIndexOf(","));
				if(substr.equals("1")||substr.equals("2"))
				{		
					s+="i";
				}
				else 
				{
					s+=erYuanShi.substring(erYuanShi.indexOf(",") + 1, erYuanShi.lastIndexOf(")"));
				}

			}
			
			
			
			System.out.println("�������ʽ��"+s);
			s+="#";//���������ʽ�����#����
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(fileName+"�ļ�������...");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		char Q;
		int k=1,j;
		char s1[]=new char[50];//����ջ����
		s1[k]='#';

		Boolean orsuccess=true;
		
		int i=-1;
		
		System.out.println("------------------������ʼ��------------------");
		
		System.out.println("\t����ջ���£�");
		do {
			i++;
			if(Vt.contains(s1[k])) {
				j=k;
			} else {
				j=k-1;
			}
			
			while(get_PriorityRelationship(s1[j],s.charAt(i))==2) {
				do {
					 Q=s1[j];
					if(Vt.contains(s1[j-1])) {
						j=j-1;
					} else {
						j=j-2;
					}
				} while(get_PriorityRelationship(s1[j],Q)!=1);
				
				
				for(int i1=0;i1<=k;i1++)
				{
					System.out.print(s1[i1]+" ");
				}
				System.out.print("\t\t\t\t");
				
				for(int i1=i;i1<s.length();i1++)
				{
					System.out.print(s.charAt(i1)+" ");
				}
				System.out.print("\n");
				System.out.print("\t\t��Լ");
				for(int i1=j+1;i1<=k;i1++)
				{
					
					System.out.print(s1[i1]+" ");
				}
				System.out.print("\n");
				k=j+1;
				if(s1[k]=='('&&s1[k+1]==')')
				{
					System.out.println("( �� ) ֮��ȱ�ٱ��ʽ���߱���");
					orsuccess=false;
				}
			    s1[k]='N';
			    
			    
			    for(int i1=0;i1<=k;i1++)
				{
					System.out.print(s1[i1]+" ");
				}
			    	System.out.print("\t\t\t\t");
				
				for(int i1=i;i1<s.length();i1++)
				{
					System.out.print(s.charAt(i1)+" ");
				}
				System.out.print("\n");
			}
			int flag1=get_PriorityRelationship(s1[j],s.charAt(i));
			if(flag1==1||flag1==0) {
				k=k+1;
				s1[k]=s.charAt(i);
			} else {
				System.out.print("Error");
				orsuccess=false;
				
			}
			
		} while(s.charAt(i)!='#');
		
		if(orsuccess)
		{
			System.out.println("�����ɹ����������ʽ��ʽ��ȷ");
		}
		else
		{
			System.out.println("����ʧ�ܣ��������ʽ��ʽ����");
		}

	}
	
	public int get_PriorityRelationship(char Vt1,char Vt2)
	{
		if(Vt.contains(Vt1)&&Vt.contains(Vt2))
		{
			int PR=OPGtable[Vt.indexOf(Vt1)][Vt.indexOf(Vt2)];
			if( PR==-1)
			{
					//System.out.println(Vt1+"��"+Vt2+"֮�䲻�������ȹ�ϵ��");
					if(Vt1=='('&&Vt2=='#')
					{
						System.out.println("( " +"�Ҳ�ȱ�� ) ��֮ƥ��");
					}
					if(Vt1=='#'&&Vt2==')')
					{
						System.out.println(") " +"���ȱ�� ( ��֮ƥ��");
					}
					if(Vt1==')')
					{
						if(Vt2=='(')
						{
							System.out.println(")��( " +"֮��ȱ���������");
						}
						else if(Vt2=='i')
						{
							System.out.println(")����� " +"֮��ȱ���������");
						}
					}
					
					if(Vt1=='i')
					{
						if(Vt2=='(')
						{
							System.out.println("������( " +"֮��ȱ���������");
						}
						else if(Vt2=='i')
						{
							System.out.println("���������֮��" +"֮��ȱ���������");
						}
					}
					return -1;
			}
			else
			{
				return PR;
			}
		}
		else
		{
			System.out.println(Vt1+"��"+Vt2+"֮�䲻�������ȹ�ϵ��");
			return -1;
		}
		
	}
	public static void main(String[] args) {
		
	
		OPGMain opg= new OPGMain();
		opg.OPGanalysis("zhuanti��_1.tys");
		
	}
}
