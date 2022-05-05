package com.linka39.code07.quartzJob;

import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.Message;
import com.linka39.code07.lucene.ArticleIndex;
import com.linka39.code07.service.ArticleService;
import com.linka39.code07.service.MessageService;
import com.linka39.code07.util.IsLinkUserfulUtil;
import com.linka39.code07.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//生成job的工厂交给spring管理
@Component
public class QuartzJobFactory extends AdaptableJobFactory {
    @Autowired
    ArticleService articleService;
    @Autowired
    MessageService messageService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ArticleIndex articleIndex;
    @Scheduled(cron = "0 0-5 20 * * ?")//每天20点0分到10分执行
    public void testScheduled(){
        System.out.println("springScheduled run:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    @Scheduled(cron = "0 0 12 * * ?")//每天中午12点触发，检查资源链接有效性
    //@Scheduled 注解内不能有参数
    public void testLinkUseful(){
        List<Article> articleList = articleService.listAll();
        //循环数组,判断链接是否有效
        Iterator<Article> it = articleList.iterator();
        Integer count = 0;
        while (it.hasNext()) {
            Article article = (Article) it.next();
            if(article.isUseful()){
                Boolean useful = IsLinkUserfulUtil.TestUserful(article.getDownload1());
                //链接失效的话
                if(!useful){
                    count++;
                    Message message = new Message();
                    message.setUser(article.getUser());
                    message.setPublishDate(new Date());
                    article.setUseful(useful);
                    // todo 消息模块要添加一个
                    message.setContent("【失效通知】:您发布的帖子《"+article.getName()+"》链接已经失效！");
                    messageService.save(message);
                    //todo 更新redis缓存10分钟时长，Index缓存，
                    redisUtil.set("article_"+article.getId(),article,10*60);
                    articleIndex.updateIndex(article);
                    articleService.save(article);
                }
            }
            count++;
        }
    }

    /*@Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        capableBeanFactory.autowireBean(jobInstance); //这一步解决不能spring注入bean的问题
        return jobInstance;
    }*/

}
