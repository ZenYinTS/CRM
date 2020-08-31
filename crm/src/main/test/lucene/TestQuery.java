package lucene;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;


//创建索引的测试类
public class TestQuery {

    private String path = "index/query";
    private Version version = Version.LUCENE_4_10_4;
    //文档
    private String content1 = "hello world";
    private String content2 = "hello java world";
    private String content3 = "hello lucene aaa world";

    @Test
    public void testCreate() throws Exception {
        //1. 定义lucene存放文件的位置
        Directory d = FSDirectory.open(new File(path));
        //2. 需要配置对象——分词器
        Analyzer analyzer = new StandardAnalyzer();
        //3. 配置对象
        IndexWriterConfig conf = new IndexWriterConfig(version,analyzer);
        //每次运行都重新创建索引库
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(d,conf);

        //4. 往库里写内容
        FieldType type = new FieldType();
        type.setStored(true);
        type.setIndexed(true);

        //创建文档对象
        Document doc1 = new Document();
        doc1.add(new Field("title","doc1",type));
        doc1.add(new Field("content",content1,type));
        doc1.add(new Field("inputtime","20200828",type));
        writer.addDocument(doc1);

        Document doc2 = new Document();
        doc2.add(new Field("title","doc2",type));
        doc2.add(new Field("content",content2,type));
        doc2.add(new Field("inputtime","20200829",type));
        writer.addDocument(doc2);

        Document doc3 = new Document();
        doc3.add(new Field("title","doc3",type));
        doc3.add(new Field("content",content3,type));
        doc3.add(new Field("inputtime","20200830",type));
        writer.addDocument(doc3);

        //5. 提交内容
        writer.commit();

        //6. 关闭资源
        writer.close();
    }


    //根据字符串查询【支持通配符】
    public void testSearcher(String parseStr) throws Exception{
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
        Query query = parser.parse(parseStr);
        //查看使用了哪个子类
        System.out.println(query.getClass());
        //第二个参数表示，符合条件的前n条记录
        TopDocs tds = searcher.search(query,10000);
        System.out.println("总命中数："+tds.totalHits);

        ScoreDoc[] scoreDocs = tds.scoreDocs;
        ScoreDoc scoreDoc = null;
        Document doc = null;
        for (int i = 0; i < scoreDocs.length; i++) {
            scoreDoc = scoreDocs[i];
            doc = searcher.doc(scoreDoc.doc);
            System.out.println("文档分数："+scoreDoc.score+" 文档编号："+scoreDoc.doc+
                    " title >>>>>>> "+doc.get("title")+" content >>>>>>> "+doc.get("content")
                    +" inputtime >>>>>>> "+doc.get("inputtime"));
            System.out.println("------------------------------");
        }
    }

    //根据传入的查询条件查询
    public void testSearcher(Query query) throws Exception{
        //1. 定义lucene存放文件的位置
        Directory d = FSDirectory.open(new File(path));
        //2. 添加Reader
        IndexReader r = DirectoryReader.open(d);
        //3. 创建读取对象
        IndexSearcher searcher = new IndexSearcher(r);

        //第二个参数表示，符合条件的前n条记录
        TopDocs tds = searcher.search(query,10000);
        System.out.println("总命中数："+tds.totalHits);

        ScoreDoc[] scoreDocs = tds.scoreDocs;
        ScoreDoc scoreDoc = null;
        Document doc = null;
        for (int i = 0; i < scoreDocs.length; i++) {
            scoreDoc = scoreDocs[i];
            doc = searcher.doc(scoreDoc.doc);
            System.out.println("文档分数："+scoreDoc.score+" 文档编号："+scoreDoc.doc
                    + " title >>>>>>> "+doc.get("title")+" content >>>>>>> "+doc.get("content")
                    +" inputtime >>>>>>> "+doc.get("inputtime"));
            System.out.println("------------------------------");
        }
    }

    //1. 查询所有记录【两种方式】
    @Test
    public void testAll() throws Exception {
        //方式1：使用通配符
        testSearcher("*:*");
        System.out.println("***********************************");
        //方式二：使用子类
        MatchAllDocsQuery query = new MatchAllDocsQuery();
        testSearcher(query);

    }

    //2. 单词查询【两种方式】
    @Test
    public void testTermQuery() throws Exception {
        //方式1：字符串查询
        testSearcher("title:doc2");
        System.out.println("***********************************");
        //方式二：使用子类
        TermQuery query = new TermQuery(new Term("title", "doc2"));
        testSearcher(query);
    }

    //3. 短语查询【两种方式】
    @Test
    public void testPhraseQuery() throws Exception {
        //方式1：字符串查询
        //包裹在双引号中的内容不再被分词器分割，将作为整体进行查询
        testSearcher("\"hello world\"");
        System.out.println("***********************************");
        //方式二：使用子类
        PhraseQuery query = new PhraseQuery();
        query.add(new Term("content","hello"));
        query.add(new Term("content","world"));
        testSearcher(query);
    }

    //4. 通配符查询【两种方式】
    //*  ----  匹配任意多个
    //?  ----  匹配单个
    @Test
    public void testWildcardQuery() throws Exception {
        //方式1：字符串查询
        testSearcher("lu*n?");
        System.out.println("***********************************");
        //方式二：使用子类
        WildcardQuery query = new WildcardQuery(new Term("content","lu*n?"));
        testSearcher(query);
    }


    //5. 单词模糊查询【两种方式】
    //~N:表示最多可以错N个字符，N最大为2
    @Test
    public void testFuzzyQuery() throws Exception {
        //方式1：字符串查询
        testSearcher("luceXX~2");
        System.out.println("***********************************");
        //方式二：使用子类
        FuzzyQuery query = new FuzzyQuery(new Term("content","luceXX"),2);
        testSearcher(query);
    }

    //6. 段落模糊查询【一种方式】
    //~N：表示段落中间最多可以插入n个单词，n不限大小
    @Test
    public void testPhraseQuery2() throws Exception {
        testSearcher("\"hello world\"~1");
        System.out.println("***********************************");
//        PhraseQuery query = new PhraseQuery();
//        query.add(new Term("content","hello"));
//        query.add(new Term("content","world"));
//        testSearcher(query);
    }

    //7. 范围查询【两种方式】
    @Test
    public void testTermRangeQuery() throws Exception {
        //{：左开区间
        //]：右闭区间
        testSearcher("inputtime:{20200827 TO 20200830]");
        System.out.println("***********************************");
        TermRangeQuery query = new TermRangeQuery("inputtime",new BytesRef("20200827"),
                new BytesRef("20200830"),false,false);
        testSearcher(query);
    }
}