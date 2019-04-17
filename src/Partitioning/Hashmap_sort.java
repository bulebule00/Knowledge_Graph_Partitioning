package Partitioning;

import java.util.Map;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class Hashmap_sort {


	public static void sort() throws IOException
	{
		System.out.println("--------开始对SO和P根据密度排序--------");
		sort_one_so(Config.subject_object,Config.list_so,"S_and_O_Degree");
		sort_one_p(Config.predication,Config.list_p,"P_Degree");
		System.out.println("--------密度排序完成--------");
		System.out.println();
	}
	
	private static void sort_one_so(Map<String, Node> map,List<Map.Entry<String, Node>> list,String filename) throws IOException
	{
		/***
		根据hashmap中的值对hashmap排序
		 */
		
		//HashMap或Map本身没有排序功能，若要进行较轻松的排序，可利用ArrayList中的sort方法
		String filePath=Config.hashmap_store_filePath+"//"+filename+".txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
	    for(Map.Entry<String, Node> entry : map.entrySet()){
	        list.add(entry); //将map中的元素放入list中	             
	    	}
	    System.out.println(filename+"的HashMap存入list");
	    list.sort(new Comparator<Map.Entry<String, Node>>(){
            @Override
             public int compare(Map.Entry<String, Node> o1, Map.Entry<String,Node> o2) {
                  return o2.getValue().getDegree()-o1.getValue().getDegree();} 
                 //逆序（从大到小）排列，正序为“return o1.getValue()-o2.getValue”
	    	}); 	
	    
        for(Map.Entry<String, Node> entry: list){            
			//将结果写入文件
		      writer.write(entry.getValue().getName()+" = "+entry.getValue().getDegree()+"\n");
           // System.out.println(entry);
        	}
        writer.close();
	   	System.out.println("密度信息保存至："+filePath);	

   }
	
	
	private static void sort_one_p(Map<String, Integer> map,List<Map.Entry<String, Integer>> list,String filename) throws IOException
	{
		/***
		根据hashmap中的值对hashmap排序
		 */
		
		//HashMap或Map本身没有排序功能，若要进行较轻松的排序，可利用ArrayList中的sort方法
		String filePath=Config.hashmap_store_filePath+"//"+filename+".txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
	    for(Map.Entry<String, Integer> entry : map.entrySet()){
	        list.add(entry); //将map中的元素放入list中	             
	    	}
	    System.out.println(filename+"的HashMap存入list");
	    list.sort(new Comparator<Map.Entry<String, Integer>>(){
            @Override
             public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                  return o2.getValue()-o1.getValue();} 
                 //逆序（从大到小）排列，正序为“return o1.getValue()-o2.getValue”
	    	}); 	
	    
        for(Map.Entry<String, Integer> entry: list){            
			//将结果写入文件
	        writer.write(entry.toString()+"\n");
           // System.out.println(entry);
        	}
        writer.close();
	   	System.out.println("密度信息保存至："+filePath);	

   }
	
	
	
}
