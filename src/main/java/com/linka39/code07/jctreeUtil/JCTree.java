package com.linka39.code07.jctreeUtil;


import com.linka39.code07.config.SensitivePathConfig;
import org.apache.log4j.*;


import java.util.*;

/**
 * 决策树
 * @author:Dyl
 *
 */
public class JCTree {

//	private static Logger logger = Logger.getLogger(JCTree.class);
	private static Logger logger1 = Logger.getLogger("myTest1");

	/**
	 * 构造函数，初始化决策树，停顿词库
	 */
	public  JCTree() throws Exception {
		initJCTree();
	}

	/**
	 * 分类结果Map
	 */
	public static TreeNode rootNode=null;
	public static int leaf = 0;

	/**
	 * 属性获取
	 */
	public static Vector<Object> rootShuXing= null;

	public static void initJCTree() throws Exception {
		//获取数据
		if(rootShuXing == null){
			rootShuXing = MyFile.getShuXing(SensitivePathConfig.jctreePath+ "test.txt");// 获取属性
		}
		Vector<Object>[] testDate= MyFile.readData(SensitivePathConfig.jctreePath+ "test.txt");//获取样本数据-不包括头属性
		rootNode=JCTree.createTree(rootShuXing,testDate);
//		getJCTreeSuccess();
	}

	public static void getJCTreeSuccess() throws Exception {
		MyFile myFile =new MyFile();
		Vector<Object[]> predictData= myFile.readPredictData(SensitivePathConfig.jctreePath+ "predict.txt");//获取测试数据
		StringBuffer sb = new StringBuffer();
		//打印树结构
		sb.append("决策树结构如下： \n");
		JCTree.showTree(rootNode,1,sb);
		logger1.info(sb);
		sb = new StringBuffer();//引用一个新的对象清空原对象的值
		//计算测试的成功率
		sb.append("决策树的成功率为： \n");
		double temp=0;
		temp=JCTree.suceessGl(rootNode,rootShuXing,predictData);
		sb.append((float)temp*1000/10+"%");
		logger1.info(sb);
	}

	/**
	 * 建立决策树
	 * @param shuXing：属性
	 * @param testData：样本数据
	 * @return
	 */
	public static TreeNode createTree(Vector<Object> shuXing,
									  Vector<Object>[] testData) {
		int maxGainIndex = -1;// 记录增益最大的列
		double gain = 0;
		double maxGain = 0;
		TreeNode fatherNode = new TreeNode();// 定义相应的父节点
		fatherNode.setShuXing(shuXing);// 设置属性
		// 计算熵，并记录信息增益最大的一列
		for (int i = 0; i < shuXing.size() - 1; i++) {
			gain = Gain.infoD(testData, shuXing.size() - 1)
					- Gain.infoSX(testData, i, shuXing.size() - 1);// 某一列的info
			if (maxGain < gain) {
				maxGainIndex = i;
				maxGain = gain;
			}
		}
		if (maxGainIndex == -1) {
			return null;
		}
		Vector<Object> splitSX = Gain.getSX(testData, maxGainIndex);
		fatherNode.setNodeName(shuXing.get(maxGainIndex));
		fatherNode.setSplitShuXing(splitSX);// 最大熵值进行分裂属性

		// 添加子节点
		for (int i = 0; i < splitSX.size(); i++) {
			Vector<Object>[] splitData = splitData(testData, maxGainIndex,
					splitSX.get(i));// //////////////////maxGainIndex没处理
			Vector<Object> lastRow = Gain
					.getSX(splitData, splitData.length - 1);// 拿到最后一列的属性
			TreeNode sonNode = new TreeNode();
			if (lastRow.size() == 1) {// 全为一种属性，unacc、acc、good、vgood，这种是最纯的属性
				sonNode.setNodeName(lastRow.get(0));
			} else {
				Vector<Object> newShuXing = new Vector<Object>();
				// 重新处理新的属性集,删掉多余的属性集
				for (Object ob : shuXing) {
					if (!ob.toString().equals(
							shuXing.get(maxGainIndex).toString())) {
						newShuXing.add(ob);
					}
				}
				// System.out.println(newShuXing);
				Vector<Object>[] newDataSet = new Vector[splitData.length - 1];
				int t = 0;
				for (int j = 0; j < splitData.length; j++) {
					if (j == maxGainIndex) {
						continue;
					}
					newDataSet[t++] = (Vector<Object>) splitData[j].clone();
				}
				// // System.out.println(newDataSet[0].size());
				sonNode = createTree(newShuXing, newDataSet);// 子节点继续扩展创建树
			}
			if (sonNode != null) {
				fatherNode.addSonNode(sonNode);
			}
		}
		rootNode = fatherNode;
		return fatherNode;
	}

	/**
	 * 根据属性分离表
	 * @param testData
	 * @param maxGainIndex
	 * @param object
	 * @return
	 */
	private static Vector<Object>[] splitData(Vector<Object>[] testData,
											  int maxGainIndex, Object object) {
		Vector<Object>[] splitBiao = new Vector[testData.length];
		for (int i = 0; i < splitBiao.length; i++) {
			splitBiao[i] = new Vector<Object>();
		}
		for (int i = 0; i < testData[maxGainIndex].size(); i++) {// i行
			Object object2 = testData[maxGainIndex].get(i);
			if (object2.toString().equals(object.toString())) {
				for (int j = 0; j < splitBiao.length; j++) {
					splitBiao[j].add(testData[j].get(i));
				}
			}
		}
		return splitBiao;
	}

	/**
	 * 递归获取决策树，决策树保存在线程安全类StringBuffer里，经过递归添加最终返回的StringBuffer就是最终的树结构
	 * @param rootNode
	 */
	public static StringBuffer showTree(TreeNode rootNode, int ceng, StringBuffer sb) {
		sb.append(rootNode.getNodeName()+"\n");// 添加当前树的属性名称
		try{
			List<Object> splitShuXing = rootNode.getSplitShuXing();
			ArrayList<TreeNode> sonNode = rootNode.getSonNode();
			if (splitShuXing != null) {// 如果有分支节点
				for (int i = 0; i < splitShuXing.size(); i++) {
					sb.append(ceng);//打印决策树所在的层数
					for (int j = 0; j <= ceng; j++)
						sb.append("     ");// 添加当前树的层数
					sb.append(splitShuXing.get(i) + "--->");//打印树的属性值的名称并指向下一个属性值
					if(sonNode.get(i).getSonNode().size()==0){
						leaf++;
						sb.append("["+ceng+"|"+leaf+"]");// 添加当前树的层数和叶子树，以[层|叶子]的形式打印
					}
					if(sonNode.get(i)!=null){
						showTree(sonNode.get(i), (ceng + 1), sb);//递归打印决策树的下一层
					}

				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return sb;//返回树的结构
	}

	/**
	 * 递归获取决策树，决策树保存在线程安全类StringBuffer里，经过递归添加最终返回的StringBuffer就是最终的树结构
	 * @param rootNode
	 */
//	public static void showTree2(TreeNode rootNode, int ceng) {
//		System.out.print(rootNode.getNodeName()+"\n");// 添加当前树的属性名称
//		List<Object> splitShuXing = rootNode.getSplitShuXing();
//		ArrayList<TreeNode> sonNode = rootNode.getSonNode();
//		if (splitShuXing != null) {// 如果没有分支
//			for (int i = 0; i < splitShuXing.size(); i++) {
//				for (int j = 0; j <= ceng; j++)
//					System.out.print("     ");// 添加当前树的层数
//				System.out.print(splitShuXing.get(i) + "-->");//打印树的属性值的名称并指向下一个属性值
//				if(sonNode.get(i)!=null)
//					showTree2(sonNode.get(i), (ceng + 1));//递归打印决策树的下一层
//			}
//		}
//	}

	/**
	 * 计算成功率，并打印出结果
	 * @param rootNode
	 * @param shuXing
	 * @param predictData
	 */
	public static double suceessGl(TreeNode rootNode, Vector<Object> shuXing,
								   Vector<Object[]> predictData) {
		int sucess = 0;//预测成功的结果数
		for (int i = 0; i < predictData.size(); i++) {//循环测试数据集，从第一条记录开始判断
			Object object = getResult(rootNode, shuXing, predictData.get(i));//获取在决策树中得到的结果 敏感词评级，four, three, two, one
			if (object.toString().equals(//如果通过决策树计算出来的结果等级 和 测试集里的这条记录等级是一样的话，成功记录数加1
					predictData.get(i)[(predictData.get(i).length-1)].toString())) {
				sucess++;
			}
		}
		//最后返回一个double类型数据，预测成功数/总预测记录数
		return (double) sucess / predictData.size();
	}

	/**
	 * 通过根节点开始访问，得出每一条记录的结果
	 * @param rootNode
	 * @param shuXing
	 * @return
	 */
	public static Object getResult(TreeNode rootNode, Vector<Object> shuXing,
								   Object[] eachRow) {
		//递归结束条件：如果当前结点是叶子节点的话返回节点的属性值
		if (rootNode.getSonNode().size()==0) {
			return rootNode.getNodeName();
		}
		//获取当前属性名字所在的索引
		Object nodeName=rootNode.getNodeName();
		int index=shuXing.indexOf(nodeName);
		Object splitSX=eachRow[index];//获取测试集的分裂属性值
		int sxIndex=0;
		for (int i = 0; i < rootNode.getSplitShuXing().size(); i++) {
			//根据分裂属性数组，依次遍历查找决策树的分裂属性
			Object temp =rootNode.getSplitShuXing().get(i);//获取当前分裂属性的值
			if (splitSX.toString().equals(temp.toString())) {//比较当前分裂属性的值是否等于测试集的属性值
				sxIndex=i;//如果相等的话，就在相应子树下继续查找
			}
		}
		return getResult(rootNode.getSonNode().get(sxIndex),shuXing,eachRow);//继续递归查找子决策树
	}

}
