# 基于LUCENE实现本地文件的全文检索
## 版本信息
lucene-core-5.5.5 IKAnalyzer-5.X

## LUCENE查询语法
1、基础的查询语法，关键词查询：

　　　域名+“：”+搜索的关键字

　　　例如：content:java

2、范围查询

　　　域名+“:”+[最小值 TO 最大值]

　　　例如：size:[1 TO 1000]

　　　范围查询在lucene中支持数值类型，不支持字符串类型。在solr中支持字符串类型。

3、组合条件查询

　1）+条件1 +条件2：两个条件之间是并且的关系and

　　　例如：+filename:apache +content:apache

　2）+条件1 条件2：必须满足第一个条件，应该满足第二个条件

　　　例如：+filename:apache content:apache

　3）条件1 条件2：两个条件满足其一即可。

　　　例如：filename:apache content:apache

　4）-条件1 条件2：必须不满足条件1，要满足条件2

　　　例如：-filename:apache content:apache
 
 | Occur.MUST 查询条件必须满足，相当于and | +（加号） |
 | ------ | ------ |
 | Occur.SHOULD 查询条件可选，相当于or | 空（不用符号） |
 | Occur.MUST_NOT 查询条件不能满足，相当于not非 | -（减号） |
 
 4、组合条件查询
  
  　　条件1 AND 条件2
  
  　　条件1 OR 条件2
  
  　　条件1 NOT 条件2
  
  