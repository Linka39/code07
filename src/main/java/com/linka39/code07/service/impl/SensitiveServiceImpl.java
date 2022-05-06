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
            json.put("result", set);
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
//        text = SensitiveWordFilter.stringConvert2(text);
        Map<String, Long> temp_words = new LinkedHashMap<String, Long>();

//        Map<String, Long> sensitiveMap = SensitiveWordFilter.getSensitiveWordToMap2(temp_words, SensitiveWordFilter.sensiMatchType);
        Map<String,Map<String, Integer>> sensitiveMap = SensitiveWordFilter.getSensitiveWordToMap(text, SensitiveWordFilter.sensiMatchType);
        Map<String, Integer> native_sensitiveMap = sensitiveMap.get("native_words");
        if(native_sensitiveMap.size()==0){
            json.put("code", "-120");
            json.put("result","没有敏感词");
            return json;
        }
        temp_words = ChineseTokenizer.segStr(text);

        //字数,敏感词数/词数,敏感词情感分类,用户发表文章数,用户年龄，用户积分,敏感词评级

        //字数
        int textNum = temp_words.size();
        Set<String> temp_wordsSet=temp_words.keySet();
        for(String words : temp_wordsSet){
            int wordNum =temp_words.get(words).intValue();
            textNum += wordNum;
        }

        //敏感词数/词数
        Set<String> classifierSet= native_sensitiveMap.keySet();
        int sensiTotalNum = 0;
        ArrayList<String> native_sensitiveList = new ArrayList<>();
        for(String classifier : classifierSet){
            int wordNum = native_sensitiveMap.get(classifier);
            sensiTotalNum += wordNum;
            while(wordNum>0){
                native_sensitiveList.add(classifier);
                wordNum--;
            }
        }
        Float sensitiveRate = sensiTotalNum*1.0f/textNum*1.0f;
        System.out.println(sensiTotalNum+"/"+textNum+" = "+sensitiveRate);

        //敏感词情感分类
        String classifyName = "";
        JSONObject sensitiveWordClassify = this.sensitiveWordClassify(native_sensitiveList);
        if(sensitiveWordClassify.getIntValue("code")==1){
            classifyName = sensitiveWordClassify.getString("result");
            json.put("sensitiveMap", sensitiveMap);
            json.put("count", textNum);
            json.put("sensitiveNum", sensiTotalNum);
            json.put("sensitive",sensitiveRate);
            json.put("emotion",classifyName);
        }else{
            json.put("code", "-101");
            json.put("result",sensitiveWordClassify.getString("result"));
        }
        return json;
    }

    @Override
    public JSONObject getUserAttr(Integer userId) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", "1");
            //用户发表文章数,用户年龄，用户积分
            User user = userService.findById(userId);
            Article s_article = new Article();
            s_article.setUser(user);
            Long total = articleService.getTotal(s_article);

            long endDateTime = new Date().getTime();
            long startDateTime = user.getRegisterDate().getTime();
            int days =  (int)((endDateTime - startDateTime) / (1000 * 3600 * 24));

            json.put("days", days);
            json.put("papers", total);
            json.put("points", user.getPoints());
        }catch (Exception e){
            e.printStackTrace();
            json.put("code", "-103");
            json.put("result", "删除失败");
        }
        return json;
    }

    /**
     * count	文章词数量 v-high[5000,+), high[3000,5000), med[500,3000), low[0,500)
     * sensitive	敏感词数/词数，v-high[0.2,+), high[0.1,0.2), med[0.05,0.1), low[0,0.05)
     * emotion	敏感词情感分类，1,2, 3, 4, 5
     * papers	用户发表文章数，low[0,5), med[5,30), high[30,+)
     * days	用户年龄，small[0,15), med[15,100), big[100,+)
     * points	用户积分，low[0,10), med[10,50), high[50,+)
     * level	敏感词评级，four, three, two, one
     */
    @Override
    public String formatUserAttr(JSONObject obj) {
        String formatStr = "";
        int tempValue = -1;
        //ccount	文章词数量格式化 v-high[5000,+), high[3000,5000), med[500,3000), low[0,500)
        if(obj.containsKey("count")){
            tempValue = obj.getIntValue("count");
            if(tempValue >= 5000)
                formatStr += "vhigh";
            else if(tempValue >= 3000 && tempValue < 5000)
                formatStr += "high";
            else if(tempValue >= 500 && tempValue < 3000)
                formatStr += "med";
            else if(tempValue >= 0 && tempValue < 500)
                formatStr += "low";
            formatStr += ";";
        }else{
            formatStr += "-;";
        }
        //sensitive	敏感词数/词数格式化，v-high[0.2,+), high[0.1,0.2), med[0.05,0.1), low[0,0.05)
        if(obj.containsKey("sensitive")){
            tempValue = obj.getIntValue("sensitive");
            if(tempValue >= 0.2)
                formatStr += "vhigh";
            else if(tempValue >= 0.1 && tempValue < 0.2)
                formatStr += "high";
            else if(tempValue >= 0.05 && tempValue < 0.1)
                formatStr += "med";
            else if(tempValue >= 0 && tempValue < 0.05)
                formatStr += "low";
            formatStr += ";";
        }else{
            formatStr += "-;";
        }
        //emotion	敏感词情感分类，1, 2, 3, 4, 5
        if(obj.containsKey("emotion")){
            String tempstr = obj.getString("emotion");
            formatStr += tempstr.substring(tempstr.length() -1)+";";
        }else{
            formatStr += "-;";
        }
        //papers	用户发表文章数，low[0,5), med[5,30), high[30,+)
        if(obj.containsKey("papers")){
            tempValue = obj.getIntValue("papers");
            if(tempValue >= 30)
                formatStr += "high";
            else if(tempValue >= 5 && tempValue < 30)
                formatStr += "med";
            else if(tempValue >= 5)
                formatStr += "low";
            formatStr += ";";
        }else{
            formatStr += "-;";
        }
        //days	用户年龄，small[0,15), med[15,100), big[100,+)
        if(obj.containsKey("days")){
            tempValue = obj.getIntValue("days");
            if(tempValue >= 100)
                formatStr += "big";
            else if(tempValue >= 15 && tempValue < 100)
                formatStr += "med";
            else if(tempValue >= 0 && tempValue < 15)
                formatStr += "small";
            formatStr += ";";
        }else{
            formatStr += "-;";
        }
        //points	用户积分，low[0,10), med[10,50), high[50,+)
        if(obj.containsKey("points")){
            tempValue = obj.getIntValue("points");
            if(tempValue >= 50)
                formatStr += "high";
            else if(tempValue >= 10 && tempValue < 50)
                formatStr += "med";
            else if(tempValue >= 0 && tempValue < 10)
                formatStr += "low";
        }else{
            formatStr += "-";
        }
        return formatStr;

    }

    @Override
    public String getJctreeAttr(String formatStr) {
//        String[] params = new String[] { "med","med","2","4","big","high"};
        String[] params = formatStr.split(";");
        String jctreeResult = null;
        try {
            System.out.println("测试数据："+ Arrays.toString(params));
            if (JCTree.rootNode == null) {
                JCTree.initJCTree();
            }
            jctreeResult = (String)JCTree.getResult(JCTree.rootNode , JCTree.rootShuXing, params);//uacc ,acc, good,vgood
        }catch (Exception e){
            e.printStackTrace();
        }

        return jctreeResult;
    }

    @Override
    public JSONObject sensitiveWordClassify(ArrayList<String> words) {
        JSONObject json = new JSONObject();
        json.put("code", "1");
        try {
            sensitivePathConfig.initTextWordsMap();
            Map<String, BigDecimal> resultMap= MultinomialModelNaiveBayes.classifyResult(words);
            Set<String> set=resultMap.keySet();
            for(String str: set){
                System.out.println("classifer:"+str+"     probability:"+resultMap.get(str));
            }
//            resultMap = MultinomialModelNaiveBayes.SortMap(resultMap,resultMap.size());
            String classifyName = MultinomialModelNaiveBayes.getClassifyResultName(resultMap);
//            System.out.println(resultMap.toString());
            json.put("result", classifyName);

        }catch (Exception e){
            e.printStackTrace();
            json.put("code", "-103");
            json.put("result", "分类失败");
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
