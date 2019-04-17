package Partitioning;

import java.io.IOException;

public class test {


	public static void main(String[] args) throws IOException {	
		
		initialize();//导入数据集
		
		do_cluster();//开始聚类
		
		print();//输出结果
				
	}
	
	
	private static void initialize() throws IOException
	{
		//将Csv导入Hashmap
		Config.data=Input_Output.read_spo();
		Put_spo_in_hashmap.put_csv_in_hashmap();		
		//对Hashmap进行排序,得到密度序列
		Hashmap_sort.sort();
	}
	
	private static void do_cluster() throws IOException
	{
		//clique聚类
		CliqueCluster.do_clique();
	}
	
	private static void print() throws IOException
	{

		//保存节点信息
		Input_Output.writeAllNodeCSV();
		//保存集群信息
		Input_Output.writeClusterInfo();
		//导出集群中节点信息
		Input_Output.writeClusterNodeCSV();
		//保存分布式数据
		Input_Output.writeClusterSpo();
		//保存索引表
		Input_Output.writeIndexTable();


	}
	

}
