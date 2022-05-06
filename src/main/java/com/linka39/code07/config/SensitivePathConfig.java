package com.linka39.code07.config;

import com.linka39.code07.bayesianUtil.TrainSampleDataManager;
import com.linka39.code07.jctreeUtil.JCTree;
import com.linka39.code07.sensitiveUtil.SensitiveWordFilter;
import com.linka39.code07.sensitiveUtil.SensitiveWordInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.linka39.code07.sensitiveUtil.SensitiveWordFilter.sensitiveWordMap;
//纳入Spring的生命周期管理
@Component("sensitivePathConfig")
public class SensitivePathConfig {

    @Value("${sensitivePath}")
    public static String sensitivePath = "E://my_project/code07/src/main/resources/config/sensitiveWords.txt";
    @Value("${stopWordsPath}")
    public static String stopWordsPath = "E://my_project/code07/src/main/resources/config/stopWords.txt";
    @Value("${bayesianPath}")
    public static String bayesianPath = "E://my_project/code07/src/main/resources/config/bayesianFile/";
    @Value("${jctreePath}")
    public static String jctreePath = "E://my_project/code07/src/main/resources/config/jctreeFile/";

    @Autowired
    private SensitiveWordInit sensitiveWordInit;

    /**
     * 初始化敏感词库sensitiveWordMap
     */
    public  void initSensitiveWordMap(){
        if (SensitiveWordFilter.sensitiveWordMap == null) {
            SensitiveWordFilter.sensitiveWordMap = sensitiveWordInit.initKeyWord2();
        }
    }
    /**
     * 初始化停顿词库stopSet
     */
    public void initStopWord(){
        if(SensitiveWordFilter.stopSet == null){
            SensitiveWordFilter.stopSet = sensitiveWordInit.initStopWords2();
        }
    }

    /**
     * 初始化决策树JCTree.rootNode
     */
    public void initJCTree(){
        try {
            if (JCTree.rootNode == null) {
                JCTree.initJCTree();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 决策树打印测试
     */
    public void getJCTreeSuccessful(){
        try {
            JCTree.getJCTreeSuccess();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 初始化贝叶斯分类器TrainSampleDataManager.allWordsMap
     */
    public void initTextWordsMap(){
        try{
            if (TrainSampleDataManager.allWordsMap == null|| TrainSampleDataManager.allWordsMap.size()==0) {
                TrainSampleDataManager.process();
                while(TrainSampleDataManager.allWordsMap.size()==0){
                    Thread.sleep(10000);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 初始化贝叶斯分类器TrainSampleDataManager.allWordsMap
     */
    public void initSensitivePram(){
        SensitiveWordFilter.stopWordStr = sensitiveWordInit.initStopWordsStr2("sensitive_cs");
        SensitiveWordFilter.replaceChar = sensitiveWordInit.initStopWordsStr2("sensitive_replace_char");
        SensitiveWordFilter.accessLevel = sensitiveWordInit.initStopWordsStr2("sensitive_min_class");
        SensitiveWordFilter.sensiMatchType = Integer.parseInt(sensitiveWordInit.initStopWordsStr2("sensitive_math_type"));
    }

    /**
     * 初始化贝叶斯分类器TrainSampleDataManager.allWordsMap
     */
    public void initIKSegmenter(){
        System.out.println("_____________");
    }
}
