package Partitioning;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private boolean visited;//是否访问过
	private int degree; //Node的度
	private int clusterNo;     // 所属类别，0表示离散点
	private String name; //节点存储的实体的名称
	private List<String> list; //这个节点的三元组
	private boolean isBorder;
	private String property;//节点的属性 有 高密度、保护、噪声、边界 三种
	
	Node(String n)
	{
		name=n;
		visited=false;
		degree=1;
		clusterNo=0;
		isBorder=false;
		list=new ArrayList<>();
		property="噪声";
	}
	
	
	public void add_spo(String spo)
	{
		list.add(spo);
	}
	public void visit()
	{
		visited=true;
	}
	public void setPropertyDen()
	{
		property=new String("高密度");
	}
	public void setPropertyPro()
	{
		property=new String("保护");
	}
	public void setPropertyNoi()
	{
		property=new String("噪声");
	}
	public void setPropertyBor()
	{
		property=new String("边界");
	}
	public void setBorder()
	{
		isBorder=true;
	}
	public void removeBorder()
	{
		isBorder=false;
	}
	public void setVisited()
	{
		visited=true;
	}
	public void setUnVisited()
	{
		visited=false;
	}
	public void setDegree(int d)
	{
		degree=d;
	}

    public void setClusterNo(int i){
        clusterNo = i;
    }
    
    
    public List<String> getList()
    {
    	return list;
    }
	public int getDegree()
	{
		return degree;
	}
	public String getName()
	{
		return name;
	}
	public boolean getVisited()
	{
		return visited;
	}
	public int getClusterNo()
	{
		return clusterNo;
	}
	public boolean getIsBorder()
	{
		return isBorder;
	}
	public String getProperty()
	{
		return property;
	}

	public String print()
	{
		return getName()+","+getClusterNo()+","+getProperty()+","+getDegree()+","+getVisited()+","+getIsBorder();
	}
	
	
}
