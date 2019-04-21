package Partitioning;

import java.io.BufferedWriter;
import java.util.Date;
import java.util.HashSet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
public class CliqueCluster {

	//private static List<Cluster> clusters;//���ڴ洢cluster��
	private static int clusterNo=0; //���������1��ʼ��������ֱ߽�� ������ �����clusterNo 0 �С�
	private static int nodeNo=0; //��ʾ������hashmap����
	private static List<Node> noise_Node; //����������list
	private static List<Node> border_Node; //��ű߽���list
	private static List<Node> noise_process_Node; //��������ʱ���޸��˱�ǵĽڵ㡣

	public static void do_clique() throws IOException
	{

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Config.cliquelog_filePath)));//д��־

		////////////////////��ʼ���ͺ��ĳ���////////////////////
		System.out.println("--------��ʼ����Clique����--------");
		writer.write("--------��ʼ����Clique����--------\n\n");

		initialize(writer);//��ʼ��
		search_all_hash(writer);//����hashmap

		 //////////////////////����////////////////////////////
        System.out.println("--------��ʼ����������--------");
        writer.write("\n--------��ʼ����������--------\n");

        deal_with_noise(writer);

        System.out.println("--------�����㴦�����--------\n");
        writer.write("--------�����㴦�����--------\n\n");

        //////////////////////�߽�/////////////////////////////
        System.out.println("--------��ʼ����߽��--------");
        writer.write("--------��ʼ����߽��--------\n");

        int n=deal_with_border(writer);

        System.out.println("�Ѵ���"+n+"���߽��");
        writer.write("�Ѵ���"+n+"���߽��\n");
        System.out.println("--------�߽�㴦�����--------\n");
        writer.write("--------�߽�㴦�����--------\n\n");
        /////////////////////////ͳ����Ϣ////////////////////////////

		System.out.println("--------Clique������Ϣ����--------");
		writer.write("--------Clique������Ϣ����--------\n");
		System.out.println("������ "+clusterNo+" ����");
		writer.write("������ "+clusterNo+" ����\n");
		System.out.println("������ "+nodeNo+" �����");
		writer.write("������ "+nodeNo+" �����\n");
		System.out.println("���и� "+border_Node.size()+" ����Ե���");
		writer.write("���и� "+border_Node.size()+" ����Ե���\n");
		System.out.println("Clique������־��д��"+Config.cliquelog_filePath);
		 /////////////////////����///////////////////////////////

		System.out.println("--------Clique�������--------\n");
		writer.write("--------Clique�������--------\n\n");
        writer.close();
	}

	private static void search_all_hash (BufferedWriter writer) throws IOException
	{
		Cluster clu = new Cluster(clusterNo); //��һ��������Ⱥ0��
		HashSet<Node> nod=new HashSet<Node>();// �洢����������Ⱥ�ĵ���Ϣ
		Config.clusters.add(clu);
		Config.clusters_node.add(nod);

		Iterator<Entry<String, Node>> iter = Config.subject_object.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			//Object key = entry.getKey();
			Node val = (Node) entry.getValue();//ȡ��Node��Ա
			nodeNo++;
			if (val.getVisited()==false )
			{
				if (val.getDegree()>= Config.densityThreshold)
				{
					//System.out.println(((Node) val).getDegree());
					clusterNo++; //�µļ�Ⱥ������

					writer.write("\n"+Config.df.format(new Date())+"----�ӽڵ�" + nodeNo+" "+val.getName()+"��ʼ����Clique����----\n");

					bfs(val,writer);

					writer.write(Config.df.format(new Date())+"----�ڵ�" + val.getName()+"�������----\n\n");
				}
				else if(val.getDegree()< Config.densityThreshold)
				{
					//�ܶ�û�дﵽ��ֵ�ĵ㣬 �����ܱ������ˣ�Ҳ������Ϊ������
					val.setClusterNo(0);//��ʱ��Ϊ�����㣬(������bfs�оͱ�����������)

					writer.write(Config.df.format(new Date())+"���ܶ� "+nodeNo+" ��"+val.getName()+"\n");
				}
			}
		}

	}

	private static void bfs (Node start_node,BufferedWriter writer) throws IOException
	{
		Queue<Node> q=new LinkedList<>();
		q.add(start_node);//��ʼ����������

		//�½�һ��cluster(�߱�)�Լ��͵���Ϣ�б������������ʼ����
		Cluster clu = null;
		try{
			clu= Config.clusters.get(clusterNo);
		}
		catch (IndexOutOfBoundsException e)
		{
			clu=new Cluster(clusterNo);
		}
		HashSet<Node> nod = null;
		try{
			nod= Config.clusters_node.get(clusterNo);
		}
		catch (IndexOutOfBoundsException e)
		{
			nod=new HashSet<Node>();
		}

		//��ʼBFSѭ��
	    while(!q.isEmpty())
	    {
	        Node top=q.poll();//ȡ������Ԫ��
			//����ǰ�ڵ�, ��ֻ�и��ܶȵ��ܵ�������ܴ���
			top.setVisited();
			top.setClusterNo(clusterNo);
			top.setPropertyDen();

			clu.addDensity(top);
			nod.add(top);

	        writer.write(Config.df.format(new Date())+"���� "+"��"+top.getName()+" ����"+"��Ⱥ"+clu.getClusterNo()+"\n");

	        //׼����һ���ڵ�
			List<String> list=top.getList();
			Iterator<String> it=list.iterator();
			while(it.hasNext())
			{
				String spo=it.next(); //��ǰ�ڵ�洢��һ��spo��Ϣ��
				//writer.write(Config.df.format(new Date())+"��֮�������� "+spo+"----");
				//String other=get_another(spo,start.getName());//��ȡspo���õ��뵱ǰ�ڵ���������һ���ڵ�
				String other=get_another(spo,top.getName());//��ȡspo���õ��뵱ǰ�ڵ���������һ���ڵ�
				Node linked_node=Config.subject_object.get(other); //�õ���֮�����Ľڵ��Node
				//writer.write("��"+top.getName()+"������node��  "+spo+"\n");
				if(linked_node.getVisited()==false )
				{
					//��������� �����߱����ܶȴﵽ��ֵ���͸���������еĻ���
					if ( linked_node.getDegree()==1)
					{
						//��������
						linked_node.setVisited();
						linked_node.setClusterNo(clusterNo);
						linked_node.setPropertyPro();//����

						clu.addDensity(linked_node);
						nod.add(linked_node);
						//writer.write("��һ���������\n");

				        writer.write(Config.df.format(new Date())+"���� "+"��"+linked_node.getName()+" ����"+"��Ⱥ"+clu.getClusterNo()+"\n");

					}
					else if(linked_node.getDegree()>= Config.densityThreshold)
					{
						writer.write("��"+top.getName()+"������node��  "+linked_node.getName()+"����У�\n");
						//writer.write("��һ�����ܶȽ��\n");
						//System.out.println("���� "+j+" ��"+top.getName());
						q.add(linked_node); //�����ڵ������
					}
					else {
						//��һ����׼�ı߽�㣬 ���յ��еķ�ʽ���ڸýڵ�洢����������һ������
						linked_node.setVisited();
						linked_node.setClusterNo(clusterNo);
						linked_node.setPropertyBor();

						border_Node.add(linked_node);//���� �߽���б�
						clu.addDensity(linked_node);
						nod.add(linked_node);

				        writer.write(Config.df.format(new Date())+"�߽� "+"��"+linked_node.getName()+" ����"+"��Ⱥ"+clu.getClusterNo()+"\n");
					}
				}
				else
				{
					writer.write("��"+top.getName()+"������node��  "+linked_node.getName()+"�Ǹ���ֵĵ�\n");
					//writer.write("��һ����Ե���\n");
					///�ҵ��˱߽�ڵ�������һ������ĵ㣬
					//linked_node.setPropertyBor();
					//BorderNum++;
					//System.out.println("������һ����ֵĵ㡣��");
					//�����ļ�Ⱥ�Ź�Ϊ999�� ��������
					//linked_node.setClusterNo(999);
					//�����¡��� ��ʱ���������ֵ㣬��������������ͱ߽�㴦�����
				}
			}
	      }
	    Config.clusters.add(clu); //��clu���뼯Ⱥlist��
	    Config.clusters_node.add(nod); //���ü�Ⱥ�Ķ������붥���list��
	    writer.write(Config.df.format(new Date())+"�����:"+clu.getClusterNo()+"  ����"+clu.getNodeNum()+"����\n");
	 }


	private static void deal_with_noise(BufferedWriter writer) throws IOException

	{
		collect_noise();

		int true_num=0;//�����������
		int false_num=0; //α���������

		System.out.println("����"+noise_Node.size()+"��������");
		writer.write("����"+noise_Node.size()+"��������\n\n");
		//1. �ȶ�ÿ���������ѣ��ҵ������cluster���롣

		//����������list
		Iterator<Node> it=noise_Node.iterator();
		while(it.hasNext())
		{

			Node start=it.next();
			writer.write(Config.df.format(new Date())+"����������"+start.getName()+"\n");
			//System.out.println(Config.df.format(new Date())+"����������"+start.getName());
			int n = bfs_noise(start); //n����0 ���������㣬�������Ǳ߽��
			if(n==0) {
				//��һ���µ��������㡣�޷�����
				true_num++;
			}
			else {
				false_num++;

				border_Node.add(start);
				it.remove();
			}
			start.setClusterNo(n); //��������bfs��õļ�Ⱥ��
			Config.clusters.get(n).addDensity(start);  //����cluster
			Config.clusters_node.get(n).add(start);
			writer.write(Config.df.format(new Date())+"��������ľ������"+n+"\n\n");
		//	System.out.println(Config.df.format(new Date())+"����������"+start.getName() +"����ľ������"+n);


			set_node_visited(noise_process_Node);
		}

        System.out.println("������"+true_num+"����������,�Ѵ���"+false_num+"��α������");
        writer.write("�Ѵ���"+true_num+"����������,"+false_num+"��α������\n");
		//return num;
	}

	private static int deal_with_border(BufferedWriter writer) throws IOException
	{
		int n=0;
		int true_border=0;
		int false_border=0;
		System.out.println("����"+border_Node.size()+"�����Ʊ߽��");
		writer.write("����"+border_Node.size()+"�����Ʊ߽��\n\n");
		Iterator<Node> it=border_Node.iterator();
		while (it.hasNext())
		{
			n++;
			Node start=it.next();
			boolean true_false = deal_one_border_node(start,writer); //�ж�һ�����Ʊ߽���Ƿ���Ҫ�и�
			if(true_false == false)
			{
				it.remove(); //��list�а���ɾ��
				false_border++;
			}
			else
			{
				true_border++;
			}
		}
		System.out.println("������"+true_border+"����߽��,�Ѵ���"+false_border+"��α�߽��");
		writer.write("\n��"+true_border+"����߽��,"+false_border+"��α�߽��\n");
		return n;
	}


	private static boolean deal_one_border_node(Node start,BufferedWriter writer) throws IOException
	{
		int now_node_clusterNo=start.getClusterNo();
		//int friend_is_border_Num=0;
		//int friend_is_differ_cluster_Num=0;
		boolean is_border=false;
		HashSet<Cluster> linked_clusters=new HashSet<Cluster>();//�߽�����������ʹ�õ�List
		linked_clusters.add(Config.clusters.get(start.getClusterNo())); //�Ȱѵ�ǰ����ڵ��cluster���ȥ

		//�ж��Ƿ��Ǳ߽�
		List<String> l=start.getList();
		Iterator<String> node_iter=l.iterator();
(		while(node_iter.hasNext())
		{
			String spo=node_iter.next(); //��ǰ�ڵ�洢��һ��spo��Ϣ��
			String other=get_another(spo,start.getName());//��ȡspo���õ��뵱ǰ�ڵ���������һ���ڵ�
			Node linked_node=Config.subject_object.get(other); //�õ���֮�����Ľڵ��Node
			if(linked_node.getClusterNo() != now_node_clusterNo )
			{
				//friend_is_differ_cluster_Num++;
				is_border=true;
				linked_clusters.add(Config.clusters.get(linked_node.getClusterNo()));
				Config.clusters_node.get(now_node_clusterNo).add(linked_node); //���ڵ�ǰ��Ⱥ��node���м���������Ⱥ�ı�Եnode
			}
			if(linked_node.getIsBorder()==true)
			{
				//friend_is_border_Num++;
			}
		}		)
		//�ж��Ƿ���Ҫ�и�
		if(is_border == false)
		{
			start.removeBorder();//ȡ�����ı�Ե���ƺ�
			writer.write(Config.df.format(new Date())+"�ڵ�"+start.getName()+"��α�߽��\n");
			//return false;
		}
		if (is_border==true)
		{
			start.setBorder();
			writer.write(Config.df.format(new Date())+"�ڵ�"+start.getName()+"����߽��\n");
			Config.Index_table.put(start.getName(), linked_clusters); //��������Ҫ�У��Ͱ�������������ص�cluster��������map.
			//return true;
			/*if (friend_is_border_Num!=friend_is_differ_cluster_Num) //�Ǳ߽�
			{
				start.setBorder();
				writer.write(Config.df.format(new Date())+"�ڵ�"+start.getName()+"����߽��\n");
				Config.Index_table.put(start, clu); //��������Ҫ�У��Ͱ�������������ص�cluster��������map.
				return true;
			}
			else  //α�߽�
			{
					start.removeBorder();//ȡ�����ı�Ե���ƺ�
					writer.write(Config.df.format(new Date())+"�ڵ�"+start.getName()+"�Ǳ߽��,��������Ҫ�и�\n");
					return false;
			}*/
		}
		return is_border; //û��Ҫ�ӡ������ǲ������ᱨ��OTZ
	}


	private static void collect_noise()

	{
		/***
		 * �������������һ�����б���ֻ��ȡ�������ڵ㡣
		 * or �����ڵ㲻һ��Ҫ���������list����bfsѭ���оͿ���ʵ����
		 */
		Iterator<Entry<String, Node>> iter = Config.subject_object.entrySet().iterator();
		while (iter.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			//Object key = entry.getKey();
			Node val = (Node) entry.getValue();//ȡ��Node��Ա

			if(val.getClusterNo()==0)
			{
				val.setVisited(); //���������еĽڵ㶼�����true.
				noise_Node.add(val);
			}
		}
	}

	private static  void set_node_visited(List <Node> nodes)
	{
		Iterator<Node> it=nodes.iterator();
		while(it.hasNext())
		{
			Node process_Node=it.next();
			process_Node.setVisited();
		}
	}


	private static int bfs_noise(Node start_node)
	{
		//����������bfs���ҵ�������о���ŵĽڵ㣬���ظþ���š�
		Queue<Node> q=new LinkedList<>();
		q.add(start_node);//��ʼ����������
	    while(!q.isEmpty())
	    {
	        Node top=q.poll();//ȡ������Ԫ��
	        top.setUnVisited();//�����ǣ�����Ҫ���������㣬�������㶼��false�ġ������о���ŵĽڵ㶼��true�����Է��ʵ�trueʱ�������Ϊfalse�����ˡ�
	        noise_process_Node.add(top);
	        if (top.getClusterNo() != 0)
			{
				return top.getClusterNo();
			}
	        //׼����һ���ڵ�
			List<String> list=top.getList();
			Iterator<String> it=list.iterator();
			while(it.hasNext())
			{
				String spo=it.next(); //��ǰ�ڵ�洢��һ��spo��Ϣ��
				String other=get_another(spo,start_node.getName());//��ȡspo���õ��뵱ǰ�ڵ���������һ���ڵ�
				Node linked_node=Config.subject_object.get(other); //�õ���֮�����Ľڵ��Node
				if(linked_node.getVisited()==true) //�����Ǵ󷨺ã�
				{
					q.add(linked_node);
				}
				//q.add(linked_node);
			}
	      }
	    return 0; //����������һ�������ĵ㣬�͹���0���С�
	}

	public static String get_another(String spo,String match)
	{
	/***
	 * ��֪spo �����е�s��o��������һ������
	 */
		List<String> result = Arrays.asList(spo.split(","));
		//��ȡspo��Ԫ�顣 ���´���������ݼ����ݽ����޸�
		Iterator<String>list_it=result.iterator();
		String s=list_it.next();
		list_it.next();//����Ӣ��label
		String o=list_it.next();

		if(s.equals(match))
			return o;
		else
			return s;
	}


	private static void initialize(BufferedWriter writer) throws IOException
	{

		//Ϊnoise_node��һ��list
		noise_Node=new ArrayList<Node>();
		//Ϊborder_node��һ��list
		border_Node=new ArrayList<Node>();

		noise_process_Node=new ArrayList<Node>();

		//�����ܶ���ֵ
		//int num=Config.list_so.get(Config.list_so.size()/10).getValue().getDegree();
		//Config.densityThreshold = num>1 ? num:2;
		System.out.println("�ܶ���ֵ = "+Config.densityThreshold);
		writer.write(Config.df.format(new Date())+"�ܶ���ֵ = "+Config.densityThreshold+"\n\n");
		writer.write("----------------------------\n");

	}

}
