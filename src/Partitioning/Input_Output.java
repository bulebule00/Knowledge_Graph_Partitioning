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
	
	//��
	public static ArrayList<String> read_spo() {
        File csv = new File(Config.read_csv_filePath); // CSV�ļ�·��
        csv.setReadable(true);//���ÿɶ�
        csv.setWritable(true);//���ÿ�д
        BufferedReader br = null;
        System.out.println("--------��ʼ��ȡCSV�ļ�--------");
        System.out.println("·����"+Config.read_csv_filePath);       
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
            while ((line = br.readLine()) != null) // ��ȡ�������ݸ�line����
            {
                everyLine = line;
              //  System.out.println(everyLine);
                allString.add(everyLine);
            }
            lines=allString.size();
            System.out.println("�Ѷ�ȡ  " + allString.size()+" ����Ԫ��");
            System.out.println("--------CSV�ļ���ȡ���--------"+"\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return allString;
        
    }
	
	//д
    static public void writeAllNodeCSV() {      
        try {
        	Iterator<Entry<String, Node>> iter = Config.subject_object.entrySet().iterator();
        	System.out.println("--------��ʼ�����ڵ���Ϣ--------");
        	System.out.println("·��Ϊ��"+Config.NodecsvFilePath);

    		CsvWriter csvWriter = new CsvWriter(Config.NodecsvFilePath,',', Charset.forName("utf-8"));
            // д����
            String[] headers = {"Name","ClusterNo","Property","Degree","Visited","Border"};
            csvWriter.writeRecord(headers);
    		while (iter.hasNext()) {
    			//nodeNo++;
    			@SuppressWarnings("rawtypes")
    			Map.Entry entry = (Map.Entry) iter.next();
    			Node val = (Node) entry.getValue();//ȡ��Node��Ա
    			//writer.write("�ڵ�"+nodeNo+val.print()+"\n");
    			String[] writeLine=val.print().split(",");
                csvWriter.writeRecord(writeLine);
    			}	                    
            csvWriter.close();
            System.out.println("--------Node_CSV�ļ��Ѿ�д��--------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }	

    public static void writeClusterNodeCSV()  throws IOException 
    {
      	int i=0;//��Ⱥ��
    	System.out.println("--------��ʼ����ÿ����Ⱥ�нڵ���Ϣ--------");
    	System.out.println("·��Ϊ��"+Config.ClusterPath);
    	Iterator<HashSet<Node>> it=Config.clusters_node.iterator();
    	while (it.hasNext())
    	{
    		HashSet<Node> cluster_nodes_list=it.next(); //ȡ����ǰ��Ⱥ��node�б�
    		String filepath=Config.ClusterPath+"Cluster"+i+"_Node.csv";
    		writeOneClusterNodeCSV(cluster_nodes_list,filepath);
            System.out.println("��Ⱥ"+i+"�ڵ���Ϣ�ѵ���,��"+cluster_nodes_list.size()+"����"); 
    		i++;
    	}   

    	System.out.println("--------��Ⱥ�нڵ���Ϣ�������--------\n");
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

		    	System.out.println("--------��ʼ������Ⱥ��Ϣ--------");
		    	System.out.println("·��Ϊ��"+Config.ClusterCsvFilePath);
		   
				CsvWriter csvWriter = new CsvWriter(Config.ClusterCsvFilePath,',', Charset.forName("utf-8"));
		        // д����
		        String[] headers = {"clusterNo","density","nodeNum"};
		        csvWriter.writeRecord(headers);
				while (iter.hasNext()) {
					Cluster clu=iter.next();			
					String[] writeLine=clu.print().split(",");
		            csvWriter.writeRecord(writeLine);
					}	                    
		        csvWriter.close();
		        
		        System.out.println("--------Cluster_CSV�ļ��Ѿ�д��--------\n");
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
	    	System.out.println("--------��ʼд������--------");
	    	System.out.println("·��Ϊ��"+Config.Index_tablePath);
			//BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Config.Index_tablePath)));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Config.Index_tablePath)),"utf-8"));
			//����������
			Iterator<Entry<Node,HashSet<Cluster>>> iter = Config.Index_table.entrySet().iterator();
			while (iter.hasNext()) 
			{
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				
				Node key = (Node) entry.getKey();
				@SuppressWarnings("unchecked")
				HashSet<Cluster> val = (HashSet<Cluster>) entry.getValue();//ȡ��Node��Ա
			
				Object[] toArray =  val.toArray();
				
				writer.write(key.getName()+"���ڼ�ȺΪ��");
					
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
		System.out.println("--------�������Ѿ�д��--------\n");
	}

	static public void writeClusterSpo() throws IOException
	{
        	
        	System.out.println("--------��ʼ����Cluster--------");
        	System.out.println("������ļ��У�"+Config.ClusterPath);

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
        	
        	
        	
            System.out.println("--------Cluster��Ϣ�Ѿ�����--------\n");

	}

}
