package Partitioning;

import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Config {
	////����
	public static int database=1; //ѡ�����ݼ�
	public static int densityThreshold=8; //�ܶ���ֵ,ȡǰ�ٷ�֮ʮ�ĵ���С�ܶȡ�
	public static SimpleDateFormat df = new SimpleDateFormat("[MM-dd HH:mm:ss]");//�������ڸ�ʽ//System.out.println(df.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
   	
	////��Դ
	static public ArrayList<String> data;//��ȡ Csv
    static public Map<String, Node> subject_object = new HashMap<String, Node>();  //��+�����洢����ͱ�������ֵ������ӳ��
    static public Map<String, Integer> predication = new HashMap<String, Integer>(); //ν
	static public List<Map.Entry<String, Node>> list_so = new ArrayList<>(); //�洢����ͱ�������ֵ������ӳ��
	static public List<Map.Entry<String, Integer>> list_p = new ArrayList<>(); //���ν��hashmap��list
	
	static public Map<String,HashSet<Cluster>> Index_table=new HashMap<String,HashSet<Cluster>>();  //������
	static public List<Cluster> clusters= new ArrayList<Cluster>();	;//���ڴ洢cluster��
	static public List<HashSet<Node>> clusters_node=new ArrayList<HashSet<Node>>();//���ڴ洢ÿ��cluster�еĲ�ͬNode
	
	////��ַ
	public static String root_path=".//Database//"; //��Ŀ¼
	//ԭʼ���ݶ�ȡ��ַ
	public static String read_csv_filePath=root_path+"Database_"+database+"//"+database+".csv";//ԭʼ����csv�ļ���ַ

	//��Ϣ�����ַ
	public static String spo_store_filePath=root_path+"Database_"+database+"//Data//spo_dataset.txt";//spo��Ԫ��洢��ַ	
	public static String hashmap_store_filePath=root_path+"Database_"+database+"//Data";//�ڵ��ܶ����ݴ洢��ַ
	public static String ClusterCsvFilePath=root_path+"Database_"+database+"//Data//cluster.csv";//��Ⱥ��Ϣ
	public static String NodecsvFilePath=root_path+"Database_"+database+"//Data//node.csv";//�ڵ���Ϣ
	public static String Index_tablePath=root_path+"Database_"+database+"//Cluster//Index_table.txt";//������
	public static String ClusterPath=root_path+"Database_"+database+"//Cluster//"; //д��ͬ��Ⱥ����Ԫ��
	//��־�����ַ
	public static String cliquelog_filePath=root_path+"Database_"+database+"//Log//cliqueLog.txt"; //clique������־



}
