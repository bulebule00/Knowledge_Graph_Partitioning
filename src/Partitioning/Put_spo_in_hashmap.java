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
	

	public  static void put_csv_in_hashmap() throws IOException //读取csv格式的三元组，拆成s p o分别调用以下三个函数
	{

		/***
		 将csv中的每行三元组拆分成s、p、o三部分，然后调用put_spo_in_hashmap完成hashmap的加入。
		 */
		System.out.println("--------开始拆分SPO--------");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Config.spo_store_filePath)));
		
		Iterator<String> it=Config.data.iterator();
		it.next(); //跳过表头

		while(it.hasNext())
		{
			String line=it.next();//取出一个三元组	
		//	System.out.println(line);  
			List<String> result = Arrays.asList(line.split(",")); 
			
			//读取spo三元组。 以下代码根据数据集内容进行修改
			Iterator<String>list_it=result.iterator();		
			String s=list_it.next();
			String o=list_it.next();
			list_it.next();//跳过英文label		
			String p=list_it.next();	
			
		//输出spo
		//	System.out.println("s="+s);
			put_s_in_hashmap(Config.subject_object ,s,p,o);
		//	System.out.println("p="+p);
			put_p_in_hashmap(Config.predication,p);		
		//	System.out.println("o="+o);
			put_o_in_hashmap( Config.subject_object ,o,p,s);
		//	System.out.println();
			
			//将结果写入文件
		     writer.write("\n"+"s="+s+"\n"+"p="+p+"\n"+"o="+o+"\n");
		}
		writer.close();
		System.out.println("实体  "+ Config.subject_object .size()+" 个");
		System.out.println("关系   "+Config.predication.size()+" 种");
		System.out.println("实体和关系分别存入两个HashMap");
		System.out.println("实体和关系保存至 ："+Config.spo_store_filePath);
		System.out.println("--------SPO拆分完毕--------"+"\n");
//		System.out.println("o.size()= "+object.size());
	}
	
	private static void put_s_in_hashmap(Map<String, Node> spo,String s,String p,String o)
	{
		/***
		 将spo分为 s o p三部分 放入两个hashmap;
		 */
		Node result=spo.get(s);	
		if(result==null)
		{
			//如果是第一次，就新建一个node，度为1
			Node n=new Node(s);
			n.add_spo((String)(s+","+p+","+o));
			spo.put(s,n); 
		}
		else
		{
			//如果这个节点已存在,就把他的度+1
			result.add_spo((String)(s+","+p+","+o));
			result.setDegree(result.getDegree()+1);
		}
	}
	private static void put_o_in_hashmap(Map<String, Node> spo,String o,String p,String s)
	{
		/***
		 将spo分为 s o p三部分 放入两个hashmap;
		 */
		Node result=spo.get(o);	
		if(result==null)
		{
			//如果是第一次，就新建一个node，度为1
			Node n=new Node(o);
			n.add_spo((String)(s+","+p+","+o));
			spo.put(o,n); 
		}
		else
		{
			//如果这个节点已存在,就把他的度+1
			result.add_spo((String)(s+","+p+","+o));
			result.setDegree(result.getDegree()+1);
		}
	}
	private static void put_p_in_hashmap(Map<String, Integer> spo,String spo_name)
	{
		/***
		将spo分为 s o p三部分 放入两个hashmap;
		 */
		boolean book=false; // 标志位，是否已加入hashmap
		int result = 0;
		try {
			result=(Integer)spo.get(spo_name);			
		}
		catch(java.lang.NullPointerException a)
		{
			//如果是第一次
			spo.put(spo_name,(Integer)1); 
			book=true;
		}
		if(!book)
		{
			//如果这个节点已存在
			spo.put(spo_name, (Integer)result+1);
		}
	}
}
