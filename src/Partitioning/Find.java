package Partitioning;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;



public class Find {
	


	public static List<String> one_jump(String start)  //����һ����Χ�ڵĽڵ�
	{
		//List<Map.Entry<String, Integer>> list=new ArrayList<>(); //���ڴ��ÿ��map

		List<String> list=new ArrayList<>(); //�����Ԫ��

		System.out.println("--------��ʼ��ѯ��"+start+"Ϊ��ʼ��,һ����Χ�ڵĽڵ�--------");
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
			//System.out.println("s=="+s+"  "+"start=="+start);
			//Ѱ�Ҿ���start�ڵ�һ������Ľڵ�
			if(s.equals(start)==true || o.equals(start)==true)
			{
				list.add((String)(s+","+p+","+o));
				
				//System.out.println("s=="+s+"  "+"start=="+start);
				//System.out.println(o);
			}

		}

		Iterator<String> it1=list.iterator();
		while(it1.hasNext())
		{
			System.out.println(it1.next());
		}
		
		System.out.println("��"+start+"Ϊ����,һ����Χ�ڵĽڵ���"+list.size()+"��");
		System.out.println("--------��"+start+"Ϊ����,һ����Χ�ڵĽڵ��ѯ���--------");
		return list;			
	}


	

}
