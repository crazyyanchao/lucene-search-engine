package casia.isi.zdr.lucene.local;
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

import casia.isi.zdr.util.FileUtil;
import casia.isi.zdr.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.zdr.lucene.local
 * @Description: TODO(索引本地文件并检索)
 * @date 2019/4/17 10:02
 */
public class LocalIndex {

    /**
     * 指定一个标准分析器，对文档内容进行分析
     * 搜索使用的分析器要和索引使用的分析器一致，不然搜索出来结果可能会错乱
     **/
    // 标准分词器
    // private static Analyzer analyzer = new StandardAnalyzer();

    // 中文分词器
    private static Analyzer analyzer = new IKAnalyzer();

    /**
     * 　  第一步：创建一个indexwriter对象。
     * <p>
     * 　　　　1）指定索引库的存放位置Directory对象
     * <p>
     * 　　　　2）指定一个分析器，对文档内容进行分析。
     * <p>
     * 　　第二步：创建document对象。
     * <p>
     * 　　第三步：创建field对象，将field添加到document对象中。
     * <p>
     * 　　第四步：使用indexwriter对象将document对象写入索引库，此过程进行索引创建。并将索引和document对象写入索引库。
     * <p>
     * 　　第五步：关闭IndexWriter对象。
     *
     * @param indexDbPath:索引库的位置
     * @param rawFilePath:原始文档的位置
     */
    public void createIndex(String indexDbPath, String rawFilePath) throws Exception {
        // 指定索引库的存放位置Directory
        Directory directory = (Directory) FSDirectory.open(new File(indexDbPath).toPath());

        // 索引库还可以存放到内存
        // Directory directory = new RAMDirectory();

        // 创建indexwriterCofig对象
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // 创建一个indexwriter对象
        IndexWriter indexWriter = new IndexWriter(directory, config);

        File file = new File(rawFilePath);
        File[] fileList = file.listFiles();
        for (File singleFile : fileList) {
            Document document = new Document();

            String fileName = singleFile.getName();

            // 文件名域
            Field fileNameField = new TextField("fileName", fileName, Field.Store.YES);

            // 文件大小域
            Field fileSizeField = new LongField("fileSize", singleFile.length(), Field.Store.YES);

            // 文件路径域（不分析、不索引、只存储）
            Field filePathField = new StoredField("filePath", singleFile.getPath());

            // 文件内容
            Field fileContentField = new TextField("fileContent", FileUtil.getFileContent(singleFile), Field.Store.YES);

            document.add(fileNameField);
            document.add(fileSizeField);
            document.add(filePathField);
            document.add(fileContentField);

            // 使用indexwriter对象将document对象写入索引库，此过程进行索引创建。并将索引和document对象写入索引库
            indexWriter.addDocument(document);
        }
        indexWriter.close();
    }

    /**
     * 1）使用Lucene提供Query子类
     *    Query是一个抽象类，lucene提供了很多查询对象，比如TermQuery项精确查询，NumericRangeQuery数字范围查询等。
     *    Query query = new TermQuery(new Term("name", "lucene"));
     *
     * 2）使用QueryParse解析查询表达式
     *    QueryParse会将用户输入的查询表达式解析成Query对象实例。
     *    QueryParser queryParser = new QueryParser("name", new IKAnalyzer());
     *    Query query = queryParser.parse("name:lucene");
     *
     */
    /**
     * @param
     * @return
     * @Description: TODO(查询索引目录中的所有文档)
     */
    public void matchAllDocsQuery(String indexDbPath) throws Exception {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(indexDbPath).toPath());

        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);

        //创建Indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //创建查询条件
        //使用MatchAllDocsQuery查询索引目录中的所有文档
        Query query = new MatchAllDocsQuery();

        //执行查询
        //第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);

        // 结果输出
        output(topDocs, indexSearcher);

        //关闭indexreader对象
        indexReader.close();
    }

    /**
     * TermQuery，通过项查询，TermQuery不使用分析器所以建议匹配不分词的Field域查询，比如订单号、分类ID号等。
     * 指定要查询的域和要查询的关键词。
     *
     * **/
    /**
     * @param
     * @return
     * @Description: TODO(精准查询)
     */
    public void termQuery(String indexDbPath, Term term) throws Exception {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(indexDbPath).toPath());

        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);

        //创建Indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //创建一个TermQuery（精准查询）对象，指定查询的域与查询的关键词
        //创建查询
//        Query query = new TermQuery(new Term("fileName", "apache"));
        Query query = new TermQuery(term);

        //执行查询
        //第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);

        // 结果输出
        output(topDocs, indexSearcher);

        //关闭indexreader对象
        indexReader.close();
    }

    /**
     * @param
     * @return
     * @Description: TODO(可以根据数值范围查询)
     */
    public void numericRangeQuery(String indexDbPath, String field,
                                  long min, long max, boolean minInclusive, boolean maxInclusive) throws Exception {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(indexDbPath).toPath());

        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);

        //创建Indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //创建查询
        //参数：
        //1.域名
        //2.最小值
        //3.最大值
        //4.是否包含最小值
        //5.是否包含最大值
//        Query query = NumericRangeQuery.newLongRange("fileSize", 41L, 2055L, true, true);
        Query query = NumericRangeQuery.newLongRange(field, min, max, minInclusive, maxInclusive);
        //执行查询

        //第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);

        // 结果输出
        output(topDocs, indexSearcher);

        //关闭indexreader对象
        indexReader.close();
    }

    /**
     * Occur.MUST：必须满足此条件，相当于and
     * Occur.SHOULD：应该满足，但是不满足也可以，相当于or
     * Occur.MUST_NOT：必须不满足。相当于not
     *
     * @param
     * @return
     * @Description: TODO(组合查询条件)
     */
    public void booleanQuery(String indexDbPath) throws Exception {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(indexDbPath).toPath());

        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);

        //创建Indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //创建一个布尔查询对象
        BooleanQuery query = new BooleanQuery();
        //创建第一个查询条件
        Query query1 = new TermQuery(new Term("fileName", "linkin.csv"));
        Query query2 = new TermQuery(new Term("fileName", "ok.csv"));
        //组合查询条件
        query.add(query1, BooleanClause.Occur.MUST);
        query.add(query2, BooleanClause.Occur.SHOULD);
        //执行查询

        //第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);

        // 结果输出
        output(topDocs, indexSearcher);

        //关闭indexreader对象
        indexReader.close();
    }

    /**
     * 这个操作需要使用到分析器。建议创建索引时使用的分析器和查询索引时使用的分析器要一致
     *
     * @param
     * @return
     * @Description: TODO(根据查询语法来查询)
     */
    public void queryParser(String indexDbPath, String parserQuery) throws Exception {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(indexDbPath).toPath());

        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);

        //创建Indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //创建queryparser对象
        //第一个参数默认搜索的域
        //第二个参数就是分析器对象
        QueryParser queryParser = new QueryParser("fileName", analyzer);
        //使用默认的域,这里用的是语法，下面会详细讲解一下
        // Query query = queryParser.parse("apache");

        //不使用默认的域，可以自己指定域
        Query query = queryParser.parse(parserQuery);
        //Query query = queryParser.parse("fileContent:apache");

        //第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);

        // 结果输出
        output(topDocs, indexSearcher);

        //关闭indexreader对象
        indexReader.close();
    }

    /**
     * @param
     * @return
     * @Description: TODO(指定多个默认搜索域)
     */
    public void MultiFieldQueryParser(String indexDbPath) throws Exception {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(indexDbPath).toPath());

        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);

        //创建Indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //可以指定默认搜索的域是多个
        String[] fields = {"fileName", "fileContent"};
        //创建一个MulitFiledQueryParser对象
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
        Query query = queryParser.parse("apache");
        System.out.println(query);

        //执行查询
        //第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);

        // 结果输出
        output(topDocs, indexSearcher);

        //关闭indexreader对象
        indexReader.close();
    }

    /**
     * @param
     * @return
     * @Description: TODO(删除全部索引)
     */
    public void deleteAllIndex(String indexDbPath) throws Exception {
        Directory directory = FSDirectory.open(new File(indexDbPath).toPath());
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);
        //删除全部索引
        indexWriter.deleteAll();

        indexWriter.close();
    }

    /**
     * @param
     * @return
     * @Description: TODO(指定查询条件删除)
     */
    public void deleteIndexByQuery(String indexDbPath) throws Exception {
        Directory directory = FSDirectory.open(new File(indexDbPath).toPath());
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);
        //创建一个查询条件
        Query query = new TermQuery(new Term("fileContent", "apache"));
        //根据查询条件删除
        indexWriter.deleteDocuments(query);

        indexWriter.close();
    }

    /**
     * @param
     * @return
     * @Description: TODO(索引库的修改 - 更新的原理就是先删除再添加)
     */
    public void updateIndex(String indexDbPath) throws Exception {

        Directory directory = FSDirectory.open(new File(indexDbPath).toPath());
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        //创建一个Document对象
        Document document = new Document();
        //向document对象中添加域。
        //不同的document可以有不同的域，同一个document可以有相同的域。
        document.add(new TextField("fileXXX", "要更新的文档", Field.Store.YES));
        document.add(new TextField("contentYYY", "简介 Lucene 是一个基于 Java 的全文信息检索工具包。", Field.Store.YES));

        // 先删除再添加
        indexWriter.updateDocument(new Term("fileName", "apache"), document);

        indexWriter.close();
    }

    /**
     * @param
     * @return
     * @Description: TODO(结果输出)
     */
    public void output(TopDocs topDocs, IndexSearcher indexSearcher) throws Exception {
        //查询结果的总条数
        System.out.println("Total number of query results:" + topDocs.totalHits);
        //遍历查询结果
        //topDocs.scoreDocs存储了document对象的id
        //ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            //scoreDoc.doc属性就是document对象的id
            //int doc = scoreDoc.doc;
            //根据document的id找到document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            //文件名称
            System.out.println(document.get("fileName"));
            //文件大小
            System.out.println(document.get("fileSize"));
            //文件路径
            System.out.println(document.get("filePath"));
            //文件内容
            System.out.println(document.get("fileContent"));
            System.out.println("----------------------------------");
        }


    }

}
