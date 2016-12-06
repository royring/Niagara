package kdde.niagara.ming.contrast.mdsp;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

public class FindGap {
	public String posFile;
	public String negFile;
	public double posSup;
	public double negSup;
	public double posSetLength;
	public double negSetLength;
	public boolean flag;
	public Map<String, int[]> Item; /* ������������������������ֺ�����ʵ�gap�� */
	List<String> linkItem = new LinkedList<String>();
	public static List<String> result = new LinkedList<String>();
	/**
	 * �ҵ�����������һ������ǵĳ��ֵļ��ϣ�
	 * 
	 * @return
	 */
	public FindGap(String posFile, String negFile, double posSup, double negSup) {
		this.posFile = posFile;
		this.negFile = negFile;
		this.posSup = posSup;
		this.negSup = negSup;
		this.Item = new TreeMap<String, int[]>();
	}

	/* �ҵ�һ������г��� */
	public Map<String, Map<Integer, List<OccurSet>>> constructItemPositionIndex() {
		/* record the occurrences (seqId, {posId}) of each item */
		Map<String, Map<Integer, List<OccurSet>>> itemPositionIndex = new TreeMap<String, Map<Integer, List<OccurSet>>>();
		Initilize initilize = new Initilize();
		List<List<String>> posSet = initilize.readFile(this.posFile);
		List<List<String>> negSet = initilize.readFile(this.negFile);
		this.posSetLength = posSet.size();/* pos���ݼ��ĳ��� */
		this.negSetLength = negSet.size();/* neg���ݼ��ĳ��� */
		/* ����pos���ݼ��ҵ�����������һ� */
		Map<Integer, List<OccurSet>> occurSet = null;/* ��������ĳ��ĳ��� */
		List<OccurSet> occur = null;
		int seqId = 1;/* ����ͳ�Ƶ�ǰ�ǵڼ������� */
		for (List<String> sequence : posSet) {
			int positionId = 1;/* ������¼һ��һ���һ�������г��ֵ�λ�ã� */
			for (String element : sequence) {
				/* ���one_Item_Set�а�����str */
				if (itemPositionIndex.containsKey(element)) {
					/* ���str�ĳ��ּ����а����е�count1�����У�����b��s1�еĳ���{1,{2, 3, 7}} */
					if (itemPositionIndex.get(element).containsKey(seqId)) {
						OccurSet oSet = new OccurSet(positionId);
						itemPositionIndex.get(element).get(seqId).add(oSet);
					}
					/* ���str�ĳ��ּ����в������е�count1������ */
					else {
						occur = new ArrayList<OccurSet>();
						OccurSet oSet = new OccurSet(positionId);
						occur.add(oSet);
						itemPositionIndex.get(element).put(seqId, occur);
					}
				}
				/* ���one_Item_Set�в�������str */
				else {
					occur = new ArrayList<OccurSet>();
					OccurSet oSet = new OccurSet(positionId);
					occur.add(oSet);
					occurSet = new HashMap<Integer, List<OccurSet>>();
					occurSet.put(seqId, occur);
					itemPositionIndex.put(element, occurSet);
				}
				positionId++;
			}
			seqId++;
		}
		/* �ж����ɵ�һ����Ƿ�����pos֧�ֶȵ�Ҫ�����������ֱ�Ӽ����� */
		checkPosSupThreshold(itemPositionIndex, posSup, this.posSetLength);
		/* ����neg���ݼ��ҵ�����pos��֧�ֶȵ�һ��ĳ��֣� */
		for (List<String> list : negSet) {
			int positionId = 1;
			for (String str : list) {
				OccurSet os = null;
				if (itemPositionIndex.containsKey(str)) {
					if (itemPositionIndex.get(str).containsKey(seqId)) {
						/* ���str�ĳ��ּ����а����е�count1������ */
						os = new OccurSet(positionId);
						itemPositionIndex.get(str).get(seqId).add(os);
					}
					/* ���str�ĳ��ּ����в������е�count1������ */
					else {
						occur = new ArrayList<OccurSet>();
						os = new OccurSet(positionId);
						occur.add(os);
						itemPositionIndex.get(str).put(seqId, occur);
					}
				}
				positionId++;
			}
			seqId++;
		}
		return itemPositionIndex;
	}

	/**
	 * �ж��Ƿ�����֧�ֶ���ֵ����
	 * 
	 * @param index
	 * @param threshole
	 */
	public <U> void checkPosSupThreshold(Map<String, Map<Integer, U>> index, double posSup, double length) {
		Iterator<String> iter = index.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (index.get(key).keySet().size() / length < posSup) {
				// System.out.println("�Ƴ�"+key);
				iter.remove();
			}
		}
		return;
	}

	/**
	 * 
	 * @param oneItemSet;
	 * @return
	 */
	public void findMoreLengthItem(Stack<TreeNode> sk, Map<String, Map<Integer, List<OccurSet>>> oneItemSet) {
		// ���ж���û�������д���
		TreeNode node = sk.pop();
		// System.out.println(node.getName()+"-------->��ջ");
		// if(node.getName().compareTo("c")==0)
		// {
		// System.out.println(node.getName());
		// }
		Iterator<String> oneItemSetIter = oneItemSet.keySet().iterator();
		while (oneItemSetIter.hasNext()) {
			String str2 = oneItemSetIter.next();
			TreeNode newNode = new TreeNode(node.getName() + str2);
			// ���û��������
			if (!compareTowItem(newNode.getName(), linkItem)) {
				Map<Integer, List<OccurSet>> newPosOccurSet = getOccurSet(node.getOccurPosSet(), oneItemSet.get(str2));
				/////
				if (newPosOccurSet.size() / this.posSetLength >= this.posSup) {
					newNode.setOccurPosSet(newPosOccurSet);
					Map<Integer, List<OccurSet>> newNegOccurSet = getOccurSet(node.getOccurNegSet(),
							oneItemSet.get(str2));
					if (newNegOccurSet != null)
						newNode.setOccurNegSet(newNegOccurSet);
					int[] gap = null;
					/* �����������������gap���������Item�У�����������ջ�� */
					this.flag = false;
					if ((gap = getGap(newNode)) != null) {
						if (linkItem.size() == 0) {
							linkItem.add(newNode.getName());
						} else {
							for (int i = 0; i < linkItem.size(); i++) {
								if (i + 1 < linkItem.size()) {
									if (newNode.getName().length() <= linkItem.get(i).length()) {
										linkItem.add(i, newNode.getName());
										break;
									} else if (newNode.getName().length() >= linkItem.get(i).length()
											&& newNode.getName().length() <= linkItem.get(i + 1).length()) {
										linkItem.add(i + 1, newNode.getName());
										break;
									}
								} else {
									linkItem.add(i + 1, newNode.getName());
									break;
								}
							}
						}
						this.Item.put(newNode.getName(), gap);
						result.add(newNode.getName() + "的gap约束为： [" + gap[0] + ", " + gap[1] + "]");
					} else {
						if (this.flag) {
							// System.out.println(newNode.getName()+"������������ֵҪ���gap");
							sk.push(newNode);
							findMoreLengthItem(sk, oneItemSet);
						} else {
							// System.out.println(newNode.getName()+"������gap������ڦ�����������ֵҪ��");
						}
						// sk.push(newNode);
						// findMoreLengthItem(sk, oneItemSet);
					}
				} else {
					// System.out.println(newNode.getName()+"������pos��֧�ֶ���ֵ���Ա���֦");
				}
			} else {
				// System.out.println(newNode.getName()+"�ڽ�����д������������Ա���֦");
			}
		}
		/* �ж����ɵĶ���Ƿ�����pos֧�ֶȵ���������ͼ�֦ */
		return;
	}

	/**
	 * �Ƚ�newIntem��linkIntem���Ƿ��г�����
	 * 
	 * @param newIntem
	 * @param linkItem
	 * @return
	 */
	public boolean compareTowItem(String newIntem, List<String> linkItem) {
		boolean flag = false;
		for (int i = 0; i < linkItem.size(); i++) {
			if (linkItem.get(i).length() < newIntem.length()) {
				int index = 0, j = 0;
				for (; j < linkItem.get(i).length(); j++) {
					int loc = 0;
					if ((loc = newIntem.indexOf(linkItem.get(i).charAt(j), index)) != -1) {
						index = loc + 1;
					} else {

						break;
					}
				}
				if (j == linkItem.get(i).length()) {
					flag = true;
					// System.out.println("��������Ѿ�����"+newIntem+"�������У����Ա���֦");
					break;
				}
			} else {
				break;
			}
		}
		return flag;
	}

	// ----------------------------��ʼ����ö����--------------------------------------------------------------//
	/**
	 * ����ö����
	 * 
	 * @param oneItemSet
	 * @param oneItemSet
	 * @return
	 */
	public TreeNode formEnumerationTree(Map<String, Map<Integer, List<OccurSet>>> oneItemSet) {
		TreeNode root = new TreeNode();/* ��ʼ�����ڵ� */
		List<TreeNode> oneItemNodeSet = new ArrayList<TreeNode>();
		TreeNode node = null;
		Iterator<String> iter = oneItemSet.keySet().iterator();
		Stack<TreeNode> sk = new Stack<TreeNode>();/* ��ջ��ʵ��ö����������������� */
		/* ������pos��ֵ������neg��yi��������� */
		while (iter.hasNext()) {
			String subSql = iter.next();
			node = new TreeNode(subSql);
			Iterator<Integer> iter1 = oneItemSet.get(subSql).keySet().iterator();
			while (iter1.hasNext()) {
				Integer sqlId = iter1.next();
				/* ��һ������γ����Ľڵ� */
				if (sqlId <= this.posSetLength)/* �����жϸ���ĳ���������pos */
				{
					if (node.occurPosSet == null) {
						node.occurPosSet = new HashMap<Integer, List<OccurSet>>();
						node.occurPosSet.put(sqlId, oneItemSet.get(subSql).get(sqlId));
					} else {
						node.occurPosSet.put(sqlId, oneItemSet.get(subSql).get(sqlId));
					}
				} else {
					if (node.occurNegSet == null) {
						node.occurNegSet = new HashMap<Integer, List<OccurSet>>();
						node.occurNegSet.put(sqlId, oneItemSet.get(subSql).get(sqlId));
					} else {
						node.occurNegSet.put(sqlId, oneItemSet.get(subSql).get(sqlId));
					}
				}
			}
			if (node.occurNegSet == null || node.occurNegSet.size() / this.negSetLength <= this.negSup) {
				Item.put(node.getName(), null);
				System.out.println(node.getName() + "��������gapΪ��");
				if (linkItem.size() == 0) {
					linkItem.add(node.getName());
				} else {
					for (int i = 0; i < linkItem.size(); i++) {
						if (i + 1 < linkItem.size()) {
							if (node.getName().length() >= linkItem.get(i).length()
									&& node.getName().length() <= linkItem.get(i + 1).length()) {
								linkItem.add(i + 1, node.getName());
								break;
							}
						} else {
							linkItem.add(i + 1, node.getName());
							break;
						}
					}
				}
			} else {
				int i = 0;
				for (; i < oneItemNodeSet.size(); i++) {
					if (node.getName().compareTo(oneItemNodeSet.get(i).getName()) > 0) {
						oneItemNodeSet.add(i, node);
						break;
					}
				}
				if (i == oneItemNodeSet.size()) {
					oneItemNodeSet.add(node);
				}
				/* �����һ�������neg֧�ֶȵ���ֵ�����������������֧�ֶ���ֵ���������ջ�� */
				// System.out.println(node.getName()+"------>��ջ");
			}
		}
		sk.addAll(oneItemNodeSet);
		/* ���ջ��Ϊ��һֱ���� */
		while (!sk.isEmpty()) {
			/* ����get_Other_Item()�ҵ������������������Ľڵ� */
			findMoreLengthItem(sk, oneItemSet);
		}
		return root;
	}

	/**
	 * �õ�����Ҫ��Ķ�gapֵ
	 * 
	 * @param node������һ���ڵ�
	 * @return
	 */
	public int[] getGap(TreeNode node) {
		int[] gap = null;/* �����洢gapֵ */
		Map<Gap, BitSet> supMap = new TreeMap<Gap, BitSet>(); /* �����洢ÿ������յ�gap��Ӧ��bitset */
		Map<Integer, List<Integer>> candidateGapSet = new TreeMap<Integer, List<Integer>>();/* ������߽��ҪҪ�ϲ����ұ߽� */
		/* �ֱ��supmap��candidateGapSet���г�ʼ�� */
		getSupMap(node.getOccurPosSet(), supMap);
		getSupMap(node.getOccurNegSet(), supMap);
		getCandidateGapSet(candidateGapSet, node.getOccurPosSet());
		/* ��������ָ�벢��ʼ��Ϊ�� */
		Map<Integer, Integer> index = new HashMap<Integer, Integer>();
		/* ��ʼ��index */
		Iterator<Integer> candidateGapSetIter = candidateGapSet.keySet().iterator();
		while (candidateGapSetIter.hasNext()) {
			Integer candidateGapSetKey = candidateGapSetIter.next();
			index.put(candidateGapSetKey, 0);
		}
		/* �ھ���ʵ�gap */
		while (!index.isEmpty()) {
			Gap mingap = new Gap(1, 32767);
			Iterator<Integer> indexIter = index.keySet().iterator();
			while (indexIter.hasNext()) {
				Integer left = indexIter.next();
				Integer candidateGapSetIndex = index.get(left);
				Gap currentgap = new Gap(left, candidateGapSet.get(left).get(candidateGapSetIndex));
				if ((currentgap.maxGap - currentgap.minGap < mingap.maxGap - mingap.minGap)
						|| (currentgap.maxGap - currentgap.minGap == mingap.maxGap - mingap.minGap
								&& currentgap.minGap < mingap.minGap)) {
					mingap = currentgap;
				}
			}
			BitSet bs = pan_Duan(mingap, supMap);

			BitSet posbs = bs.get(0, (int) (this.posSetLength));
			BitSet negbs = bs.get((int) (this.posSetLength), (int) (this.posSetLength + this.negSetLength));
			if (posbs.cardinality() / this.posSetLength >= this.posSup) {
				if (negbs.cardinality() / this.negSetLength <= this.negSup) {
					gap = new int[2];
					gap[0] = mingap.minGap;
					gap[1] = mingap.maxGap;
					return gap;
				}
				this.flag = true;
			}
			/* �¼�֦ */
			if (negbs.cardinality() / this.negSetLength > this.negSup) {
				Iterator<Integer> indexIter2 = index.keySet().iterator();
				while (indexIter2.hasNext()) {
					Integer left = indexIter2.next();
					if (left <= mingap.minGap) {
						int i = index.get(left);
						for (; i < candidateGapSet.get(left).size(); i++) {
							if (candidateGapSet.get(left).get(i) >= mingap.maxGap) {
								break;
							}
						}
						if (i != candidateGapSet.get(left).size()) {
							/* �ж��Ƿ���ڴ��ڦ��ĺ�ѡgap */
							if (!this.flag) {
								int index2 = candidateGapSet.get(left).size() - 1;
								Gap newgap = new Gap(left, candidateGapSet.get(left).get(index2));
								BitSet bs2 = pan_Duan(newgap, supMap);
								BitSet posbs2 = bs2.get(0, (int) (this.posSetLength));
								// BitSet negbs2=bs.get((int)
								// (this.posSetLength),(int)
								// (this.posSetLength+this.negSetLength));
								if (posbs2.cardinality() / this.posSetLength >= this.posSup) {
									this.flag = true;
								}
							}
							List<Integer> delSet = new ArrayList<Integer>();
							delSet.addAll(candidateGapSet.get(left).subList(i + 1, candidateGapSet.get(left).size()));
							candidateGapSet.get(left).removeAll(delSet);
						}
					}
				}
			}
			if (index.get(mingap.minGap) != candidateGapSet.get(mingap.minGap).size() - 1) {
				Integer newindex = index.get(mingap.minGap) + 1;
				index.put(mingap.minGap, newindex);
			} else {
				index.remove(mingap.minGap);
			}
		}
		return gap;
	}

	/**
	 * 
	 * @param occurSet
	 * @return
	 * @return
	 */
	public void getSupMap(Map<Integer, List<OccurSet>> occurSet, Map<Gap, BitSet> supMap) {
		// Map<int[], BitSet> supMap=new HashMap<int[],BitSet>();
		Iterator<Integer> iter = occurSet.keySet().iterator();
		while (iter.hasNext()) {
			Integer sqId = iter.next();
			for (OccurSet os : occurSet.get(sqId)) {
				for (int[] osgap : os.maxOccur) {
					/* ��ÿ��gapԼ����supmap����� */
					if (supMap.size() == 0) {
						Gap gap = new Gap(osgap[0], osgap[1]);
						BitSet bs = new BitSet((int) (this.posSetLength + this.negSetLength));
						bs.set(sqId - 1);
						supMap.put(gap, bs);
					} else {
						Set<Gap> supList = supMap.keySet();
						boolean flag = true;
						for (Gap a : supList) {
							if (a.minGap == osgap[0] && a.maxGap == osgap[1]) {
								flag = false;
								break;
							}
						}
						if (flag == false) {
							Gap gap = new Gap(osgap[0], osgap[1]);
							supMap.get(gap).set(sqId - 1);
						} else {
							Gap gap = new Gap(osgap[0], osgap[1]);
							BitSet bs = new BitSet((int) (this.posSetLength + this.negSetLength));
							bs.set(sqId - 1);
							supMap.put(gap, bs);
						}
					}
				}
			}
		}
		return;
	}

	/**
	 * 
	 * @param candidateGapSet
	 * @param supMap
	 */
	public void getCandidateGapSet(Map<Integer, List<Integer>> candidateGapSet, Map<Integer, List<OccurSet>> occurSet) {
		// ���������ϵĽ���gap��һ����
		Set<Gap> c_GapSet = new TreeSet<Gap>();
		Iterator<Integer> c_GapSetIter = occurSet.keySet().iterator();
		while (c_GapSetIter.hasNext()) {
			Integer sqId = c_GapSetIter.next();
			for (OccurSet os : occurSet.get(sqId)) {
				for (int[] osgap : os.maxOccur) {
					Gap gap = new Gap(osgap[0], osgap[1]);
					c_GapSet.add(gap);
				}
			}
		}
		Iterator<Gap> iter = c_GapSet.iterator();
		while (iter.hasNext()) {
			Gap osgap = iter.next();
			if (!candidateGapSet.containsKey(osgap.minGap)) {
				List<Integer> right = new ArrayList<Integer>();
				right.add(osgap.maxGap);
				candidateGapSet.put(osgap.minGap, right);
			} else {
				/* ֱ�Ӵ�С������� */
				if (!candidateGapSet.get(osgap.minGap).contains(osgap.maxGap)) {
					int i = 0;
					for (; i < candidateGapSet.get(osgap.minGap).size(); i++) {
						if (osgap.maxGap < candidateGapSet.get(osgap.minGap).get(i)) {
							candidateGapSet.get(osgap.minGap).add(i, osgap.maxGap);
							break;
						}
					}
					if (i == candidateGapSet.get(osgap.minGap).size()) {
						candidateGapSet.get(osgap.minGap).add(osgap.maxGap);
					}
				}
			}
			Iterator<Integer> candidateGapSetIter = candidateGapSet.keySet().iterator();
			while (candidateGapSetIter.hasNext()) {
				Integer left = candidateGapSetIter.next();
				if (osgap.minGap >= left && osgap.maxGap > candidateGapSet.get(left).get(0)
						&& !candidateGapSet.get(left).contains(osgap.maxGap)) {
					/* ��С����������� */
					int i = 0;
					for (; i < candidateGapSet.get(left).size(); i++) {
						if (osgap.maxGap < candidateGapSet.get(left).get(i)) {
							candidateGapSet.get(left).add(i, osgap.maxGap);
							break;
						}
					}
					if (i == candidateGapSet.get(left).size()) {
						candidateGapSet.get(left).add(osgap.maxGap);
					}
				}
			}
		}
	}

	/**
	 * �ж��Ƿ�������Ҫ���gap������о���� ��ab 0(2,0), 1(1,2), 2(2,1), 5(1,0)
	 * 
	 * @param posNegOccur
	 * @return
	 */
	public BitSet pan_Duan(Gap candidategap, Map<Gap, BitSet> supMap) {
		BitSet bs = new BitSet((int) (this.negSetLength + this.posSetLength));
		Iterator<Gap> supMapIter = supMap.keySet().iterator();
		while (supMapIter.hasNext()) {
			Gap a = supMapIter.next();
			if (a.minGap >= candidategap.minGap && a.maxGap <= candidategap.maxGap) {
				bs.or(supMap.get(a));
			}

		}
		return bs;
	}

	/**
	 * �õ�ĳһ����������������֮���������ֵ
	 * 
	 * @param occurSet��ĳһ���еĳ��ּ���
	 * @return
	 */
	public Map<Integer, List<int[]>> getTwoItemgap(Map<Integer, List<OccurSet>> nodeOccurSet) {
		Map<Integer, List<int[]>> twoItemGap = new HashMap<Integer, List<int[]>>();
		/* ���������ݼ� */
		Iterator<Integer> iter = nodeOccurSet.keySet().iterator();
		while (iter.hasNext()) {
			List<int[]> newOccurSet = new ArrayList<int[]>();
			Integer sqId = iter.next();
			for (OccurSet os : nodeOccurSet.get(sqId)) {
				if (os != null && os.maxOccur != null) {
					newOccurSet.addAll(os.maxOccur);
				}
			}
			twoItemGap.put(sqId, newOccurSet);
		}
		return twoItemGap;
	}

	/**
	 * ��i��ĳ��������i��Ϊ���ǰ׺��i+1��ĳ��֣�
	 * 
	 * @param b��
	 *            һ��ĵ�j����
	 * @param occur_Set��
	 *            i�ĳ���
	 * @param a��
	 *            i������һ��
	 * @param two_Item��
	 *            ���еĶ���ڵ�
	 * @return i+1��ĳ���
	 */
	public Map<Integer, List<OccurSet>> getOccurSet(Map<Integer, List<OccurSet>> nodeOccurSet,
			Map<Integer, List<OccurSet>> oneItemOccur) {
		Map<Integer, List<OccurSet>> moreOccurSet = null;
		if (nodeOccurSet != null) {
			moreOccurSet = new HashMap<Integer, List<OccurSet>>();
			Iterator<Integer> nodeOccurSetIter = nodeOccurSet.keySet().iterator();
			while (nodeOccurSetIter.hasNext()) {
				Integer sqId = nodeOccurSetIter.next();
				List<OccurSet> occurSet = new ArrayList<OccurSet>();
				if (oneItemOccur.containsKey(sqId)) {
					for (OccurSet oneOccur : oneItemOccur.get(sqId)) {
						OccurSet os = null;
						for (OccurSet nodeOccur : nodeOccurSet.get(sqId)) {
							if (oneOccur.occurNumber > nodeOccur.occurNumber) {
								if (os == null) {
									os = new OccurSet(oneOccur.occurNumber);
									// int a[]=new int[2];
									os.maxOccur.addAll(getMinMaxOccur(os.maxOccur, oneOccur.occurNumber, nodeOccur));
								} else {
									os.maxOccur.addAll(getMinMaxOccur(os.maxOccur, oneOccur.occurNumber, nodeOccur));
								}
							}
						}
						if (os != null)
							occurSet.add(os);
					}
					if (occurSet.size() != 0)
						moreOccurSet.put(sqId, occurSet);
				}
			}
		}
		return moreOccurSet;
	}

	/**
	 * �õ��������������ֵ�ļ���
	 * 
	 * @param maxOccur
	 * @param oneItemOccurNumber
	 * @param nodeOccur
	 * @return
	 */
	public List<int[]> getMinMaxOccur(List<int[]> maxOccur, Integer oneItemOccurNumber, OccurSet nodeOccur) {
		List<int[]> newOccurSet = new ArrayList<int[]>();
		Integer number = oneItemOccurNumber - nodeOccur.occurNumber - 1;
		if (nodeOccur.maxOccur.size() == 0) {
			int a[] = new int[2];
			a[0] = a[1] = number;
			newOccurSet.add(a);
		} else {
			for (int[] occur : nodeOccur.maxOccur) {
				if (occur[0] >= number && !newOccurSet.contains(occur)) {
					int a[] = new int[2];
					a[0] = number;
					a[1] = occur[1];
					newOccurSet.add(a);
					// newOccurSet.add(number);
				} else if (occur[1] < number && !newOccurSet.contains(occur)) {
					int a[] = new int[2];
					a[0] = occur[0];
					a[1] = number;
					newOccurSet.add(a);
				}
			}
		}
		return newOccurSet;
	}
}