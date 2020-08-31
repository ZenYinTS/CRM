package lucene;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
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
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;


//创建索引的测试类
public class TestIK {
    private String path = "index/ik";
    private Version version = Version.LUCENE_4_10_4;
    //文档
    private String str = "测试yidz的一个String字符串by ik";

    @Test
    public void testIK() throws Exception {

        Analyzer analyzer = new IKAnalyzer();
        //填写解析哪个字段，因为这里没有规定字段，可以随便写一个
        TokenStream stream = analyzer.tokenStream("c", str);
        stream.reset();    //将读取流的指针移动到最前面
        while (stream.incrementToken()){
            System.out.println(stream);
        }
    }
}