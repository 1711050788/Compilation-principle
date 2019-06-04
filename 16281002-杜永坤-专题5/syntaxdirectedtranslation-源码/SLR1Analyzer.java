package syntaxdirectedtranslation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

//SLR1分析器
//输入符号串
//状态栈
//符号栈
//产生式
public class SLR1Analyzer {
	private List<String> grammar = new AssignmentGrammar().getBasicGrammar();//文法
	private BufferedReader br = null; //输入的二元式文件流
	private static List<String> InputStream = new ArrayList<String>(); //从二元式文件中拆解的符号穿输入流
	private int indexP = 0; //扫描指针，初始为0
	private Stack<Integer> stateStack = new Stack<Integer>(); //状态栈
	private Stack<String> symbolStack = new Stack<String>(); //符号栈
	private Map<String,String> SLR1at = null;
	private AssignmentTranslationGrammar  atg = new AssignmentTranslationGrammar(); //语法制导翻译类
	public void addSLR1AnalysisTable(SLR1AnalysisTable slr1) { //添加分析表
		SLR1at = slr1.getSLR1Table();
	}
	public SLR1Analyzer(String fileName) {
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
			for(int i = 0;i<InputStream.size();++i) {
				System.out.print(InputStream.get(i)+" ");
			}
			System.out.println();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(fileName+"文件不存在...");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//分析器执行
	public void anaylsisProcessing() {
		System.out.println("分析过程如下：");
		//System.out.println("状态栈\t\t\t符号栈\t\t\t输入流\t\t\t产生式");
		//#符号先入栈，0状态入栈
		stateStack.push(0);
		symbolStack.push("#");
		//用当前栈顶状态Sm以及正在输入的符号a组成符号对查分析表
		boolean keepScann = true;
		printAction("");
		while(keepScann) {
			int keyState = stateStack.peek();
			String inputSymbol = InputStream.get(indexP);
			//对输入字符处理
			if(Character.isLetterOrDigit(inputSymbol.charAt(0))) {
				atg.getSymbolValue().get("i").push(inputSymbol);
				inputSymbol = "i";
			}
			String key = keyState+","+inputSymbol;
		//System.out.println(key);
			String action = SLR1at.get(key);
			if(keyState==2&&!inputSymbol.equals("=")) 
			{
				System.out.println("缺少    = ");
			}
			if(inputSymbol.equals("i"))
			{
				if(keyState!=0&&keyState!=4&&keyState!=7&&keyState!=10&&keyState!=11&&keyState!=13&&keyState!=14)
				{
					System.out.println("缺少操作符");
				}
			}
			if(action==null) {
				System.out.println("分析错误");
				break;
			}
			char choice = action.charAt(0);
			String production = "";
			switch(choice) {
			case 's': //移进项目
				//将a压栈，将转移目标状态压栈
				//如果压栈是i,出栈时就会丢失信息，也没法查表
				//symbolStack.push(inputSymbol);
				symbolStack.push(InputStream.get(indexP));
				stateStack.push(Integer.parseInt(action.substring(1)));
				++indexP;
				printAction("");
				break;
			case 'r': //归约项目
				//查询产生式表
				production = grammar.get(Integer.parseInt(action.substring(1)));
				Character A = production.charAt(0); //产生式左部
				production = production.substring(3);//产生式右部
				for(int i = 0;i<production.length();++i) {
					stateStack.pop();
					symbolStack.pop();
				}
				symbolStack.push(A+"");
				String Sk = SLR1at.get(stateStack.peek()+","+A);
				stateStack.push(Integer.parseInt(Sk));
				String pro = A+"->"+production;
				printAction(pro);
				atg.translateAction(pro);
				break;
			case 'a': //接受项目
				System.out.println("分析成功");
				keepScann = false;
				//显示生成的四元式序列
				System.out.println("生成的四元式如下：");
				atg.displayFourYuanShi();
				break;
			default:  //应该不存在
				break;
			}
		}
		
	}
	
	//打印输出
	public void printAction(String pro) {
		System.out.print("状态栈："+stateStack+"\t\t符号栈："+symbolStack);
		System.out.print("\t\t输入流："+InputStream.subList(indexP,InputStream.size()));
		System.out.println("\t\t产生式："+pro);
	}
}
