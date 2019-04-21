package Partitioning;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;



public class Find {



	public static List<String> one_jump(String start)  //查找一跳范围内的节点
	{
		//List<Map.Entry<String, Integer>> list=new ArrayList<>(); //用于存放每个map

		List<String> list=new ArrayList<>(); //存放三元组

		System.out.println("--------开始查询以"+start+"为起始点,一跳范围内的节点--------");
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
			//System.out.println("s=="+s+"  "+"start=="+start);
			//寻找距离start节点一跳距离的节点
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

		System.out.println("以"+start+"为中心,一跳范围内的节点有"+list.size()+"个");
		System.out.println("--------以"+start+"为中心,一跳范围内的节点查询完毕--------");
		return list;
	}




}
