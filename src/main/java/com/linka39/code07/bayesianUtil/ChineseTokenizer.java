package com.linka39.code07.bayesianUtil;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

/**
 *
 * <p>Title:ChineseTokenizer</p>
 * <p>Description: 中文分词,采用es-ik实现
 * </p>
 * @createDate：2013-8-30
 * @author xq
 * @version 1.0
 */
public class ChineseTokenizer {
    /**
     *
    * @Title: segStr
    * @Description: 返回LinkedHashMap的分词
    * @param @param content
    * @param @return
    * @return Map<String,Integer>
    * @throws
     */
    public static Map<String, Long> segStr(String content){
        // 分词
        Reader input = new StringReader(content);
        // 智能分词关闭（对分词的精度影响很大）
        IKSegmenter iks = new IKSegmenter(input, true);
        Lexeme lexeme = null;
        Map<String, Long> words = new LinkedHashMap<String, Long>();
        try {
            while ((lexeme = iks.next()) != null) {
                // 禁用默认词典，只用自定义词典
                // 1.默认词典设为停用词典
                // 2.getLexemeType为64的直接跳过
                int nType = lexeme.getLexemeType();
                if (nType == 64) {
                    continue;
                }
                if (words.containsKey(lexeme.getLexemeText())) {
                    words.put(lexeme.getLexemeText(), words.get(lexeme.getLexemeText()) + 1);
                } else {
                    words.put(lexeme.getLexemeText(), 1L);
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static Map<String, Long> segStrSplit(String content,String split){
        Map<String, Long> words = new LinkedHashMap<String, Long>();
        try {
            String[] strArr = content.split(split);  //\r\n
            for (String str:strArr) {
                if (words.containsKey(str)){
                    words.put(str, words.get(str) + 1);
                } else {
                    words.put(str, 1L);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return words;
    }

    /**
     *
     * @Title: segStr
     * @Description: 从大到小排序，返回LinkedHashMap的分词
     * @param @param content
     * @param @return
     * @return Map<String,Integer>
     * @throws
     */
    public static Map<String, Long> segStr(String content, Integer num){
        // 分词
        Reader input = new StringReader(content);
        // 智能分词关闭（对分词的精度影响很大）
        IKSegmenter iks = new IKSegmenter(input, true);
        Lexeme lexeme = null;
        Map<String, Long> words = new LinkedHashMap<String, Long>();
        Map<String, Long> new_words = new LinkedHashMap<String, Long>();
        try {
            while ((lexeme = iks.next()) != null) {
                if (words.containsKey(lexeme.getLexemeText())) {
                    words.put(lexeme.getLexemeText(), words.get(lexeme.getLexemeText()) + 1);
                } else {
                    words.put(lexeme.getLexemeText(), 1L);
                }
            }
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

        }catch(IOException e) {
            e.printStackTrace();
        }
        return new_words;
    }
}
