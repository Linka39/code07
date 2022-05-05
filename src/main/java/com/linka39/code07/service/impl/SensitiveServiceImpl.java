package com.linka39.code07.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.linka39.code07.bayesianUtil.ChineseTokenizer;
import com.linka39.code07.bayesianUtil.MultinomialModelNaiveBayes;
import com.linka39.code07.bayesianUtil.TrainSampleDataManager;
import com.linka39.code07.config.SensitivePathConfig;
import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.User;
import com.linka39.code07.jctreeUtil.JCTree;
import com.linka39.code07.sensitiveUtil.FileUtils;
import com.linka39.code07.sensitiveUtil.SensitiveWordFilter;
import com.linka39.code07.sensitiveUtil.SensitiveWordInit;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.service.SensitiveService;
import com.linka39.code07.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static com.linka39.code07.config.SensitivePathConfig.*;

/**
 * 资源类别Service实现类
 */
@Transactional
@Service("sensitiveService")
public class SensitiveServiceImpl implements SensitiveService {

    @Autowired
    private SensitivePathConfig sensitivePathConfig;

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;

//    @Override
//    public JSONObject sensitiveWordWrite(String text, String classify) {
//        JSONObject json = new JSONObject();
//        json.put("code", "1");
//        ReentrantLock lock=new ReentrantLock();
//        lock.lock();
//        try {
//            if (text.length() != 0) {
//                int line = FileUtils.haveSensitiveWord(sensitivePath, text);
//                if(line > 0){
//                    json.put("code", "-101");
//                    json.put("result", "敏感词库已有："+text+line);
//                }else{
//                    FileUtils.continueWrite(sensitivePath, text);
//                    if(classify != null)
//                        FileUtils.continueWrite(bayesianPath + classify, text);
//                    json.put("result", "追加成功");
//                    SensitiveWordFilter.sensitiveWordMap = null;
//                }
//            } else {
//                json.put("code", "-102");
//                json.put("result", "不能追加空值");
//            }
//
//        } catch (Exception e) {
//            json.put("code", "-103");
//            json.put("result", "追加失败");
//        }finally {
//            lock.unlock();
//        }
//        return json;
//    }
//
//    @Override
//    public JSONObject sensitiveWordDelete(String text) {
//        JSONObject json = new JSONObject();
//        json.put("code", "1");
//        ReentrantLock lock=new ReentrantLock();
//        lock.lock();
//        try {
//            ArrayList<String> arrayList= FileUtils.search(new File(bayesianPath), text);
//
//            int line = FileUtils.haveSensitiveWord(sensitivePath, text);
//            if(line > 0){
//                FileUtils.deleteSensitiveWordFile(sensitivePath, text);
//                for(int i =0;i<arrayList.size();i++){
//                    FileUtils.deleteSensitiveWordFile(arrayList.get(i), text);
//                }
//                json.put("result", "删除成功");
//                SensitiveWordFilter.sensitiveWordMap = null;
//            }else{
//                json.put("code", "-101");
//                json.put("result", "敏感词库没有："+text);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            json.put("code", "-103");
//            json.put("result", "删除失败");
//        }finally {
//            lock.unlock();
//        }
//        return json;
//    }

    @Override
    public JSONObject sensitiveWordExist(String text) {
        JSONObject json = new JSONObject();
        json.put("code", "1");
        sensitivePathConfig.initStopWord();
        sensitivePathConfig.initSensitiveWordMap();

        Set<String> set = SensitiveWordFilter.getSensitiveWord(text, SensitiveWordFilter.sensiMatchType);
        if (set.size() == 0) {
            json.put("code", "-101");
        }else{
            json.put("result", set.toString());
        }
        return json;
    }

    @Override
    public JSONObject getArticleSensitiveWord(String text) {
        JSONObject json = new JSONObject();
        json.put("code", "1");
        sensitivePathConfig.initStopWord();
        sensitivePathConfig.initSensitiveWordMap();
        /*去除全部空格*/
        text = SensitiveWordFilter.stringConvert2(text);
        Map<String, Long> temp_words = new LinkedHashMap<String, Long>();
        temp_words = ChineseTokenizer.segStr(text);

        Map<String, Long> sensitiveMap = SensitiveWordFilter.getSensitiveWordToMap2(temp_words, SensitiveWordFilter.sensiMatchType);

//字数,敏感词数/词数,敏感词情感分类,用户发表文章数,用户年龄，用户积分,敏感词评级

        //字数
        int textNum = temp_words.size();
        Set<String> temp_wordsSet=temp_words.keySet();
        for(String words : temp_wordsSet){
            int wordNum =temp_words.get(words).intValue();
            textNum += wordNum;
        }

        //敏感词数/词数
        Set<String> classifierSet=sensitiveMap.keySet();
        int sensiTotalNum = 0;
        for(String classifier : classifierSet){
            int wordNum =sensitiveMap.get(classifier).intValue();
            sensiTotalNum += wordNum;
        }
        sensitiveMap = SensitiveWordFilter.SortMap(sensitiveMap,sensitiveMap.size());
        Float sensitiveRate = sensiTotalNum*1.0f/textNum*1.0f;
        System.out.println(sensiTotalNum+"/"+textNum+" = "+sensitiveRate);

        //敏感词情感分类
        System.out.println(sensitiveMap.toString());

        //用户发表文章数,用户年龄，用户积分
        if (sensitiveMap.size() == 0) {
            json.put("code", "-101");
        }else{
            json.put("sensitiveMap", sensitiveMap);
            json.put("textNum", textNum);
            json.put("sensiTotalNum", sensiTotalNum);
        }
        return json;
    }

    @Override
    public JSONObject getUserAttr(Integer userId) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", "1");
            User user = userService.findById(userId);
            Article s_article = new Article();
            s_article.setUser(user);
            Long total = articleService.getTotal(s_article);

            long startDateTime = new Date().getTime();
            long endDateTime = user.getRegisterDate().getTime();
            int days =  (int)((endDateTime - startDateTime) / (1000 * 3600 * 24));

            json.put("registerdays", days);
            json.put("articletotals", total);
            json.put("points", user.getPoints());
        }catch (Exception e){
            e.printStackTrace();
            json.put("code", "-103");
            json.put("result", "删除失败");
        }
        return json;
    }

    @Override
    public JSONObject formatUserAttr(Integer userId) {
        String[] params = new String[] { "med","med","2","4","big","high"};
        JSONObject json = new JSONObject();
        json.put("code", "1");
        try {
            System.out.println("测试数据："+ Arrays.toString(params));
            if (JCTree.rootNode == null) {
                JCTree.initJCTree();
            }
            Object object = JCTree.getResult(JCTree.rootNode , JCTree.rootShuXing, params);//uacc ,acc, good,vgood
            User user = userService.findById(userId);
            Article s_article = new Article();
            s_article.setUser(user);
            Long total = articleService.getTotal(s_article);

            long startDateTime = new Date().getTime();
            long endDateTime = user.getRegisterDate().getTime();
            int days =  (int)((endDateTime - startDateTime) / (1000 * 3600 * 24));
            json.put("registerdays", days);
            json.put("articletotals", total);
            json.put("points", user.getPoints());
        }catch (Exception e){
            e.printStackTrace();
            json.put("code", "-103");
            json.put("result", "删除失败");
        }

        return json;
    }

    @Override
    public JSONObject sensitiveWordClassify(ArrayList<String> words) {
        JSONObject json = new JSONObject();
        json.put("code", "1");
        try {
            if (TrainSampleDataManager.allWordsMap == null) {
                TrainSampleDataManager.process();
            }
            Map<String, BigDecimal> resultMap= MultinomialModelNaiveBayes.classifyResult(words);
            Set<String> set=resultMap.keySet();
            for(String str: set){
                System.out.println("classifer:"+str+"     probability:"+resultMap.get(str));
            }
            resultMap = MultinomialModelNaiveBayes.SortMap(resultMap,resultMap.size());
            System.out.println(resultMap.toString());


        }catch (Exception e){
            e.printStackTrace();
            json.put("code", "-103");
            json.put("result", "删除失败");
        }

        return json;
    }

    //insertMod导入模式，其中1为覆盖，2为插入
    @Override
    public JSONObject sensitiveDataWrite(LinkedList<String[]> sxList, int insertMod) {
        JSONObject json = new JSONObject();
        json.put("code", "1");
        StringBuilder tempString = new StringBuilder();
        ReentrantLock lock=new ReentrantLock();
        lock.lock();
        try {
            if (sxList.size() != 0) {
                if(insertMod == 1 ) {//先清空原有数据
                    FileUtils.clearInfoForFile(jctreePath + "dataBase.txt");
                    tempString.append("count,sensitive,emotion,papers,days,points,level\n");
                }
                for(String[] myStr:sxList){
                    for(int i = 0; i<myStr.length;i++){
                        tempString.append(myStr[i]);
                        if(i!=myStr.length-1){
                            tempString.append(',');
                        }
                    }
                    tempString.append('\n');
                }
                FileUtils.continueWrite(jctreePath + "dataBase.txt", tempString.toString());
                json.put("result", "添加成功");
            } else {
                json.put("code", "-102");
                json.put("result", "不能添加加空值");
            }

        } catch (Exception e) {
            json.put("code", "-103");
            json.put("result", "追加失败");
        }finally {
            lock.unlock();
        }
        return json;
    }
}
