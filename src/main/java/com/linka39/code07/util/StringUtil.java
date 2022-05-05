package com.linka39.code07.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 字符串工具类
 * @author
 *
 */
public class StringUtil {

	/**
	 * 判断是否是空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str==null||"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 判断是否不是空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		if((str!=null)&&!"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 格式化模糊查询
	 * @param str
	 * @return
	 */
	public static String formatLike(String str){
		if(isNotEmpty(str)){
			return "%"+str+"%";
		}else{
			return null;
		}
	}

	/**
	 * 过滤掉集合里的空格
	 * @param list
	 * @return
	 */
	public static List<String> filterWhite(List<String> list){
		List<String> resultList=new ArrayList<String>();
		for(String l:list){
			if(isNotEmpty(l)){
				resultList.add(l);
			}
		}
		return resultList;
	}

	/**
	 * 去除html标签
	 */
	public static String stripHtml(String content) {
	    // <p>段落替换为换行
	    content = content.replaceAll("<p.*?>", "\r\n");
	    // <br><br/>替换为换行
	    content = content.replaceAll("<br\\s*/?>", "\r\n");
	    // 去掉其它的<>之间的东西
	    content = content.replaceAll("\\<.*?>", "");
	    // 去掉空格
	    content = content.replaceAll(" ", "");
	    return content;
	}

	/**
	 * 替换敏感词中的内容
	 * @param content
	 * @return
	 */
	public static String replaceStartTag(String content) {
		if (content == null || content.length() == 0) {
			return content;
		}
//		String regEx = "<[a-zA-Z]*?>([\\s\\S]*?)";
		String regEx = "<.?font>";
		content = content.replaceAll(regEx, "");
		return content;
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
	public static  String getReplaceCharsUtil(String replaceChar,int length){
		String resultReplace = replaceChar;
		for(int i = 1 ; i < length ; i++){
			resultReplace += replaceChar;
		}
		return resultReplace;
	}

	/**
	 * 生成六位随机数
	 * @return
	 */
	public static String genSixRandomNum(){
		Random random = new Random();
		String result="";
		for (int i=0;i<6;i++)
		{
			random.nextFloat();
			result+=random.nextInt(10);
		}
		return result;
	}

	public static Integer randomInteger(){
		Random random = new Random();
		return 100+random.nextInt(100);
	}

	/**
     * 生成由[A-Z,0-9]生成的随机字符串
     * @param length  欲生成的字符串长度
     * @return
     */
    public static String getRandomString(int length){
        Random random = new Random();

        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i){
            int number = random.nextInt(2);
            long result = 0;

            switch(number){
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:

                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }

	public static void main(String[] args) {
		genSixRandomNum();
    	System.out.println(StringUtil.randomInteger());
	}


}
