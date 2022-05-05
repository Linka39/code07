package com.linka39.code07.sensitiveUtil;


import com.linka39.code07.entity.Dic;
import com.linka39.code07.entity.SensitiveWord;
import com.linka39.code07.service.DicService;
import com.linka39.code07.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.*;

/**
 * @Description: 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 * @Project：test
 * @Author : zsh
 * @Date ： 2021年4月20日 下午2:27:06
 * @version 1.0
 */
@Component("SensitiveWordInit")
public class SensitiveWordInit {
	private static String ENCODING = "UTF-8";    //字符编码
	@SuppressWarnings("rawtypes")
	public static HashMap sensitiveWordMap;
	public static HashSet<String> stopSet; // 停顿词

	@Autowired
	private  SensitiveWordService sensitiveWordService;
	@Autowired
	private  DicService dicService;
	@Autowired
	private  getList  getList;


	public SensitiveWordInit(){
		super();
	}

	/**
	 * @author zsh
	 * @date 2021年4月20日 下午2:28:32
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public static Map initKeyWord(String path){
		try {
			//读取敏感词库
			Set<String> keyWordSet = readSensitiveWordFile(path);
			//将敏感词库加入到HashMap中
			addSensitiveWordToHashMap(keyWordSet);
			//spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
//			application.setAttribute("sensitiveWordMap",sensitiveWordMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensitiveWordMap;
	}

	/**
	 * @author zsh
	 * @date 2021年4月20日 下午2:28:32
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public  Map initKeyWord2(){
		try {
			//读取敏感词库
			Set<String> keyWordSet = readSensitiveWordFromDB();
			//将敏感词库加入到HashMap中
			addSensitiveWordToHashMap(keyWordSet);
			//spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
//			application.setAttribute("sensitiveWordMap",sensitiveWordMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensitiveWordMap;
	}

	/**
	 * @author zsh
	 * @date 2021年4月20日 下午2:28:32
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public static HashSet<String> initStopWords(String path){
		try {
			//读取停顿词库,将停顿词库加入到Set中
			stopSet = readSensitiveWordFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stopSet;
	}


	/**
	 * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
	 * 中 = {
	 *      isEnd = 0
	 *      国 = {<br>
	 *      	 isEnd = 1
	 *           人 = {isEnd = 0
	 *                民 = {isEnd = 1}
	 *                }
	 *           男  = {
	 *           	   isEnd = 0
	 *           		人 = {
	 *           			 isEnd = 1
	 *           			}
	 *           	}
	 *           }
	 *      }
	 *  五 = {
	 *      isEnd = 0
	 *      星 = {
	 *      	isEnd = 0
	 *      	红 = {
	 *              isEnd = 0
	 *              旗 = {
	 *                   isEnd = 1
	 *                  }
	 *              }
	 *      	}
	 *      }
	 * @author zsh
	 * @date 2021年4月20日 下午3:04:20
	 * @param keyWordSet  敏感词库
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
		String key = null;
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		//迭代keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while(iterator.hasNext()){
			key = iterator.next();    //关键字
			nowMap = sensitiveWordMap;
			for(int i = 0 ; i < key.length() ; i++){
				char keyChar = key.charAt(i);       //转换成char型
				/*去除敏感词中的空格*/
				if (Character.isSpaceChar(keyChar)){
					continue;
				}
				Object wordMap = nowMap.get(keyChar);       //获取
				if(wordMap != null){        //如果存在该key，直接赋值
					nowMap = (Map) wordMap;
				}else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<String,String>();
					newWorMap.put("isEnd", "0");     //不是最后一个
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}
				if(i == key.length() - 1){
					nowMap.put("isEnd", "1");    //最后一个
				}
			}
		}
	}

	/**
	 * 读取敏感词库中的内容，将内容添加到set集合中
	 * @author zsh
	 * @date 2021年4月20日 下午2:31:18
	 * @return
	 * @version 1.0
	 * @throws Exception
	 */

	private  HashSet<String> readSensitiveWordFromDB() throws Exception{


		List<String> wordList = getList.getWordList();
		HashSet<String> set = new HashSet<String>(wordList);

		return set;
	}

	/**
	 * @author zsh
	 * @date 2021年4月20日 下午2:28:32
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public  HashSet<String> initStopWords2(){
		try {
			//读取停顿词库,将停顿词库加入到Set中
			stopSet = readStopWordFromDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stopSet;
	}

	/**
	 * @author zsh
	 * @date 2021年4月20日 下午2:28:32
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public  String initStopWordsStr2(String zddm){
		String str = "";
		try {
			//读取停顿词库,将停顿词库加入到Set中
			return  readStopWordStrFromDB(zddm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	private  HashSet<String> readStopWordFromDB() throws Exception{
		HashSet<String> set = new HashSet<String>();
		List<String> stopList = dicService.getZdzByzddm("sensitive_cs");
		char[] stopArr = stopList.get(0).toCharArray();
		for (int i =0;i<stopArr.length;i++){
			set.add(stopArr[i]+"");
		}
		return set;
	}

	private  String readStopWordStrFromDB(String zddm) throws Exception{
		List<String> stopList = dicService.getZdzByzddm(zddm);
		String stopArr = stopList.get(0);
		return stopArr;
	}



	/**
	 * 读取敏感词库中的内容，将内容添加到set集合中
	 * @author zsh
	 * @date 2021年4月20日 下午2:31:18
	 * @return
	 * @version 1.0
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	private static HashSet<String> readSensitiveWordFile(String path) throws Exception{
		HashSet<String> set = null;
		//读取文件`
		File file = new File(path);
		InputStreamReader read = new InputStreamReader(new FileInputStream(file),ENCODING);
		try {
			if(file !=null && file.exists() && file.isFile() ){      //文件流是否存在
				set = new HashSet<String>();
				BufferedReader bufferedReader = new BufferedReader(read);
				String txt = null;
				while((txt = bufferedReader.readLine()) != null){    //读取文件，将文件内容放入到set中
					/*去除首尾空格*/
					set.add(txt);
				}
			}else{         //不存在抛出异常信息
				throw new Exception("敏感词库文件不存在");
			}
		} catch (Exception e) {
			throw e;
		}finally{
			read.close();     //关闭文件流
		}
		return set;
	}
}
