package casia.isi.zdr.lucene.local;

import org.apache.log4j.PropertyConfigurator;
import org.apache.lucene.index.Term;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
/**
 * 　　　　　　　 ┏┓       ┏┓+ +
 * 　　　　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　 ┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 █████━█████  ┃+
 * 　　　　　　　┃　　　　　　 ┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　 ┃ + +
 * 　　　　　　　┗━━┓　　　 ┏━┛
 * ┃　　  ┃
 * 　　　　　　　　　┃　　  ┃ + + + +
 * 　　　　　　　　　┃　　　┃　Code is far away from     bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ +
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 */

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.zdr.lucene.local
 * @Description: TODO(全文索引工具测试)
 * @date 2019/4/17 11:15
 */
public class LocalIndexTest {
    private static LocalIndex localIndex = new LocalIndex();

    String indexDoc = "data/index";
    String rawDoc = "data/raw";

    @Test
    public void createIndex() {
        try {
            localIndex.createIndex(indexDoc, rawDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void matchAllDocsQuery() {
        try {
            localIndex.matchAllDocsQuery(indexDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void termQuery() {
        try {
            Term term = new Term("fileName", "linkin.csv");
            localIndex.termQuery(indexDoc, term);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void numericRangeQuery() {
        try {
            localIndex.numericRangeQuery(indexDoc, "fileSize", 41L, 205875L, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void booleanQuery() {
        try {
            localIndex.booleanQuery(indexDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryParser() {
        try {
            localIndex.queryParser(indexDoc, "fileContent:apache");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void MultiFieldQueryParser() {
        try {
            localIndex.MultiFieldQueryParser(indexDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteAllIndex() {
        try {
            localIndex.deleteAllIndex(indexDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}