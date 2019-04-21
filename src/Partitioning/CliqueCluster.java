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

	//private static List<Cluster> clusters;//用于存储cluster。
	private static int clusterNo=0; //正常情况从1开始，如果出现边界点 噪声点 则归在clusterNo 0 中。
	private static int nodeNo=0; //表示遍历的hashmap次序。
	private static List<Node> noise_Node; //存放噪声点的list
	private static List<Node> border_Node; //存放边界点的list
	private static List<Node> noise_process_Node; //处理噪声时被修改了标记的节点。

	public static void do_clique() throws IOException
	{

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Config.cliquelog_filePath)));//写日志

		////////////////////初始化和核心程序////////////////////
		System.out.println("--------开始进行Clique聚类--------");
		writer.write("--------开始进行Clique聚类--------\n\n");

		initialize(writer);//初始化
		search_all_hash(writer);//遍历hashmap

		 //////////////////////噪声////////////////////////////
        System.out.println("--------开始处理噪声点--------");
        writer.write("\n--------开始处理噪声点--------\n");

        deal_with_noise(writer);

        System.out.println("--------噪声点处理结束--------\n");
        writer.write("--------噪声点处理结束--------\n\n");

        //////////////////////边界/////////////////////////////
        System.out.println("--------开始处理边界点--------");
        writer.write("--------开始处理边界点--------\n");

        int n=deal_with_border(writer);

        System.out.println("已处理"+n+"个边界点");
        writer.write("已处理"+n+"个边界点\n");
        System.out.println("--------边界点处理结束--------\n");
        writer.write("--------边界点处理结束--------\n\n");
        /////////////////////////统计信息////////////////////////////

		System.out.println("--------Clique聚类信息如下--------");
		writer.write("--------Clique聚类信息如下--------\n");
		System.out.println("共产生 "+clusterNo+" 个类");
		writer.write("共产生 "+clusterNo+" 个类\n");
		System.out.println("共遍历 "+nodeNo+" 个结点");
		writer.write("共遍历 "+nodeNo+" 个结点\n");
		System.out.println("需切割 "+border_Node.size()+" 个边缘结点");
		writer.write("需切割 "+border_Node.size()+" 个边缘结点\n");
		System.out.println("Clique聚类日志已写入"+Config.cliquelog_filePath);
		 /////////////////////结束///////////////////////////////

		System.out.println("--------Clique聚类结束--------\n");
		writer.write("--------Clique聚类结束--------\n\n");
        writer.close();
	}

	private static void search_all_hash (BufferedWriter writer) throws IOException
	{
		Cluster clu = new Cluster(clusterNo); //建一个噪声集群0。
		HashSet<Node> nod=new HashSet<Node>();// 存储属于噪声集群的点信息
		Config.clusters.add(clu);
		Config.clusters_node.add(nod);

		Iterator<Entry<String, Node>> iter = Config.subject_object.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			//Object key = entry.getKey();
			Node val = (Node) entry.getValue();//取出Node成员
			nodeNo++;
			if (val.getVisited()==false )
			{
				if (val.getDegree()>= Config.densityThreshold)
				{
					//System.out.println(((Node) val).getDegree());
					clusterNo++; //新的集群出现了

					writer.write("\n"+Config.df.format(new Date())+"----从节点" + nodeNo+" "+val.getName()+"开始进行Clique聚类----\n");

					bfs(val,writer);

					writer.write(Config.df.format(new Date())+"----节点" + val.getName()+"聚类结束----\n\n");
				}
				else if(val.getDegree()< Config.densityThreshold)
				{
					//密度没有达到阈值的点， （可能被保护了，也可能作为噪声）
					val.setClusterNo(0);//暂时设为噪声点，(可能在bfs中就被保护起来了)

					writer.write(Config.df.format(new Date())+"低密度 "+nodeNo+" ："+val.getName()+"\n");
				}
			}
		}

	}

	private static void bfs (Node start_node,BufferedWriter writer) throws IOException
	{
		Queue<Node> q=new LinkedList<>();
		q.add(start_node);//起始顶点加入队列

		//新建一个cluster(边表)以及和点信息列表（顶点表），并初始化。
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

		//开始BFS循环
	    while(!q.isEmpty())
	    {
	        Node top=q.poll();//取出队首元素
			//处理当前节点, （只有高密度点能到这里接受处理）
			top.setVisited();
			top.setClusterNo(clusterNo);
			top.setPropertyDen();

			clu.addDensity(top);
			nod.add(top);

	        writer.write(Config.df.format(new Date())+"高密 "+"："+top.getName()+" 加入"+"集群"+clu.getClusterNo()+"\n");

	        //准备下一批节点
			List<String> list=top.getList();
			Iterator<String> it=list.iterator();
			while(it.hasNext())
			{
				String spo=it.next(); //当前节点存储的一条spo信息。
				//writer.write(Config.df.format(new Date())+"与之相连的有 "+spo+"----");
				//String other=get_another(spo,start.getName());//读取spo，得到与当前节点相连的另一个节点
				String other=get_another(spo,top.getName());//读取spo，得到与当前节点相连的另一个节点
				Node linked_node=Config.subject_object.get(other); //得到与之相连的节点的Node
				//writer.write("与"+top.getName()+"相连的node：  "+spo+"\n");
				if(linked_node.getVisited()==false )
				{
					//如果被保了 ，或者本身密度达到阈值，就给他加入队列的机会
					if ( linked_node.getDegree()==1)
					{
						//保护保护
						linked_node.setVisited();
						linked_node.setClusterNo(clusterNo);
						linked_node.setPropertyPro();//保护

						clu.addDensity(linked_node);
						nod.add(linked_node);
						//writer.write("是一个保护结点\n");

				        writer.write(Config.df.format(new Date())+"保护 "+"："+linked_node.getName()+" 加入"+"集群"+clu.getClusterNo()+"\n");

					}
					else if(linked_node.getDegree()>= Config.densityThreshold)
					{
						writer.write("与"+top.getName()+"相连的node：  "+linked_node.getName()+"入队列！\n");
						//writer.write("是一个高密度结点\n");
						//System.out.println("遍历 "+j+" ："+top.getName());
						q.add(linked_node); //附近节点入队列
					}
					else {
						//是一个标准的边界点， 按照点切的方式，在该节点存储，但不做进一步遍历
						linked_node.setVisited();
						linked_node.setClusterNo(clusterNo);
						linked_node.setPropertyBor();

						border_Node.add(linked_node);//加入 边界点列表
						clu.addDensity(linked_node);
						nod.add(linked_node);

				        writer.write(Config.df.format(new Date())+"边界 "+"："+linked_node.getName()+" 加入"+"集群"+clu.getClusterNo()+"\n");
					}
				}
				else
				{
					writer.write("与"+top.getName()+"相连的node：  "+linked_node.getName()+"是个奇怪的点\n");
					//writer.write("是一个边缘结点\n");
					///找到了边界节点属于另一个聚类的点，
					//linked_node.setPropertyBor();
					//BorderNum++;
					//System.out.println("发现了一个奇怪的点。。");
					//把他的集群号归为999， 不处理它
					//linked_node.setClusterNo(999);
					//【最新】： 暂时不处理这种点，留给后面的噪声和边界点处理程序。
				}
			}
	      }
	    Config.clusters.add(clu); //将clu加入集群list中
	    Config.clusters_node.add(nod); //将该集群的顶点表加入顶点表list中
	    writer.write(Config.df.format(new Date())+"聚类号:"+clu.getClusterNo()+"  共有"+clu.getNodeNum()+"个点\n");
	 }


	private static void deal_with_noise(BufferedWriter writer) throws IOException

	{
		collect_noise();

		int true_num=0;//真噪声点个数
		int false_num=0; //伪噪声点个数

		System.out.println("共有"+noise_Node.size()+"个噪声点");
		writer.write("共有"+noise_Node.size()+"个噪声点\n\n");
		//1. 先对每个点做深搜，找到最近的cluster加入。

		//遍历噪声点list
		Iterator<Node> it=noise_Node.iterator();
		while(it.hasNext())
		{

			Node start=it.next();
			writer.write(Config.df.format(new Date())+"处理噪声点"+start.getName()+"\n");
			//System.out.println(Config.df.format(new Date())+"处理噪声点"+start.getName());
			int n = bfs_noise(start); //n不是0 则不是噪音点，但可能是边界点
			if(n==0) {
				//是一个孤岛的噪声点。无法处理
				true_num++;
			}
			else {
				false_num++;

				border_Node.add(start);
				it.remove();
			}
			start.setClusterNo(n); //给他设置bfs获得的集群号
			Config.clusters.get(n).addDensity(start);  //更新cluster
			Config.clusters_node.get(n).add(start);
			writer.write(Config.df.format(new Date())+"距离最近的聚类号是"+n+"\n\n");
		//	System.out.println(Config.df.format(new Date())+"距离噪声点"+start.getName() +"最近的聚类号是"+n);


			set_node_visited(noise_process_Node);
		}

        System.out.println("其中有"+true_num+"个真噪声点,已处理"+false_num+"个伪噪声点");
        writer.write("已处理"+true_num+"个真噪声点,"+false_num+"个伪噪声点\n");
		//return num;
	}

	private static int deal_with_border(BufferedWriter writer) throws IOException
	{
		int n=0;
		int true_border=0;
		int false_border=0;
		System.out.println("共有"+border_Node.size()+"个疑似边界点");
		writer.write("共有"+border_Node.size()+"个疑似边界点\n\n");
		Iterator<Node> it=border_Node.iterator();
		while (it.hasNext())
		{
			n++;
			Node start=it.next();
			boolean true_false = deal_one_border_node(start,writer); //判断一个疑似边界点是否需要切割
			if(true_false == false)
			{
				it.remove(); //从list中把他删除
				false_border++;
			}
			else
			{
				true_border++;
			}
		}
		System.out.println("其中有"+true_border+"个真边界点,已处理"+false_border+"个伪边界点");
		writer.write("\n有"+true_border+"个真边界点,"+false_border+"个伪边界点\n");
		return n;
	}


	private static boolean deal_one_border_node(Node start,BufferedWriter writer) throws IOException
	{
		int now_node_clusterNo=start.getClusterNo();
		//int friend_is_border_Num=0;
		//int friend_is_differ_cluster_Num=0;
		boolean is_border=false;
		HashSet<Cluster> linked_clusters=new HashSet<Cluster>();//边界点的索引表中使用的List
		linked_clusters.add(Config.clusters.get(start.getClusterNo())); //先把当前这个节点的cluster存进去

		//判断是否是边界
		List<String> l=start.getList();
		Iterator<String> node_iter=l.iterator();
(		while(node_iter.hasNext())
		{
			String spo=node_iter.next(); //当前节点存储的一条spo信息。
			String other=get_another(spo,start.getName());//读取spo，得到与当前节点相连的另一个节点
			Node linked_node=Config.subject_object.get(other); //得到与之相连的节点的Node
			if(linked_node.getClusterNo() != now_node_clusterNo )
			{
				//friend_is_differ_cluster_Num++;
				is_border=true;
				linked_clusters.add(Config.clusters.get(linked_node.getClusterNo()));
				Config.clusters_node.get(now_node_clusterNo).add(linked_node); //就在当前集群的node表中加入其他集群的边缘node
			}
			if(linked_node.getIsBorder()==true)
			{
				//friend_is_border_Num++;
			}
		}		)
		//判断是否需要切割
		if(is_border == false)
		{
			start.removeBorder();//取消他的边缘结点称号
			writer.write(Config.df.format(new Date())+"节点"+start.getName()+"是伪边界点\n");
			//return false;
		}
		if (is_border==true)
		{
			start.setBorder();
			writer.write(Config.df.format(new Date())+"节点"+start.getName()+"是真边界点\n");
			Config.Index_table.put(start.getName(), linked_clusters); //如果这个点要切，就把这个点和与他相关的cluster加入索引map.
			//return true;
			/*if (friend_is_border_Num!=friend_is_differ_cluster_Num) //是边界
			{
				start.setBorder();
				writer.write(Config.df.format(new Date())+"节点"+start.getName()+"是真边界点\n");
				Config.Index_table.put(start, clu); //如果这个点要切，就把这个点和与他相关的cluster加入索引map.
				return true;
			}
			else  //伪边界
			{
					start.removeBorder();//取消他的边缘结点称号
					writer.write(Config.df.format(new Date())+"节点"+start.getName()+"是边界点,但他不需要切割\n");
					return false;
			}*/
		}
		return is_border; //没必要加。。但是不加他会报错OTZ
	}


	private static void collect_noise()

	{
		/***
		 * 可以在这里遍历一次所有表，不只是取出噪声节点。
		 * or 噪声节点不一定要在这里加入list，在bfs循环中就可以实现了
		 */
		Iterator<Entry<String, Node>> iter = Config.subject_object.entrySet().iterator();
		while (iter.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			//Object key = entry.getKey();
			Node val = (Node) entry.getValue();//取出Node成员

			if(val.getClusterNo()==0)
			{
				val.setVisited(); //这样，所有的节点都标成了true.
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
		//对噪声点做bfs，找到最近的有聚类号的节点，返回该聚类号。
		Queue<Node> q=new LinkedList<>();
		q.add(start_node);//起始顶点加入队列
	    while(!q.isEmpty())
	    {
	        Node top=q.poll();//取出队首元素
	        top.setUnVisited();//反向标记，不需要访问噪声点，而噪声点都是false的。其他有聚类号的节点都是true。所以访问到true时候把他设为false就行了。
	        noise_process_Node.add(top);
	        if (top.getClusterNo() != 0)
			{
				return top.getClusterNo();
			}
	        //准备下一批节点
			List<String> list=top.getList();
			Iterator<String> it=list.iterator();
			while(it.hasNext())
			{
				String spo=it.next(); //当前节点存储的一条spo信息。
				String other=get_another(spo,start_node.getName());//读取spo，得到与当前节点相连的另一个节点
				Node linked_node=Config.subject_object.get(other); //得到与之相连的节点的Node
				if(linked_node.getVisited()==true) //反向标记大法好！
				{
					q.add(linked_node);
				}
				//q.add(linked_node);
			}
	      }
	    return 0; //如果他真的是一个独立的点，就归在0类中。
	}

	public static String get_another(String spo,String match)
	{
	/***
	 * 已知spo 和其中的s或o，返回另一个本体
	 */
		List<String> result = Arrays.asList(spo.split(","));
		//读取spo三元组。 以下代码根据数据集内容进行修改
		Iterator<String>list_it=result.iterator();
		String s=list_it.next();
		list_it.next();//跳过英文label
		String o=list_it.next();

		if(s.equals(match))
			return o;
		else
			return s;
	}


	private static void initialize(BufferedWriter writer) throws IOException
	{

		//为noise_node建一个list
		noise_Node=new ArrayList<Node>();
		//为border_node建一个list
		border_Node=new ArrayList<Node>();

		noise_process_Node=new ArrayList<Node>();

		//计算密度阈值
		//int num=Config.list_so.get(Config.list_so.size()/10).getValue().getDegree();
		//Config.densityThreshold = num>1 ? num:2;
		System.out.println("密度阈值 = "+Config.densityThreshold);
		writer.write(Config.df.format(new Date())+"密度阈值 = "+Config.densityThreshold+"\n\n");
		writer.write("----------------------------\n");

	}

}
