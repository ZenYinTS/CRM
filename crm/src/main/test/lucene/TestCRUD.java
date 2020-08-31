package lucene;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;

import java.io.File;
import java.io.IOException;


//创建索引的测试类
public class TestCRUD {

    private String path = "index/crud";
    private Version version = Version.LUCENE_4_10_4;
    //文档
    private String content1 = "hello world";
    private String content2 = "hello java world";
    private String content3 = "hello lucene world";
    private String updateContent = "update content";

    @Test
    public void testCreate() throws Exception {
        //1. 定义lucene存放文件的位置
        Directory d = FSDirectory.open(new File(path));
        //2. 需要配置对象——分词器
        Analyzer analyzer = new StandardAnalyzer();
        //3. 配置对象
        IndexWriterConfig conf = new IndexWriterConfig(version,analyzer);
        IndexWriter writer = new IndexWriter(d,conf);

        //4. 往库里写内容
        FieldType type = new FieldType();
        type.setStored(true);
        type.setIndexed(true);

        //创建文档对象
        Document doc1 = new Document();
        doc1.add(new Field("title","doc1",type));
        doc1.add(new Field("content",content1,type));
        writer.addDocument(doc1);

        Document doc2 = new Document();
        doc2.add(new Field("title","doc2",type));
        doc2.add(new Field("content",content2,type));
        writer.addDocument(doc2);

        Document doc3 = new Document();
        doc3.add(new Field("title","doc3",type));
        doc3.add(new Field("content",content3,type));
        writer.addDocument(doc3);

        //5. 提交内容
        writer.commit();

        //6. 关闭资源
        writer.close();
    }

    @Test
    public void testUpdate() throws Exception {
        //1. 定义lucene存放文件的位置
        Directory d = FSDirectory.open(new File(path));
        //2. 需要配置对象——分词器
        Analyzer analyzer = new StandardAnalyzer();
        //3. 配置对象
        IndexWriterConfig conf = new IndexWriterConfig(version,analyzer);
        IndexWriter writer = new IndexWriter(d,conf);

        //4. 往库里写内容
        FieldType type = new FieldType();
        type.setStored(true);
        type.setIndexed(true);

        //创建文档对象
        Document updateDoc = new Document();
        updateDoc.add(new Field("title","doc2",type));
        updateDoc.add(new Field("content",updateContent,type));
        //第一个参数为更新条件：更新title为doc2的文档
        //第二个参数为更新内容
        writer.updateDocument(new Term("title","doc2"),updateDoc);
        //5. 提交内容
        writer.commit();
        //6. 关闭资源
        writer.close();
        testSearcher();
    }

    //根据传入的标题删除
    @Test
    public void testDelete1() throws Exception{
        //1. 定义lucene存放文件的位置
        Directory d = FSDirectory.open(new File(path));
        //2. 需要配置对象——分词器
        Analyzer analyzer = new StandardAnalyzer();
        //3. 配置对象
        IndexWriterConfig conf = new IndexWriterConfig(version,analyzer);
        IndexWriter writer = new IndexWriter(d,conf);

        writer.deleteDocuments(new Term("title","doc1"));

        writer.commit();
        writer.close();
        testSearcher();
    }

    //根据查询条件，删除包含有该查询字段的文档
    @Test
    public void testDelete2() throws Exception{
        //1. 定义lucene存放文件的位置
        Directory d = FSDirectory.open(new File(path));
        //2. 需要配置对象——分词器
        Analyzer analyzer = new StandardAnalyzer();
        //3. 配置对象
        IndexWriterConfig conf = new IndexWriterConfig(version,analyzer);
        IndexWriter writer = new IndexWriter(d,conf);

        QueryParser parser = new QueryParser("content",analyzer);
        Query query = parser.parse("lucene");
        writer.deleteDocuments(query);

        writer.commit();
        writer.close();
        testSearcher();
    }

    @Test
    public void testSearcher() throws Exception{
        //1. 定义lucene存放文件的位置
        Directory d = FSDirectory.open(new File(path));
        //2. 添加Reader
        IndexReader r = DirectoryReader.open(d);
        //3. 创建读取对象
        IndexSearcher searcher = new IndexSearcher(r);
        //第一个参数表示：在哪个字段查询内容
        //第二个参数：分词对象
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("content",analyzer);
        Query query = parser.parse("hello");
        //第二个参数表示，符合条件的前n条记录
        TopDocs tds = searcher.search(query,10000);
        System.out.println("总命中数："+tds.totalHits);

        ScoreDoc[] scoreDocs = tds.scoreDocs;
        ScoreDoc scoreDoc = null;
        Document doc = null;
        for (int i = 0; i < scoreDocs.length; i++) {
            scoreDoc = scoreDocs[i];
            System.out.println("文档分数："+scoreDoc.score);
            System.out.println("文档编号："+scoreDoc.doc);
            doc = searcher.doc(scoreDoc.doc);
            System.out.println("title >>>>>>> "+doc.get("title"));
            System.out.println("content >>>>>>> "+doc.get("content"));
            System.out.println("------------------------------");
        }
    }


}