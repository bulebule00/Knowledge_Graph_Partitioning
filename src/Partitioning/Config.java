package Partitioning;

import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Config {
	////参数
	public static int database=1; //选择数据集
	public static int densityThreshold=8; //密度阈值,取前百分之十的点最小密度。
	public static SimpleDateFormat df = new SimpleDateFormat("[MM-dd HH:mm:ss]");//设置日期格式//System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
   	
	////资源
	static public ArrayList<String> data;//读取 Csv
    static public Map<String, Node> subject_object = new HashMap<String, Node>();  //主+宾，存储主语和宾语的名字到点类的映射
    static public Map<String, Integer> predication = new HashMap<String, Integer>(); //谓
	static public List<Map.Entry<String, Node>> list_so = new ArrayList<>(); //存储主语和宾语的名字到点类的映射
	static public List<Map.Entry<String, Integer>> list_p = new ArrayList<>(); //存放谓语hashmap的list
	
	static public Map<Node,HashSet<Cluster>> Index_table=new HashMap<Node,HashSet<Cluster>>();  //索引表
	static public List<Cluster> clusters= new ArrayList<Cluster>();	;//用于存储cluster。
	static public List<HashSet<Node>> clusters_node=new ArrayList<HashSet<Node>>();//用于存储每个cluster中的不同Node
	
	////地址
	public static String root_path="C://Users//liu01//Desktop//red_building//"; //根目录
	//原始数据读取地址
	public static String read_csv_filePath=root_path+"Database_"+database+"//"+database+".csv";//原始数据csv文件地址
	//日志保存地址
	public static String spo_store_filePath=root_path+"Database_"+database+"//Log//spo_dataset.txt";//spo三元组存储地址	
	public static String hashmap_store_filePath=root_path+"Database_"+database+"//Log";//节点密度数据存储地址
	public static String cliquelog_filePath=root_path+"Database_"+database+"//Log//cliqueLog.txt"; //clique聚类日志
	public static String ClusterCsvFilePath=root_path+"Database_"+database+"//Log//cluster.csv";//集群信息
	//信息保存地址
	public static String NodecsvFilePath=root_path+"Database_"+database+"//Data//node.csv";//节点信息
	public static String Index_tablePath=root_path+"Database_"+database+"//Data//Index_table.txt";//索引表
	public static String ClusterPath=root_path+"Database_"+database+"//Cluster//"; //写不同集群的三元组
	


}
