package com.linka39.code07.lucene;

import com.linka39.code07.entity.Article;
import com.linka39.code07.util.DateUtil;
import com.linka39.code07.util.StringUtil;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
    /**
     * 更新索引
     * @param article
     */
    public boolean updateIndex(Article article){
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
            writer.updateDocument(new Term(String.valueOf(article.getId())),doc);//根据id更新索引
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            lock.unlock();
        }
        return true;
    }
    /**
     * 删除所有帖子索引
     * @param
     */
    public boolean deleteAllIndex(){
        ReentrantLock lock=new ReentrantLock();
        //进行锁的操作，多线程下只有一个执行，完成后再执行下一个
        lock.lock();
        try{
            IndexWriter writer = getWriter();
            writer.deleteAll();
            writer.commit();//提交
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            lock.unlock();
        }
        return true;
    }
    /**
     * 按id删除帖子索引
     * @param
     */
    public boolean deleteIndex(String id){
        ReentrantLock lock=new ReentrantLock();
        //进行锁的操作，多线程下只有一个执行，完成后再执行下一个
        lock.lock();
        try{
            IndexWriter writer = getWriter();
            writer.deleteDocuments(new Term("id",id));//执行删除命令
            writer.forceMergeDeletes();//强制删除
            writer.commit();//提交
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * 查询帖子信息
     * @param q 查询关键字
     * @return
     * @throws Exception
     */
    public List<Article> search(String q)throws Exception{
        dir=FSDirectory.open(Paths.get(lucenePath));
        //实例化文件读取流
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is=new IndexSearcher(reader);
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        //设置索引搜索条件,根据名称和内容
        QueryParser parser=new QueryParser("name",analyzer);
        Query query=parser.parse(q);
        QueryParser parser2=new QueryParser("content",analyzer);
        Query query2=parser2.parse(q);
        //将查询条件添加进去,SHOULD表示两者都可以
        booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        booleanQuery.add(query2,BooleanClause.Occur.SHOULD);
        //构造查询条件，选择100条命中记录
        TopDocs hits=is.search(booleanQuery.build(), 100);
        QueryScorer scorer=new QueryScorer(query);//实例化得分
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);//实例化片段
        SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
        //设置高亮部分
        Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);
        List<Article> articleList=new LinkedList<Article>();
        //根据得分获取文档
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            Document doc=is.doc(scoreDoc.doc);
            Article article=new Article();
            //设置相关字段
            article.setId(Integer.parseInt(doc.get(("id"))));
            article.setPublishDateStr(doc.get(("publishDate")));
            String name=doc.get("name");
            //取出html
            String content= StringUtil.stripHtml(doc.get("content"));
            if(name!=null){
                TokenStream tokenStream = analyzer.tokenStream("name", new StringReader(name));
                //获取检索条件中的高光内容
                String hName=highlighter.getBestFragment(tokenStream, name);
                if(StringUtil.isEmpty(hName)){
                    article.setName(name);
                }else{
                    article.setName(hName);
                }
            }
            if(content!=null){
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
                String hContent=highlighter.getBestFragment(tokenStream, content);
                //获取检索条件中的高光内容
                if(StringUtil.isEmpty(hContent)){
                    if(content.length()<=200){
                        article.setContent(content);
                    }else{
                        //对内容截取
                        article.setContent(content.substring(0, 200));
                    }
                }else{
                    article.setContent(hContent);
                }
            }
            articleList.add(article);
        }
        return articleList;
    }

    /**
     * 查询帖子信息无高亮
     * @param q 查询关键字
     * @return
     * @throws Exception
     */
    public List<Article> searchNoHighLighter(String q)throws Exception{
        dir=FSDirectory.open(Paths.get(lucenePath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is=new IndexSearcher(reader);
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        QueryParser parser=new QueryParser("name",analyzer);
        Query query=parser.parse(q);
        QueryParser parser2=new QueryParser("content",analyzer);
        Query query2=parser2.parse(q);
        //获取无高亮内容
        booleanQuery.add(query,BooleanClause.Occur.SHOULD);
        booleanQuery.add(query2,BooleanClause.Occur.SHOULD);
        TopDocs hits=is.search(booleanQuery.build(), 20);
        List<Article> articleList=new LinkedList<Article>();
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            Document doc=is.doc(scoreDoc.doc);
            Article article=new Article();
            article.setId(Integer.parseInt(doc.get(("id"))));
            String name=doc.get("name");
            article.setName(name);
            articleList.add(article);
        }
        return articleList;
    }
}
