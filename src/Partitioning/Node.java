package Partitioning;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private boolean visited;//�Ƿ���ʹ�
	private int degree; //Node�Ķ�
	private int clusterNo;     // �������0��ʾ��ɢ��
	private String name; //�ڵ�洢��ʵ�������
	private List<String> list; //����ڵ����Ԫ��
	private boolean isBorder;
	private String property;//�ڵ������ �� ���ܶȡ��������������߽� ����
	
	Node(String n)
	{
		name=n;
		visited=false;
		degree=1;
		clusterNo=0;
		isBorder=false;
		list=new ArrayList<>();
		property="����";
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
		property=new String("���ܶ�");
	}
	public void setPropertyPro()
	{
		property=new String("����");
	}
	public void setPropertyNoi()
	{
		property=new String("����");
	}
	public void setPropertyBor()
	{
		property=new String("�߽�");
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
