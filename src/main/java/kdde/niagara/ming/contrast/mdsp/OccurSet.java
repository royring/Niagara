package kdde.niagara.ming.contrast.mdsp;
import java.util.ArrayList;
import java.util.List;


public class OccurSet {
 public int occurNumber;
 public List<int[]> maxOccur;
 OccurSet()
 {
	 
 }
 OccurSet(int occurNumber)
 {
	 this.occurNumber=occurNumber;
	 maxOccur=new ArrayList<int[]>();
 }
public void setMaxOccur(List<int[]> maxOccur) {
	this.maxOccur = maxOccur;
}

 
}
