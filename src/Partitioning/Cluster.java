package Partitioning;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class Cluster {
    private int clusterNo;   //cluster编号
    private int density;     //密度
    private int nodeNum;
    private HashSet<String> spo;
    public Cluster(int i){
        clusterNo = i;
        density = 0;
        nodeNum=0;
        spo=new HashSet<String>();
    }

	public String print()
	{
		return getClusterNo()+","+ getDensity()+","+getNodeNum();
	}

    public void addDensity(Node n){
    	/***
    	 cluster密度增加
    	 */
    	List<String> spo_list=n.getList();
		Iterator<String> stringList=spo_list.iterator();
		while(stringList.hasNext())
		{
			spo.add(stringList.next());
		}
        density +=n.getDegree();
        nodeNum++;
        }



    public HashSet<String> getSpo()
    {
    	return spo;
    }
    public String getClusterName()
    {
    	return "cluster_"+clusterNo;
    }
    public int getNodeNum()
    {
    	return nodeNum;
    }
    public int getClusterNo(){
        return clusterNo;
    }


    public int getDensity(){
        return density;
    }


}
