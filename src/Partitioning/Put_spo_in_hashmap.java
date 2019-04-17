package Partitioning;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Put_spo_in_hashmap {

	//public static String filePath="C://Users//liu01//Desktop//red_building//spo_dataset.txt";
	

	public  static void put_csv_in_hashmap() throws IOException //��ȡcsv��ʽ����Ԫ�飬���s p o�ֱ����������������
	{

		/***
		 ��csv�е�ÿ����Ԫ���ֳ�s��p��o�����֣�Ȼ�����put_spo_in_hashmap���hashmap�ļ��롣
		 */
		System.out.println("--------��ʼ���SPO--------");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Config.spo_store_filePath)));
		
		Iterator<String> it=Config.data.iterator();
		it.next(); //������ͷ

		while(it.hasNext())
		{
			String line=it.next();//ȡ��һ����Ԫ��	
		//	System.out.println(line);  
			List<String> result = Arrays.asList(line.split(",")); 
			
			//��ȡspo��Ԫ�顣 ���´���������ݼ����ݽ����޸�
			Iterator<String>list_it=result.iterator();		
			String s=list_it.next();
			String o=list_it.next();
			list_it.next();//����Ӣ��label		
			String p=list_it.next();	
			
		//���spo
		//	System.out.println("s="+s);
			put_s_in_hashmap(Config.subject_object ,s,p,o);
		//	System.out.println("p="+p);
			put_p_in_hashmap(Config.predication,p);		
		//	System.out.println("o="+o);
			put_o_in_hashmap( Config.subject_object ,o,p,s);
		//	System.out.println();
			
			//�����д���ļ�
		     writer.write("\n"+"s="+s+"\n"+"p="+p+"\n"+"o="+o+"\n");
		}
		writer.close();
		System.out.println("ʵ��  "+ Config.subject_object .size()+" ��");
		System.out.println("��ϵ   "+Config.predication.size()+" ��");
		System.out.println("ʵ��͹�ϵ�ֱ��������HashMap");
		System.out.println("ʵ��͹�ϵ������ ��"+Config.spo_store_filePath);
		System.out.println("--------SPO������--------"+"\n");
//		System.out.println("o.size()= "+object.size());
	}
	
	private static void put_s_in_hashmap(Map<String, Node> spo,String s,String p,String o)
	{
		/***
		 ��spo��Ϊ s o p������ ��������hashmap;
		 */
		Node result=spo.get(s);	
		if(result==null)
		{
			//����ǵ�һ�Σ����½�һ��node����Ϊ1
			Node n=new Node(s);
			n.add_spo((String)(s+","+p+","+o));
			spo.put(s,n); 
		}
		else
		{
			//�������ڵ��Ѵ���,�Ͱ����Ķ�+1
			result.add_spo((String)(s+","+p+","+o));
			result.setDegree(result.getDegree()+1);
		}
	}
	private static void put_o_in_hashmap(Map<String, Node> spo,String o,String p,String s)
	{
		/***
		 ��spo��Ϊ s o p������ ��������hashmap;
		 */
		Node result=spo.get(o);	
		if(result==null)
		{
			//����ǵ�һ�Σ����½�һ��node����Ϊ1
			Node n=new Node(o);
			n.add_spo((String)(s+","+p+","+o));
			spo.put(o,n); 
		}
		else
		{
			//�������ڵ��Ѵ���,�Ͱ����Ķ�+1
			result.add_spo((String)(s+","+p+","+o));
			result.setDegree(result.getDegree()+1);
		}
	}
	private static void put_p_in_hashmap(Map<String, Integer> spo,String spo_name)
	{
		/***
		��spo��Ϊ s o p������ ��������hashmap;
		 */
		boolean book=false; // ��־λ���Ƿ��Ѽ���hashmap
		int result = 0;
		try {
			result=(Integer)spo.get(spo_name);			
		}
		catch(java.lang.NullPointerException a)
		{
			//����ǵ�һ��
			spo.put(spo_name,(Integer)1); 
			book=true;
		}
		if(!book)
		{
			//�������ڵ��Ѵ���
			spo.put(spo_name, (Integer)result+1);
		}
	}
}
