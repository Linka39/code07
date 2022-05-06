package com.linka39.code07.sensitiveUtil;

import com.linka39.code07.ZHConverterUtil.ZHConverter;
import com.linka39.code07.bayesianUtil.ChineseTokenizer;
import com.linka39.code07.config.SensitivePathConfig;
import com.linka39.code07.service.DicService;
import com.linka39.code07.service.SensitiveWordService;
import com.linka39.code07.service.impl.SensitiveServiceImpl;
import com.linka39.code07.util.SizeOf;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;
/**
 * 1、设置系统参数 遍历算法;关键词文件
 */

/**
 * @Description: 敏感词过滤
 * @Project：test
 * @Author : zsh
 * @Date ： 2021年4月20日 下午4:17:15
 * @version 1.0
 */
public class SensitiveWordFilter extends SizeOf {

	public static Map sensitiveWordMap = null;
	public static HashSet stopSet = null; // 停顿词
	//spring项目启动时由此获取加载目录配置
	public static  int minMatchTYpe = 1;      //最小匹配规则
	public static  int maxMatchType = 2;      //最大匹配规则

	public static String replaceChar = "*"; // 停顿词
	public static String stopWordStr = null;	//停顿词库的str
	public static String accessLevel = null;	//最低等级
	public static int sensiMatchType = 1;	//停顿词匹配方式


	/**
	 * 构造函数，初始化敏感词库，停顿词库
	 */
	public  SensitiveWordFilter(){
//		SensitiveWordInit sensitiveWordInit = new SensitiveWordInit();
//		new SensitiveWordInit().initKeyWord(SensitivePathConfig.sensitivePath);
//		new SensitiveWordInit().initStopWords(SensitivePathConfig.stopWordsPath);
	}
	/**
	 * 当静态变量sensitiveWordMap为null时(新增，删除敏感词库)，重新初始化敏感词库的方法
	 */
	public static void initKeyWord() {
		sensitiveWordMap = SensitiveWordInit.initKeyWord(SensitivePathConfig.sensitivePath);
	}

	/**
	 * 当静态变量sensitiveWordMap为null时，重新初始化停顿词库的方法
	 */
	public static void initStopWord() {
		stopSet = SensitiveWordInit.initStopWords(SensitivePathConfig.stopWordsPath);
//		stopSet = SensitiveWordInit.initStopWords2();
	}




	/**
	 * 获取文字中的敏感词
	 * @author zsh
	 * @date 2021年4月20日 下午5:10:52
	 * @param txt 文字
	 * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
	 * @return
	 * @version 1.0
	 */
	public static Set<String> getSensitiveWord(String txt , int matchType){
		Set<String> sensitiveWordList = new HashSet<String>();
		/*去除全部空格*/
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
		String txt2 = converter.convertOver(txt);
//		System.out.println("繁体转简体时间为：" + (System.currentTimeMillis() - endTime) +"毫秒");
		for(int i = 0 ; i < txt2.length() ; i++){
			int length = CheckSensitiveWord(txt2, i, matchType);    //判断是否包含敏感字符
			if(length > 0){    //存在,加入list中
				sensitiveWordList.add(txt.substring(i, i+length));
				i = i + length - 1;    //减1的原因，是因为for会自增
			}
		}

		return sensitiveWordList;
	}

	/**
	 * 判断文字是否包含敏感字符
	 *
	 * @param txt       文字
	 * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
	 * @return 若包含返回true，否则返回false
	 * @author zsh
	 * @date 2021年4月20日 下午4:28:30
	 * @version 1.0
	 */
	public static boolean isContaintSensitiveWord(String txt, int matchType) {
		boolean flag = false;
		if (txt == null) {
			return flag;
		}

		for (int i = 0; i < txt.length(); i++) {
			//判断是否包含敏感字符
			int matchFlag = CheckSensitiveWord(txt, i, matchType);
			if (matchFlag > 0) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 替换敏感字字符
	 * @author zsh
	 * @date 2021年4月20日 下午5:12:07
	 * @param txt
	 * @param matchType
	 * @param replaceChar 替换字符，默认*
	 * @version 1.0
	 */
	public static String  replaceSensitiveWord(String txt,int matchType,String replaceChar){
		String resultTxt = txt;
		Set<String> set = getSensitiveWord(txt, sensiMatchType);     //获取所有的敏感词
		Iterator<String> iterator = set.iterator();
		String word = null;
		String replaceString = null;
		while (iterator.hasNext()) {
			word = iterator.next();
			replaceString = getReplaceChars(replaceChar, word.length());
			resultTxt = resultTxt.replaceAll(word, replaceString);
		}

		return resultTxt;
	}

	/**
	 * 获取替换字符串
	 * @author zsh
	 * @date 2021年4月20日 下午5:21:19
	 * @param replaceChar
	 * @param length
	 * @return
	 * @version 1.0
	 */
	private static  String getReplaceChars(String replaceChar,int length){
		String resultReplace = replaceChar;
		for(int i = 1 ; i < length ; i++){
			resultReplace += replaceChar;
		}
		return resultReplace;
	}

	/**
	 * 检查文字中是否包含敏感字符，检查规则如下：<br>
	 * @date 2021年4月20日 下午4:31:03
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return，如果存在，则返回敏感词字符的长度，不存在返回0
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes"})
	public static int CheckSensitiveWord(String txt,int beginIndex,int matchType){
		boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
		int matchFlag = 0;     //匹配标识数默认为0
		char word = 0;
		/*标识，这是这次关键词判断的第几次*/
		int stopWordCount = 0;
		Map nowMap = sensitiveWordMap;
		HashSet nowSet = stopSet;

		for(int i = beginIndex; i < txt.length() ; i++ , stopWordCount++ ){
			word = txt.charAt(i);
			/*判断空格,并且不是第一个敏感词的开始词*/
			if (stopWordCount != 0 && (nowSet.contains(word+""))) {
				matchFlag++;
				continue;
			}
			nowMap = (Map) nowMap.get(word);     //获取指定key
			if(nowMap != null){     //存在，则判断是否为最后一个
				matchFlag++;     //找到相应key，匹配标识+1
				if("1".equals(nowMap.get("isEnd"))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
					flag = true;       //结束标志位为true
					if(SensitiveWordFilter.minMatchTYpe == matchType){    //最小规则，直接返回,最大规则还需继续查找
						break;
					}
				}
			}
			else{     //不存在，直接返回
				break;
			}
		}
		if(matchFlag < 2 || !flag){        //长度必须大于等于1，为词
			matchFlag = 0;
		}
		return matchFlag;
	}


	/**
	 * 获取文字中的敏感词
	 * @author zsh
	 * @date 2021年4月20日 下午5:10:52
	 * @param txt 文字
	 * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
	 * @return
	 * @version 1.0
	 */
	public static Map<String,Map<String, Integer>> getSensitiveWordToMap(String txt , int matchType){
		/*去除全部空格*/
		Map<String,Map<String, Integer>> mspRst = new HashMap<>();
		txt = stringConvert2(txt);
		String SensWord = "";//当前敏感词
		String nativeSensWord = "";//原生敏感词
		Map<String, Integer> words = new HashMap<String, Integer>();//干扰性敏感词
		Map<String, Integer> native_words = new HashMap<String, Integer>();//原生敏感词
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
		String txt2 = converter.convertOver(txt);

		String regEx = "["+stopWordStr+"]";//当前停顿词

		for(int i = 0 ; i < txt.length() ; i++){
			int length = CheckSensitiveWord(txt2, i,  matchType);    //判断是否包含敏感字符
			if(length > 0){    //存在,加入list中
				//处理输入的敏感词map
				SensWord = txt.substring(i, i+length);
				if (words.containsKey(SensWord)) {
					words.put(SensWord, words.get(SensWord) + 1);
				} else {
					words.put(SensWord, 1);
				}
				//处理词库的敏感词map
				nativeSensWord = txt2.substring(i, i+length);
				if(nativeSensWord.matches(regEx)){
					nativeSensWord = nativeSensWord.replaceAll(regEx, "");
				}
				if (native_words.containsKey(nativeSensWord)) {
					native_words.put(nativeSensWord, native_words.get(nativeSensWord) + 1);
				} else {
					native_words.put(nativeSensWord, 1);
				}

				i = i + length - 1;    //减1的原因，是因为for会自增
			}
		}
		mspRst.put("words",words);
		mspRst.put("native_words",native_words);

		return mspRst;
	}

	/**
	 * 获取文字中的敏感词(第二版)
	 * @author zsh
	 * @date 2022年4月20日 下午5:10:52
	 * @param temp_words 文字
	 * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
	 * @return
	 * @version 2.0
	 */
	public static Map<String, Long> getSensitiveWordToMap2(Map<String, Long> temp_words, int matchType){

		Set<String> wordsSet=temp_words.keySet();
		String SensWord = "";//当前敏感词
		Map<String, Long> words = new LinkedHashMap<String, Long>();
		for(String word : wordsSet){
			Long word_count =temp_words.get(word);

			for(int i = 0 ; i < word.length() ; i++){
				int length = CheckSensitiveWord(word, i, matchType);    //判断是否包含敏感字符
				if(length > 0){    //存在,加入list中
					SensWord = word.substring(i, i+length);
					words.put(SensWord, word_count);
				}
			}
		}

		return words;
	}

	/**
	 * 获取文字中的敏感词
	 * @author zsh
	 * @date 2021年4月20日 下午5:10:52
	 * @param words 敏感词
	 * @param num 最大词数
	 * @return
	 * @version 1.0
	 */
	public static Map<String, Long> SortMap(Map<String, Long> words,int num){
		Map<String, Long> new_words = new LinkedHashMap<String, Long>();
		if(words.size()>0){
			List<Map.Entry<String,Long>> list = new ArrayList<Map.Entry<String,Long>>(words.entrySet());
			Collections.sort(list,new Comparator<Map.Entry<String,Long>>() {
				@Override
				public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
			for(int j = 0;j<list.size()&&j<num;j++){
				Map.Entry<String,Long> tempMap = list.get(j);
				new_words.put(tempMap.getKey(),tempMap.getValue());
			}
		}

		return new_words;
	}

	/**
	 * 大写转化为小写 全角转化为半角
	 *
	 * @param src
	 * @return
	 */
	private static char charConvert(char src) {
		//如果字符在全角范围内转成半角
		int r = BCConvertUtils.qj2bj(src);
		if(r >= 'A' && r <= 'Z'){
			r= r + 32;
		}
		return (char)r;
	}
	/**
	 * 大写转化为小写 全角转化为半角 string
	 *
	 * @param src
	 * @return
	 */
	private static String stringConvert(String src) {
		if (src == null) {
			return src;
		}
		StringBuilder buf = new StringBuilder(src.length());
		char[] ca = src.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			buf.append(charConvert(ca[i]));
		}
		return buf.toString();
	}

	/**
	 * 大写转化为小写 全角转化为半角 繁体转简体 string
	 *
	 * @param src
	 * @return
	 */
	public static String stringConvert2(String src) {
		src = stringConvert(src);
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
		String simplifiedStr = converter.convert(src);
		return simplifiedStr;
	}


	// 统计树种节点个数
	public static int totalNode(Map<Object, Object> map) {
		try{
			if (map==null) {
				return 0;
			} else {
				for (Map.Entry<Object, Object> entry : map.entrySet()) {
					if(entry.getValue().getClass().equals(HashMap.class)){
						Map map2 = (Map)entry.getValue();
						if(("1".equals(map2.get("isEnd").toString())))
							return 1;
						while(!"1".equals(map2.get("isEnd").toString())){
							return totalNode(map2)+1;
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	protected Object newInstance() {
		return new Object();
	}

	public static void main(String[] args) throws Exception {
		long beginTime = System.currentTimeMillis();
		if(SensitiveWordFilter.sensitiveWordMap==null){
			initKeyWord();
		}
		long jsTime = System.currentTimeMillis();
		System.out.println("建树时间为：" + (jsTime - beginTime) +"毫秒");
		if(SensitiveWordFilter.stopSet==null){
			initStopWord();
		}
		SizeOf sizeOf = new SensitiveWordFilter();
		System.out.println("所占内存：" + sizeOf.size() + "字节");
		int i = totalNode(SensitiveWordFilter.sensitiveWordMap);
		System.out.println(i);

		System.out.println("敏感词的始节点数：" + SensitiveWordFilter.sensitiveWordMap.size());
		String txt = "我帮你足交我帮你足交我帮你足交我帮裸露自拍你足交我帮你足交裸露自拍丝袜淫妇\n";


//		txt += "ＦＵ？cＫ太多的伤感情怀也许只局限于黑#火藥￥？配方，要在卖火药商那买点火药粉，最近几年经常在咸鱼上看到各种小姐鄙视链，其实真实情况是我们都很忙，黑火药配方,使用了黑火药配方，我就了解了C4炸药的成分";
		System.out.println("待检测语句字数：" + txt.length());
		System.out.println("待检测语句：" + txt);
		beginTime = System.currentTimeMillis();
		Set<String> set = SensitiveWordFilter.getSensitiveWord(txt, sensiMatchType);
		long endTime = System.currentTimeMillis();
//		FileUtils.deleteSensitiveWordFile(SensitivePathConfig.sensitivePath, "超碰");
		System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
		System.out.println("执行消耗时间为：" + (System.currentTimeMillis() - beginTime) +"毫秒");
//		System.out.println("总共消耗时间为：" + (endTime - beginTime) +"毫秒");
//		System.out.println("总共消耗时间为："  +"43 毫秒");
		System.out.println("替换关敏感词：");

		String text = "ＡＢＤ愛愛SDFFFＦAAAfsd發發《《fdｆｆ，我要%畢業$%￥我要畢_？業我￥&'要畢——……業我}=~要畢/;業&》要#$%畢》業我ｗ》Ａn{t，【《畢業";
		System.out.println("包含全角，特殊字符，大写字母，繁体字的字符串为：");
		System.out.println(text);
		String regEx = "["+"@！、。——……（）《》，【】‘[；“]^_？ !\"#$%￥&'()*+,-./;{|<}=~>?"+"]";
		text = text.replaceAll(regEx, "");

		System.out.println("进行统一处理转换后：");
		System.out.println(stringConvert2(text));




//		System.out.println(SensitiveWordFilter.replaceSensitiveWord(txt,1,"*"));


//		String qjStr = "ＦＵＣＫ太多怀也许ASDSADSD.com自杀草 泥馬個比比了個比比喝水和氣身材興奮生意";

//		System.out.println("大写转化为小写 全角转化为半角:"+qjStr+"\n"+stringConvert2(qjStr));
//		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
//		String simplifiedStr = converter.convert("草泥馬個比比了個比比喝水和@@$氣興奮生意 無能蠢貨");
//		System.out.println(simplifiedStr);

//		SensitiveServiceImpl sensitiveService = new SensitiveServiceImpl();
//		System.out.println(sensitiveService.getSensitiveWord(txt).toJSONString());
	}
}
