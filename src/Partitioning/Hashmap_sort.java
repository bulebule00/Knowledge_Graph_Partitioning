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
		System.out.println("--------��ʼ��SO��P�����ܶ�����--------");
		sort_one_so(Config.subject_object,Config.list_so,"S_and_O_Degree");
		sort_one_p(Config.predication,Config.list_p,"P_Degree");
		System.out.println("--------�ܶ��������--------");
		System.out.println();
	}
	
	private static void sort_one_so(Map<String, Node> map,List<Map.Entry<String, Node>> list,String filename) throws IOException
	{
		/***
		����hashmap�е�ֵ��hashmap����
		 */
		
		//HashMap��Map����û�������ܣ���Ҫ���н����ɵ����򣬿�����ArrayList�е�sort����
		String filePath=Config.hashmap_store_filePath+"//"+filename+".txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
	    for(Map.Entry<String, Node> entry : map.entrySet()){
	        list.add(entry); //��map�е�Ԫ�ط���list��	             
	    	}
	    System.out.println(filename+"��HashMap����list");
	    list.sort(new Comparator<Map.Entry<String, Node>>(){
            @Override
             public int compare(Map.Entry<String, Node> o1, Map.Entry<String,Node> o2) {
                  return o2.getValue().getDegree()-o1.getValue().getDegree();} 
                 //���򣨴Ӵ�С�����У�����Ϊ��return o1.getValue()-o2.getValue��
	    	}); 	
	    
        for(Map.Entry<String, Node> entry: list){            
			//�����д���ļ�
		      writer.write(entry.getValue().getName()+" = "+entry.getValue().getDegree()+"\n");
           // System.out.println(entry);
        	}
        writer.close();
	   	System.out.println("�ܶ���Ϣ��������"+filePath);	

   }
	
	
	private static void sort_one_p(Map<String, Integer> map,List<Map.Entry<String, Integer>> list,String filename) throws IOException
	{
		/***
		����hashmap�е�ֵ��hashmap����
		 */
		
		//HashMap��Map����û�������ܣ���Ҫ���н����ɵ����򣬿�����ArrayList�е�sort����
		String filePath=Config.hashmap_store_filePath+"//"+filename+".txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
	    for(Map.Entry<String, Integer> entry : map.entrySet()){
	        list.add(entry); //��map�е�Ԫ�ط���list��	             
	    	}
	    System.out.println(filename+"��HashMap����list");
	    list.sort(new Comparator<Map.Entry<String, Integer>>(){
            @Override
             public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                  return o2.getValue()-o1.getValue();} 
                 //���򣨴Ӵ�С�����У�����Ϊ��return o1.getValue()-o2.getValue��
	    	}); 	
	    
        for(Map.Entry<String, Integer> entry: list){            
			//�����д���ļ�
	        writer.write(entry.toString()+"\n");
           // System.out.println(entry);
        	}
        writer.close();
	   	System.out.println("�ܶ���Ϣ��������"+filePath);	

   }
	
	
	
}
