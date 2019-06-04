package syntaxdirectedtranslation;
/**语法制导翻译的主函数入口
 * 实验目标：
 * 完成以下描述赋值语句SLR(1)文法语法制导生成中间代码四元式的过程。
	G[A]:	A→V=E
			E→E+T∣E-T∣T
          	T→T*F∣T/F∣F
          	F→(E)∣i
          	V→i
 * 设计说明：终结符号i为用户定义的简单变量，即标识符的定义。
 * 设计要求：	（1）构造文法的SLR(1)分析表，设计语法制导翻译过程，给出每一产生式对应的语义动作；
 * 			（2）设计中间代码四元式的结构；
 * 			（3）输入串应是词法分析的输出二元式序列，即某赋值语句“专题1”的输出结果，输出为赋值语句的四元式序列中间文件；
 * 			（4）设计两个测试用例（尽可能完备），并给出程序执行结果四元式序列。
 * */
public class TranslationMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//1、根据文法创建识别活前缀的DFA
		DFAManager dfa = new DFAManager();
		dfa.buildDFA();
		dfa.displayDFA();
		//2、构建SLR(1)分析表
		SLR1AnalysisTable slr1t = new SLR1AnalysisTable(dfa);
		slr1t.buildSLR1Table();
		slr1t.displayTable();
		//3、构建分析器，分析执行，输出四元式
		SLR1Analyzer slr1a = new SLR1Analyzer("zhuanti5_1.tys");
		slr1a.addSLR1AnalysisTable(slr1t);
		slr1a.anaylsisProcessing();
	}

}
