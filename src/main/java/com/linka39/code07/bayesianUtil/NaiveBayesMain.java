package com.linka39.code07.bayesianUtil;

import com.linka39.code07.util.StringUtil;

import java.math.BigDecimal;
import java.util.*;

import static com.linka39.code07.bayesianUtil.MultinomialModelNaiveBayes.SortMap;
import static com.linka39.code07.bayesianUtil.MultinomialModelNaiveBayes.classifierResult;

/**
 *
 * <p>Title:NaiveBayesMain</p>
 * <p>Description: 主函数类
 * </p>
 * @createDate：2013-8-30
 * @author xq
 * @version 1.0
 */
public class NaiveBayesMain {

	public static void main(String[] args){
		/**
		 * 文章采用决策树的形式进行判断，属性值设为 字数，敏感词数，敏感词分类，积分，用户发表文章数
		 */
		TrainSampleDataManager.process();
		String s="打人，成人dv，炸药，售卖炸药";

		//todo
//		public static Map<String, Map<String, Map<String, Long>>> allWordsMap=new HashMap<String, Map<String, Map<String, Long>>>();
		Set<String> classifierSet=TrainSampleDataManager.allWordsMap.keySet();
		int count = 0;
		for(String classifier : classifierSet){
			if(!"色情0rr4".equals(classifier)){
				String emotion = classifier.substring(classifier.length()-1);
				Map<String, Map<String, Long>> temp2=TrainSampleDataManager.allWordsMap.get(classifier);
				Set<String> set2=temp2.keySet();
				for(String s2 : set2){
					int scnt = 0;
					Map<String, Long> temp3=temp2.get(s2);
					Set<String> set3=temp3.keySet();
					for(String s3 : set3){
						scnt = temp3.get(s3).intValue();
						count=count+ scnt;
//						System.out.print(s3+"\n");
						System.out.print("("+emotion+",NOW(),'"+ s3 +"',62,"+ scnt +"),");
					}
				}
			}

		}
		System.out.println();
		System.out.println(count);

//		String regEx = "";
//		for (Object str : nowSet) {
//			tempstr += str.toString();
//		}
//		System.out.println(tempstr);
//		String regEx = "["+"@！、。——……（）《》，【】‘[；“]^_？ !\"#$%￥&'()*+,-./;{|<}=~>?"+"]";
//		s = s.replaceAll(regEx, "");
		Map<String, Long> wordMap = ChineseTokenizer.segStr(s);
		Set<String> wordstr= wordMap.keySet();
		ArrayList<String> stringArr = new ArrayList<>();
		for(String str:wordstr){
			int len = wordMap.get(str).intValue();
//			while (len>0){
//				stringArr.add(str);
//				len--;
//			}
			stringArr.add(str);
		}


		Map<String, BigDecimal> resultMap=MultinomialModelNaiveBayes.classifyResult(stringArr);
		resultMap =  SortMap(resultMap,resultMap.size());
		Set<String> set=resultMap.keySet();
		for(String str: set){
			System.out.println("classifer:"+str+"     probability:"+resultMap.get(str));
		}
		System.out.println("The final result:"+MultinomialModelNaiveBayes.getClassifyResultName(classifierResult));
		resultMap = SortMap(resultMap,resultMap.size());
		System.out.println(resultMap.toString());

		s= "<p>sdffaa</p><p><img src=\"/image/2020/06/15/20200615032535.jpg\" alt=\"20200615032535.jpg\">爱情<br></p>";
		System.out.println(s);
		//取出html
		String content= StringUtil.stripHtml(s);
		System.out.println(content);
	}
}
