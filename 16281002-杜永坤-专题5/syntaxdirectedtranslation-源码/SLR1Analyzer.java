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

//SLR1������
//������Ŵ�
//״̬ջ
//����ջ
//����ʽ
public class SLR1Analyzer {
	private List<String> grammar = new AssignmentGrammar().getBasicGrammar();//�ķ�
	private BufferedReader br = null; //����Ķ�Ԫʽ�ļ���
	private static List<String> InputStream = new ArrayList<String>(); //�Ӷ�Ԫʽ�ļ��в��ķ��Ŵ�������
	private int indexP = 0; //ɨ��ָ�룬��ʼΪ0
	private Stack<Integer> stateStack = new Stack<Integer>(); //״̬ջ
	private Stack<String> symbolStack = new Stack<String>(); //����ջ
	private Map<String,String> SLR1at = null;
	private AssignmentTranslationGrammar  atg = new AssignmentTranslationGrammar(); //�﷨�Ƶ�������
	public void addSLR1AnalysisTable(SLR1AnalysisTable slr1) { //��ӷ�����
		SLR1at = slr1.getSLR1Table();
	}
	public SLR1Analyzer(String fileName) {
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
			for(int i = 0;i<InputStream.size();++i) {
				System.out.print(InputStream.get(i)+" ");
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
	}
	
	//������ִ��
	public void anaylsisProcessing() {
		System.out.println("�����������£�");
		//System.out.println("״̬ջ\t\t\t����ջ\t\t\t������\t\t\t����ʽ");
		//#��������ջ��0״̬��ջ
		stateStack.push(0);
		symbolStack.push("#");
		//�õ�ǰջ��״̬Sm�Լ���������ķ���a��ɷ��ŶԲ������
		boolean keepScann = true;
		printAction("");
		while(keepScann) {
			int keyState = stateStack.peek();
			String inputSymbol = InputStream.get(indexP);
			//�������ַ�����
			if(Character.isLetterOrDigit(inputSymbol.charAt(0))) {
				atg.getSymbolValue().get("i").push(inputSymbol);
				inputSymbol = "i";
			}
			String key = keyState+","+inputSymbol;
		//System.out.println(key);
			String action = SLR1at.get(key);
			if(keyState==2&&!inputSymbol.equals("=")) 
			{
				System.out.println("ȱ��    = ");
			}
			if(inputSymbol.equals("i"))
			{
				if(keyState!=0&&keyState!=4&&keyState!=7&&keyState!=10&&keyState!=11&&keyState!=13&&keyState!=14)
				{
					System.out.println("ȱ�ٲ�����");
				}
			}
			if(action==null) {
				System.out.println("��������");
				break;
			}
			char choice = action.charAt(0);
			String production = "";
			switch(choice) {
			case 's': //�ƽ���Ŀ
				//��aѹջ����ת��Ŀ��״̬ѹջ
				//���ѹջ��i,��ջʱ�ͻᶪʧ��Ϣ��Ҳû�����
				//symbolStack.push(inputSymbol);
				symbolStack.push(InputStream.get(indexP));
				stateStack.push(Integer.parseInt(action.substring(1)));
				++indexP;
				printAction("");
				break;
			case 'r': //��Լ��Ŀ
				//��ѯ����ʽ��
				production = grammar.get(Integer.parseInt(action.substring(1)));
				Character A = production.charAt(0); //����ʽ��
				production = production.substring(3);//����ʽ�Ҳ�
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
			case 'a': //������Ŀ
				System.out.println("�����ɹ�");
				keepScann = false;
				//��ʾ���ɵ���Ԫʽ����
				System.out.println("���ɵ���Ԫʽ���£�");
				atg.displayFourYuanShi();
				break;
			default:  //Ӧ�ò�����
				break;
			}
		}
		
	}
	
	//��ӡ���
	public void printAction(String pro) {
		System.out.print("״̬ջ��"+stateStack+"\t\t����ջ��"+symbolStack);
		System.out.print("\t\t��������"+InputStream.subList(indexP,InputStream.size()));
		System.out.println("\t\t����ʽ��"+pro);
	}
}
