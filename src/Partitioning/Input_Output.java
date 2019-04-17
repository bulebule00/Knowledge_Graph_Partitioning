package Partitioning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.csvreader.CsvWriter;

public class Input_Output {
	//static String read_filePath="C://Users//liu01//Desktop//red_building//triples.csv";
	static int lines;
	
	//读
	public static ArrayList<String> read_spo() {
        File csv = new File(Config.read_csv_filePath); // CSV文件路径
        csv.setReadable(true);//设置可读
        csv.setWritable(true);//设置可写
        BufferedReader br = null;
        System.out.println("--------开始读取CSV文件--------");
        System.out.println("路径："+Config.read_csv_filePath);       
        try {
        	  DataInputStream in=new DataInputStream(new FileInputStream(csv));
//            br = new BufferedReader(new FileReader(csv));
        	  br=new BufferedReader(new InputStreamReader(in,"utf-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        String line = "";
        String everyLine = "";
        ArrayList<String> allString = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) // 读取到的内容给line变量
            {
                everyLine = line;
              //  System.out.println(everyLine);
                allString.add(everyLine);
            }
            lines=allString.size();
            System.out.println("已读取  " + allString.size()+" 行三元组");
            System.out.println("--------CSV文件读取完成--------"+"\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return allString;
        
    }
	
	//写
    static public void writeAllNodeCSV() {      
        try {
        	Iterator<Entry<String, Node>> iter = Config.subject_object.entrySet().iterator();
        	System.out.println("--------开始导出节点信息--------");
        	System.out.println("路径为："+Config.NodecsvFilePath);

    		CsvWriter csvWriter = new CsvWriter(Config.NodecsvFilePath,',', Charset.forName("utf-8"));
            // 写内容
            String[] headers = {"Name","ClusterNo","Property","Degree","Visited","Border"};
            csvWriter.writeRecord(headers);
    		while (iter.hasNext()) {
    			//nodeNo++;
    			@SuppressWarnings("rawtypes")
    			Map.Entry entry = (Map.Entry) iter.next();
    			Node val = (Node) entry.getValue();//取出Node成员
    			//writer.write("节点"+nodeNo+val.print()+"\n");
    			String[] writeLine=val.print().split(",");
                csvWriter.writeRecord(writeLine);
    			}	                    
            csvWriter.close();
            System.out.println("--------Node_CSV文件已经写入--------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }	

    public static void writeClusterNodeCSV()  throws IOException 
    {
      	int i=0;//集群号
    	System.out.println("--------开始导出每个集群中节点信息--------");
    	System.out.println("路径为："+Config.ClusterPath);
    	Iterator<HashSet<Node>> it=Config.clusters_node.iterator();
    	while (it.hasNext())
    	{
    		HashSet<Node> cluster_nodes_list=it.next(); //取出当前集群的node列表
    		String filepath=Config.ClusterPath+"Cluster"+i+"_Node.csv";
    		writeOneClusterNodeCSV(cluster_nodes_list,filepath);
            System.out.println("集群"+i+"节点信息已导出,共"+cluster_nodes_list.size()+"个。"); 
    		i++;
    	}   

    	System.out.println("--------集群中节点信息导出完成--------\n");
    }
    
    static private void writeOneClusterNodeCSV(HashSet<Node> cluster_nodes_list,String filepath) throws IOException {    
    		CsvWriter csvWriter = new CsvWriter(filepath,',', Charset.forName("utf-8"));
            String[] headers = {"Name","ClusterNo","Property","Degree","Visited","Border"};
            csvWriter.writeRecord(headers);
            
            Iterator<Node> iter=cluster_nodes_list.iterator();
            while(iter.hasNext())
            {
                String[] writeLine=iter.next().print().split(",");
                csvWriter.writeRecord(writeLine);
            }
            csvWriter.close();
            

    	
    }	
    
	static public void writeClusterInfo() {      
	    try 
	    	{
		    	Iterator<Cluster> iter = Config.clusters.iterator();

		    	System.out.println("--------开始导出集群信息--------");
		    	System.out.println("路径为："+Config.ClusterCsvFilePath);
		   
				CsvWriter csvWriter = new CsvWriter(Config.ClusterCsvFilePath,',', Charset.forName("utf-8"));
		        // 写内容
		        String[] headers = {"clusterNo","density","nodeNum"};
		        csvWriter.writeRecord(headers);
				while (iter.hasNext()) {
					Cluster clu=iter.next();			
					String[] writeLine=clu.print().split(",");
		            csvWriter.writeRecord(writeLine);
					}	                    
		        csvWriter.close();
		        
		        System.out.println("--------Cluster_CSV文件已经写入--------\n");
		    } 
	    catch (IOException e) 
	    	{
		        e.printStackTrace();
		    }
	}	

	static public void writeIndexTable() 
	{
		try 
		{
	    	System.out.println("--------开始写索引表--------");
	    	System.out.println("路径为："+Config.Index_tablePath);
			//BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Config.Index_tablePath)));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Config.Index_tablePath)),"utf-8"));
			//遍历索引表
			Iterator<Entry<Node,HashSet<Cluster>>> iter = Config.Index_table.entrySet().iterator();
			while (iter.hasNext()) 
			{
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				
				Node key = (Node) entry.getKey();
				@SuppressWarnings("unchecked")
				HashSet<Cluster> val = (HashSet<Cluster>) entry.getValue();//取出Node成员
			
				Object[] toArray =  val.toArray();
				
				writer.write(key.getName()+"所在集群为：");
					
				int i=0;
				for(i=0;i<toArray.length-1;++i)
				{
					//writer.write("\""+val.get(i).getClusterNo()+"\""+",");
					writer.write("\""+((Cluster) toArray[i]).getClusterNo()+"\""+",");
				}
				writer.write("\""+((Cluster) toArray[i]).getClusterNo()+"\"");
				writer.write("\n");
			}
			writer.close();
		}
	    catch (IOException e) 
    	{
	        e.printStackTrace();
	    }
		System.out.println("--------索引表已经写入--------\n");
	}

	static public void writeClusterSpo() throws IOException
	{
        	
        	System.out.println("--------开始导出Cluster--------");
        	System.out.println("存放于文件夹："+Config.ClusterPath);

        	Iterator<Cluster> it=Config.clusters.iterator();
        	while(it.hasNext())
        	{
        		Cluster clu=it.next();
        		
        		String filepath=Config.ClusterPath+"Cluster"+clu.getClusterNo()+"_spo.csv";
    			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filepath)),"utf-8"));
        		writer.write("Head,Relation,Tail\n");
        		
        		HashSet<String> spoSet=clu.getSpo();
        		Iterator<String> iter=spoSet.iterator();
        		while (iter.hasNext())
        		{
        			writer.write(iter.next()+"\n");
        		}
        		writer.close();
        	}
        	
        	
        	
            System.out.println("--------Cluster信息已经导出--------\n");

	}

}
