package com.linka39.code07.jctreeUtil;

import com.linka39.code07.config.SensitivePathConfig;
import com.linka39.code07.service.impl.SensitiveServiceImpl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

import static com.linka39.code07.jctreeUtil.JCTree.*;

/**主函数
 * @author：Dyl
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	//字数,敏感词数/词数,敏感词情感分类,用户发表文章数,用户年龄，用户积分,敏感词评级
	public static void main(String[] args) throws Exception {
		/**
		 *   buying       v-high, high, med, low
		 *    maint        v-high, high, med, low
		 *    doors        2, 3, 4, 5-more
		 *    persons      2, 4, more
		 *    lug_boot     small, med, big
		 *    safety       low, med, high
		 *    class_value        unaccept, accept, good, vgood
		 */
		/**
		 * count	文章词数量 v-high[5000,+), high[3000,5000), med[500,3000), low[0,500)
		 * sensitive	敏感词数/词数，v-high[0.2,+), high[0.1,0.2), med[0.05,0.1), low[0,0.05)
		 * emotion	敏感词情感分类，1,2, 3, 4, 5
		 * papers	用户发表文章数，low[0,5), med[5,30), high[30,+)
		 * days	用户年龄，small[0,15), med[15,100), big[100,+)
		 * points	用户积分，low[0,10), med[10,50), high[50,+)
		 * level	敏感词评级，four, three, two, one
		 */
		//获取数据
		MyFile myFile =new MyFile();
//		String urlTest="F:\\迅雷下载\\决策树java代码_car\\data\\test.txt";
		String urlTest= SensitivePathConfig.jctreePath+ "test.txt";
		String urlPredict= SensitivePathConfig.jctreePath+ "predict.txt";
		Vector<Object> shuXing= myFile.getShuXing(urlTest);// 获取属性
		Vector<Object>[] testDate= myFile.readData(urlTest);//获取样本数据-不包括头属性
		//todo
		LinkedList<String[]> sxList = new LinkedList<>();
//		for (int i = 0; i < testDate[0].size(); i++) {
//			String []s=new String[testDate.length];
//			for(int j = 0;j<testDate.length;j++){
//				String sss = testDate[j].get(i).toString();
//				String ssd = new String();
//				String ssd2 = new String();
//				String s2 =  "";
//				if(j==6){//j是属性，i是第几个记录
//					double d = Math.random();//生成一个0~1的随机数
//					if(d<=0.9){
//						ssd = testDate[1].get(i).toString();
//						ssd2 = testDate[2].get(i).toString();
////						if("vhigh".equals(ssd)) {
////							s2 = "one";
////						}
////						if("vhigh".equals(ssd)&&"1".equals(ssd2)) {
////							s2 = "one";
////						}
////						if("low".equals(ssd)&&("5".equals(ssd2) || "4".equals(ssd2))) {
////							s2 = "four";
////						}
////						if("med".equals(ssd)&&("5".equals(ssd2) || "4".equals(ssd2))) {
////							s2 = "three";
////						}
////						if("low".equals(ssd)&&("1".equals(ssd2) || "2".equals(ssd2))) {
////							s2 = "three";
////						}
//
//						if("low".equals(ssd)&&("5".equals(ssd2) || "4".equals(ssd2))) {
//							s2 = "four";
//						}
//						if("med".equals(ssd)&&("1".equals(ssd2) || "2".equals(ssd2))) {
//							s2 = "three";
//						}
//						if("low".equals(ssd)&&("1".equals(ssd2) || ("2".equals(ssd2))||"5".equals(ssd2) || "4".equals(ssd2))) {
//							s2 = "four";
//						}
//
//
//					}
//					if("".equals(s2)&&d<=0.8){
//						ssd = testDate[3].get(i).toString();
//						ssd2 = testDate[5].get(i).toString();
//						if("high".equals(ssd)) {
//							s2 = "three";
//						}
//						if("low".equals(ssd)&&"low".equals(ssd2)) {
//							s2 = "two";
//						}
//						if("med".equals(ssd)&&"low".equals(ssd2)) {
//							s2 = "two";
//						}
//					}
//				}
//				if(!"".equals(s2)){
//					s[j] = s2;
//				}else{
//					s[j] = sss;
//				}
//			}
//			sxList.add(s);
//		}
//		SensitiveServiceImpl sensitiveService = new SensitiveServiceImpl();
//		System.out.println(sensitiveService.sensitiveDataWrite(sxList,1).toJSONString());

		if(rootNode==null){
			initJCTree();
		}
		getJCTreeSuccess();


		LinkedList<String[]> myList = new LinkedList<>();
		myList.add( new String[] { "vhigh","high","4","high","big","low"});
		myList.add( new String[] { "vhigh","vhigh","1","high","big","med"});
		myList.add( new String[] { "vhigh","vhigh","3","high","big","high"});
		myList.add( new String[] { "high","vhigh","5","low","small","low"});
		myList.add( new String[] { "vhigh","high","4","low","big","low"});
		myList.add( new String[] { "low","low","5","high","small","low"});
		for(int i = 0;i<myList.size();i++){
			String[] strlist = myList.get(i);
			System.out.println("测试数据："+ Arrays.toString(strlist));
			Object object = JCTree.getResult(rootNode, rootShuXing, strlist);//uacc ,acc, good,vgood
			System.out.println("预测结果："+object.toString());
		}


	}
}
