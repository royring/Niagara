package kdde.niagara.ming.contrast.mdsp;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
public class Initilize 
{
   //private List<List<String>> posSet;//����pos���ϣ�
  // private List<List<String>>negSet;//����neg���ϣ�
   
   public Initilize()
   {
	   
   }
  
 /**
 * ��ȡ�ļ��ļ�¼��
 * @param fileName
 * @return
 */
   public List<List<String>> readFile(String fileName)
   {
	   List<List<String>>set=new ArrayList<List<String>>();
	   if(fileName.trim().length()>0)
	   {		  
		   try
		   {			  
			   BufferedReader br=new BufferedReader(new FileReader(fileName));
			   String line;
			   List<String> record=null;
			   while((line=br.readLine())!=null)
			   {
				   if(line.trim().length()>0)
				   {
					   record=new ArrayList<String>();
					   char[]string=line.toCharArray();
					   for(char str:string )
					   {
						   record.add(str+"");				   
					   }
					   set.add(record);			   
				   }
			   }
		   }
		   catch(Exception e)
		   {
			   e.printStackTrace();
		   }		  
	   }
	   return set;
   }
}
