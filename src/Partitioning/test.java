package Partitioning;

import java.io.IOException;

public class test {


	public static void main(String[] args) throws IOException {	
		
		initialize();//�������ݼ�
		
		do_cluster();//��ʼ����
		
		print();//������
				
	}
	
	
	private static void initialize() throws IOException
	{
		//��Csv����Hashmap
		Config.data=Input_Output.read_spo();
		Put_spo_in_hashmap.put_csv_in_hashmap();		
		//��Hashmap��������,�õ��ܶ�����
		Hashmap_sort.sort();
	}
	
	private static void do_cluster() throws IOException
	{
		//clique����
		CliqueCluster.do_clique();
	}
	
	private static void print() throws IOException
	{

		//����ڵ���Ϣ
		Input_Output.writeAllNodeCSV();
		//���漯Ⱥ��Ϣ
		Input_Output.writeClusterInfo();
		//������Ⱥ�нڵ���Ϣ
		Input_Output.writeClusterNodeCSV();
		//����ֲ�ʽ����
		Input_Output.writeClusterSpo();
		//����������
		Input_Output.writeIndexTable();


	}
	

}
