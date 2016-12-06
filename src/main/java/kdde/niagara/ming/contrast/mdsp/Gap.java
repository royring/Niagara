package kdde.niagara.ming.contrast.mdsp;

public class Gap implements Comparable<Object>{
  public Integer minGap;
  public Integer maxGap;
  public Gap(Integer minGap,Integer maxGap)
  {
	  this.minGap=minGap;
	  this.maxGap=maxGap;
  } 
  @Override  
  public boolean equals(Object gap1) {  
	  Gap gap=(Gap)gap1;
	  if((this.minGap>gap.minGap)||(this.minGap==gap.minGap&&this.maxGap>gap.maxGap))return false;
		else if((this.minGap<gap.minGap)||(this.minGap==gap.minGap&&this.maxGap<gap.maxGap)) 
		{
			return false;
		}
		else
			return true;
//		if((this.maxGap-this.minGap>gap.maxGap-gap.minGap)||(this.maxGap-this.minGap==gap.maxGap-gap.minGap&&this.minGap-gap.minGap>0))
//	    {
//	   	 return false;
//	    }
//	    else if((this.maxGap-this.minGap<gap.maxGap-gap.minGap)||(this.maxGap-this.minGap==gap.maxGap-gap.minGap&&this.minGap-gap.minGap<0))
//	    {
//	   	 return false;
//	    } 
//	    else 
//	    {
//	    	return true;
//	    }
  }  

  @Override  
  public int hashCode() {  
      int result = 17;  
      result = result * 31 + minGap.hashCode();  
      result = result * 31 + maxGap.hashCode();  
      return result;  
  }
@Override
public int compareTo(Object gap1) {
	// TODO Auto-generated method stub
	Gap gap=(Gap)gap1;
	if((this.minGap>gap.minGap)||(this.minGap==gap.minGap&&this.maxGap>gap.maxGap))return 1;
	else if((this.minGap<gap.minGap)||(this.minGap==gap.minGap&&this.maxGap<gap.maxGap)) 
	{
		return -1;
	}
	else
		return 0;
	
	
	
//	if((this.maxGap-this.minGap>gap.maxGap-gap.minGap)||(this.maxGap-this.minGap==gap.maxGap-gap.minGap&&this.minGap-gap.minGap>0))
//    {
//   	 return 1;
//    }
//    else if((this.maxGap-this.minGap<gap.maxGap-gap.minGap)||(this.maxGap-this.minGap==gap.maxGap-gap.minGap&&this.minGap-gap.minGap<0))
//    {
//   	 return -1;
//    } 
//    else 
//    {
//    	return 0;
//    }
	
}  
}
