package syntaxdirectedtranslation;
/**�﷨�Ƶ���������������
 * ʵ��Ŀ�꣺
 * �������������ֵ���SLR(1)�ķ��﷨�Ƶ������м������Ԫʽ�Ĺ��̡�
	G[A]:	A��V=E
			E��E+T�OE-T�OT
          	T��T*F�OT/F�OF
          	F��(E)�Oi
          	V��i
 * ���˵�����ս����iΪ�û�����ļ򵥱���������ʶ���Ķ��塣
 * ���Ҫ��	��1�������ķ���SLR(1)����������﷨�Ƶ�������̣�����ÿһ����ʽ��Ӧ�����嶯����
 * 			��2������м������Ԫʽ�Ľṹ��
 * 			��3�����봮Ӧ�Ǵʷ������������Ԫʽ���У���ĳ��ֵ��䡰ר��1���������������Ϊ��ֵ������Ԫʽ�����м��ļ���
 * 			��4��������������������������걸��������������ִ�н����Ԫʽ���С�
 * */
public class TranslationMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//1�������ķ�����ʶ���ǰ׺��DFA
		DFAManager dfa = new DFAManager();
		dfa.buildDFA();
		dfa.displayDFA();
		//2������SLR(1)������
		SLR1AnalysisTable slr1t = new SLR1AnalysisTable(dfa);
		slr1t.buildSLR1Table();
		slr1t.displayTable();
		//3������������������ִ�У������Ԫʽ
		SLR1Analyzer slr1a = new SLR1Analyzer("zhuanti5_1.tys");
		slr1a.addSLR1AnalysisTable(slr1t);
		slr1a.anaylsisProcessing();
	}

}
