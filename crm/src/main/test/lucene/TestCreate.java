package lucene;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;


import java.io.File;


//创建索引的测试类
public class TestCreate {

    private String path = "index";
    private Version version = Version.LUCENE_4_10_4;
    //文档
    private String content1 = "hello world";
    private String content2 = "hello java world";
    private String content3 = "hello lucene world";

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