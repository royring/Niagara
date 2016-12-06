package kdde.niagara.ming.contrast.mdsp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class TreeNode 
{
	private String name;//�洢ö�����нڵ������
	public  Map<Integer,List<OccurSet>> occurPosSet;//�����洢���нڵ��pos���ּ��ϣ�
	 public Map<Integer,List<OccurSet>> occurNegSet;//�����洢���нڵ��neg���ּ��ϣ�

	public TreeNode()
	{	
		this.occurNegSet=new HashMap<Integer,List<OccurSet>> ();
		this.occurPosSet=new HashMap<Integer,List<OccurSet>> ();
	}
	
	public TreeNode(String name)
	{
		this.name=name;
	}
	public TreeNode(String name,Map<Integer,List<OccurSet>> occurPosSet,Map<Integer,List<OccurSet>> occurNegSet)
	{
		this.name=name;
		this.occurPosSet=occurPosSet;
		this.occurNegSet=occurNegSet;
	}
public Map<Integer, List<OccurSet>> getOccurPosSet() 
{
	return occurPosSet;
}
public void setOccurPosSet(Map<Integer, List<OccurSet>> occurPosSet)
{
	if(this.occurPosSet==null)
	{
		this.occurPosSet=new HashMap<Integer, List<OccurSet>>();
		this.occurPosSet.putAll(occurPosSet);
	}
	else
	{
		this.occurPosSet.putAll(occurPosSet);
	}
	
}
public Map<Integer, List<OccurSet>> getOccurNegSet() 
{
	return occurNegSet;
}
public void setOccurNegSet(Map<Integer, List<OccurSet>> occurNegSet) 
{
	if(this.occurNegSet==null)
	{
		this.occurNegSet=new HashMap<Integer, List<OccurSet>>();
		this.occurNegSet.putAll(occurNegSet);
	}
	else
	{
		this.occurNegSet.putAll(occurNegSet);
	}
}
public String getName() 
{
	return name;
}


public void setName(String name) 
{
	this.name = name;
}

}
