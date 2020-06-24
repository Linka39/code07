package com.linka39.code07.lucene;

import com.linka39.code07.entity.Article;
import com.linka39.code07.util.DateUtil;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

@Component("articleIndex")//spring扫描注入
public class ArticleIndex {
    @Value("${lucenePath}")
    private String lucenePath;

    private Directory dir = null;

    /**
     * 获取Luncen.IndexWriter实例，进行写入
     * @return
     * @throws Exception
     */
    public IndexWriter getWriter() throws Exception{
        dir= FSDirectory.open(Paths.get(lucenePath));//获取目录
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig iwc =new IndexWriterConfig(analyzer);//配置分析器
        IndexWriter writer = new IndexWriter(dir,iwc);//获取IndexWriter实例
        return writer;
    }

    /**
     * 添加写入索引
     * @param article
     */
    public boolean addIndex(Article article){
        ReentrantLock lock=new ReentrantLock();
        //进行锁的操作，多线程下只有一个执行，完成后再执行下一个
        lock.lock();
        try{
            IndexWriter writer = getWriter();
            Document doc = new Document();
            //存储索引，值，方式(是否存入)
            doc.add(new StringField("id",String.valueOf(article.getId()), Field.Store.YES));
            doc.add(new TextField("name",article.getName(),Field.Store.YES));
            doc.add(new StringField("publishDate", DateUtil.formatDate(new Date(),"yyyy-MM-dd"),Field.Store.YES));
            doc.add(new TextField("content",article.getContent(),Field.Store.YES));
            writer.addDocument(doc);
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            lock.unlock();
        }
        return true;
    }
}
