---
typora-root-url: typora文档图片

---

## solr安装、配置与集成

### 简介

Solr是一个独立的[企业级搜索](https://baike.baidu.com/item/企业级搜索)应用服务器，它对外提供类似于Web-service的API接口。用户可以通过http请求，向搜索引擎服务器提交一定格式的XML文件，生成索引；也可以通过Http Get操作提出查找请求，并得到XML格式的返回结果。

### 安装与启动

solr官网 https://lucene.apache.org/solr/

下载页 https://lucene.apache.org/solr/downloads.html

截止本文创建时间（2020-04-26）,最新版本为8.5.1。本文会采用solr7的最新版本7.7.2，本文示例操作系统为windows。

我们下载[solr-7.7.2.zip](https://www.apache.org/dyn/closer.lua/lucene/solr/7.7.2/solr-7.7.2.zip)并解压到非中文目录下。

cmd命令行到安装目录下的/bin目录，使用 solr.cmd start 命令可以直接运行。从运行日志可以看到，solr默认端口是8983。

我们可以打开浏览器，输入地址 http://localhost:8983/solr 到后台管理页面，说明安装和启动成功。

### 配置core

如果我们此时就编写一个项目链接solr是链接不通的。spring boot报错： (SOLR-11637) *Error* *from* *server* *at* *http://localhost:8983/solr:* *Expected* *mime* *type* application/o eam but got text/html.

因为我们需要一个core核心提供数据存储索引。

在后台管理页面找到侧边栏core admin，添加core页面如下：

![add core](QQ截图20200426152137.jpg)

但是这样直接add core依然不通过，他会提示你

没有找到solrconfig.xml和schema.xml。它的要求比较特别

- 要新建一个core就要新建一个文件夹存放该Core的基础配置文件
- 新建文件夹的名字任取
- 所有core文件夹都要放到一个文件夹下面,这个文件夹叫做solr_home,用户可以自己修改这个目录,也可以用默认的solr-7.2.1\server\solr
- 每个core文件都有基础的配置文件,需要复制到core文件夹下面
- solr会给我们提供默认的基础的配置文件

我可以从solr提供的样板开始：

(1)在solr-7.2.1\server\solr 下面新建一个文件夹,名字任取,这里取名test
 (2)将solr-7.2.1\server\solr\configsets\\_default 下面的***conf\***文件夹复制到刚创建的test文件夹下

现在我们回到上面截图的界面：

第一个输入框是Core的名字，任取即可，这里我们填test
第二个输入框是Core的目录，我们也填test
第三个输入框是data的目录,创建后就可以在test下面看到该文件夹,等会会上图展示。这里值保持不动即可,也可以自己指定。
第四个输入框是solr的配置信息的目录,里面各种配置信息,默认不要修改
第五个输入框是保存数据的配置xml,但是solr7.2.1之后已经没有这个文件了。取而代之的是test/confing目录下的managed-schema文件,把改文件改成solrconfig.xml也能跑起来。

再次点击add core就可以创建成功了。

### 界面

![](QQ截图20200426154752.jpg)

### 集成spring boot

我们创建一个solrdemo项目：

![](QQ图片20200426155826.png)

这里我们使用的springboot版本是2.2.6.RELEASE，2.0以上的区别不会太大。

需要的包：

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-solr</artifactId>
</dependency>
```

当然如果你有lombok编程习惯推荐添加lombok，方便你理解本文。

##### 实体类：Account

```java
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Field("id")
    private Integer id;
    @Field("username")
    private String username;
    @Field("password")
    private String password;
}
```

注意：

- 实体类必须实现可序列化接口，因为要在网络上传输
- @Field注解是org.apache.solr.client.solrj.beans包下的，它的值决定了引擎中的字段名称

##### Service接口

```java
public interface SolrService {
    
}
```

这里我们就写一个空接口，后期我们会添加。实现类也写一个空的，放在impl下

##### Controller

```java
@RestController
@RequestMapping("/solr")
public class SolrController {
    @Autowired
    private SolrClient solrClient;
    
    
}
```

##### application.yml

```yml
server:
  port: 9999
spring:
  application:
    name: solrdemo
  data:
    solr:
      host: http://localhost:8983/solr/test
```

可见，在host后面我们添加了core的名称

### 操作数据

我们将通过这个solrdemo展示solr数据的增删改查。

##### 新增数据

```java
@GetMapping("/addAccount")
public String addAccount() {
    try {
  		solrClient.addBean(Account.builder().id(1).password("123456").username("test").build());
        solrClient.commit();
        return "success";
    } catch (IOException | SolrServerException e) {
        e.printStackTrace();
    }
    return "failed";
}
```

addBean方法可以添加一个序列化的bean，他将被转换成SolrInputDocument对象，转换不成功会抛出异常。

commit执行显式提交，导致将待处理的SolrInputDocument对象提交到索引中

从检索界面可以查到，索引中已经有了一条数据：

![](QQ截图20200426163322.jpg)

插入就成功了。

##### 查询数据

如果你已经阅读并使用了“界面”章节的内容，你将比较容易理解接下来的部分。

查数据有两种方式，当我们设置的字段有id的情况下，可以使用solrClient自带的getById(String id)方法。值得注意的是，它的参数类型是String，我们的类中id是Integer，但是我们提交到索引中的时候，id字段的值被转换为String了。

```java
@GetMapping("/getAccountById/{id}")
public String getAccountById(@PathVariable("id") String id) {
    try {
        //根据id查询内容
        SolrDocument solrDocument = solrClient.getById(id);
        return solrDocument.getFieldValueMap().toString();
    } catch (SolrServerException | IOException e) {
        e.printStackTrace();
    }
    return "failed";
}
```

当然我们在条件多而复杂的情况下，我们可以定制与管理界面一样的查询条件：

```java
SolrQuery solrQuery = new SolrQuery();
// 查询条件
solrQuery.set("q", "username:" + account.getUsername());
// 筛选条件
solrQuery.set("fq", "id:" + account.getId());
// 排序方式
solrQuery.setSort("id", SolrQuery.ORDER.asc);
//设置查询的条数
solrQuery.setRows(page.getSize());
//设置查询的开始
solrQuery.setStart(page.getIndex());
//设置高亮
solrQuery.setHighlight(true);
//设置高亮的字段
solrQuery.addHighlightField("name");
//设置高亮的样式
solrQuery.setHighlightSimplePre("<font color='red'>");
solrQuery.setHighlightSimplePost("</font>");
System.out.println(solrQuery);
QueryResponse response = solrClient.query(solrQuery);
//返回高亮显示结果
Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
//response.getResults();查询返回的结果
SolrDocumentList documentList = response.getResults();

return documentList.toString();
```

本文暂不讨论查询条件搭配和性能的问题，实在是因为没有样本数据测试。











参考博文：

[solr7.4配置core创建及分词配置](https://www.jianshu.com/p/4e86df9532d9)

[windows安装solr及详细介绍说明](https://blog.csdn.net/qq_30764991/article/details/81607116)

[Spring Boot整合solr，手把手教你使用solr](https://blog.csdn.net/weixin_42129558/article/details/82682265)

[Solr7.2.1添加Core](https://blog.csdn.net/a897180673/article/details/79403952)

