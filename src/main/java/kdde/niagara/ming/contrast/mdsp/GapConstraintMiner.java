package kdde.niagara.ming.contrast.mdsp;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import kdde.niagara.mining.Miner;
import kdde.niagara.mining.Param;
public class GapConstraintMiner implements Miner
{
	public GapConstraintMiner() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<String>  mine(Param param) 
	{
		long t0 = System.nanoTime();
		for(int j=0;j<1;j++)
		{
			String posFile = param.getFile1().getAbsolutePath();// "./cbi_pos.txt";   // "eColi_pos.txt"; // "tatCD_neg.txt"; //     
			String negFile = param.getFile2().getAbsolutePath();// "./cbi_neg.txt";   // "eColi_neg.txt"; // "tatCD_pos.txt"; //  
			double posSup = param.getAlpha();
			double negSup = param.getBetal();
			FindGap gcMiner = new FindGap(posFile, negFile, posSup, negSup);		
			/* ��¼����(����֧�ֶ�>=posSup)�ĳ���λ��  */
			Map<String, Map<Integer, List<OccurSet>>> oneItemSet = gcMiner.constructItemPositionIndex();	
//            Iterator<String> iterStr=oneItemSet.keySet().iterator();
//            while(iterStr.hasNext())
//            {
//            	System.out.print(iterStr.next()+"  ");
//            }
//            System.out.println();
		    gcMiner.formEnumerationTree(oneItemSet);
		    //��ȥ��С��
		    long t1 = System.nanoTime();
			long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
			String timeCost = String.format("time took: %d ms", millis);
			String timeCostM = String.format("time took: %s m", String.valueOf(millis / 60000.0));
			System.out.println(timeCost);
			System.out.println(timeCostM);
		   FindGap.result.add(timeCost);
		   FindGap.result.add(timeCostM);
//		   System.out.println("��ʼ��ʱ�䣺   "+gcMiner.time1);
//		   System.out.println("�ھ�gap��Ҫ��ʱ�䣺   "+gcMiner.time2);
//		   System.out.println("����ָ����ƶ���ɾ����Ҫ��ʱ�䣺  "+gcMiner.time3);
//		   System.out.println("ÿ�αȽϲ�����Сgap�ķѵ�ʱ�䣺  "+gcMiner.time4);
//		   System.out.println("�����ʱ�䣺  "+gcMiner.time5);
		}	  
	   //System.out.println("�㷨����ʱ��"+df.format(new Date()));	  
		return FindGap.result;
	}
}
