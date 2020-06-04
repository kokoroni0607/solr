## solr实现定时增量同步

### 所需材料

solr安装包，

ik分词器jar包 ik-analyzer-solr7-7.x.jar 下载：https://repo1.maven.org/maven2/com/github/magese/ik-analyzer-solr7/7.x/ik-analyzer-solr7-7.x.jar，

solr-dataimport-scheduler(Solr7.x).jar 下载： https://download.csdn.net/download/weixin_44539024/12366490

### 安装

solr官网下载https://lucene.apache.org/solr/downloads.html

建议使用7.x版本，本文使用7.7.2版本，可在https://archive.apache.org/dist/lucene/solr/页面中找到

linux环境请下载solr-7.7.2.tgz，windows下载solr-7.7.2.zip，本文将介绍linux版本下的安装和配置

解压到任意文件夹。

```shell
tar -xvf solr-7.7.2.tgz
```

进入到加压后的文件夹/solr-7.7.1,执行一下命令启动solr

```shell
bin/solr start -force
```

solr 默认以8983端口启动 可在启动时更改端口 -p 8982
 -force 为以root用户启动时必须加的命令 若不是root用户启动则无需加该命令

如果报文出现了:

```
Warning: Available entropy is low. As a result, use of the UUIDField, SSL, or any other features that require
RNG might not work properly. To check for the amount of available entropy, use 'cat /proc/sys/kernel/random/entropy_avail'.

Waiting up to 180 seconds to see Solr running on port 8983 [\] 
```

说明solr正在启动，等到出现：

```
Started Solr server on port 8983 (pid=10049). Happy searching!
```

说明启动成功

可以再浏览器中输入ip:8983/solr/index.html进入后台管理页面

### 配置索引core

在solr-7.7.2/server/solr 文件夹中创建一个文件夹credit_wuhu-content

将solr-7.7.2/server/solr/configsets/_default下的conf文件夹复制到credit_wuhu-content里去

此时配置还没完成，我们需要在bin目录下重启solr:

```
./solr restart -force
```

重启完成后进入solr后台管理页面，选择右侧菜单的core admin，添加core:

第一个输入框是Core的名字，任取即可，这里我们填credit_wuhu-content

第二个输入框是Core的目录，我们也填credit_wuhu-content

其他默认即可。确认之后下面的core下拉菜单中就会有我们刚刚添加的credit_wuhu-content，core配置就算完成了

### IK分词器

这里需要一个ik-analyzer的jar包ik-analyzer-solr7-7.x.jar，(https://repo1.maven.org/maven2/com/github/magese/ik-analyzer-solr7/7.x/ik-analyzer-solr7-7.x.jar)

将该jar包放到solr-7.7.2/server/solr-webapp/webapp/WEB-INF/lib下

找到solr-7.7.2/server/solr/credit_wuhu-content/conf下managed-schema文件，编辑

在最后一个fieldType后面添加：

```xml
            <!-- Pre-analyzed field type, allows inserting arbitrary token streams and stored values. -->
    <fieldType name="preanalyzed" class="solr.PreAnalyzedField">
      <!-- PreAnalyzedField's builtin index analyzer just decodes the pre-analyzed token stream. -->
      <analyzer type="query">
        <tokenizer class="solr.WhitespaceTokenizerFactory"/>
      </analyzer>
    </fieldType>
	<!-- ik分词器 -->
    <fieldType name="text_ik" class="solr.TextField">
      <analyzer type="index">
        <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="false" conf="ik.conf"/>
        <filter class="solr.LowerCaseFilterFactory"/>
      </analyzer>
      <analyzer type="query">
        <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="true" conf="ik.conf"/>
        <filter class="solr.LowerCaseFilterFactory"/>
      </analyzer>
    </fieldType>
```





一个preanalyzed类一个text_ik类

重启solr就可以了

我们可以到admin后台看一下credit_wuhu-content下的analysis，可以看到我们可以对检索词进行分词了

这里的分词规则暂不介绍，可自行百度

### 导入Oracle数据

##### 加入相关jar包

进入solr-7.7.2/dist下，复制：

solr-dataimporthandler-extras-7.7.2.jar
solr-dataimporthandler-7.7.2.jar

到solr-7.7.2/server/solr-webapp/webapp/WEB-INF/lib下

将oracle驱动jar包 ojdbc6-11.1.0.7.0.jar 也导到该目录下

##### 配置Core下的solrconfig.xml文件

找到solr-7.7.2/server/solr/credit_wuhu-content/conf下solrconfig.xml文件，编辑

61行添加

```xml
    <requestHandler name="/dataimport" class="org.apache.solr.handler.dataimport.DataImportHandler">
    　　<lst name="defaults">
    　　　　<str name="config">data-config.xml</str>
    　　</lst>
    </requestHandler> 
```

##### 创建data-config.xml(或者直接把提供的data-config.xml复制进去)

```xml
<dataConfig>
    <dataSource driver="oracle.jdbc.driver.OracleDriver" url="jdbc:oracle:thin:@172.24.254.91:1521/hzpt" user="u_hzpt" password="zzvKEcbyWVbQecaU" stripHTML="true"/>
    <document>
       <entity name="t_Web_Info_Content" pk="ID" 
	   query="SELECT ID  ,TITLE ,CONTENT,PUBLISH_TIME,STATIC_PAGE_LINK  FROM t_Web_Info_Content  WHERE DEL_FLAG = '0' AND status = '2'" 
	   deltaImportQuery="SELECT ID ,TITLE ,CONTENT,PUBLISH_TIME,STATIC_PAGE_LINK  FROM t_Web_Info_Content where ID = '${dih.delta.ID}' and DEL_FLAG = '0' AND STATUS = '2' "
            deltaQuery="SELECT ID  from t_Web_Info_Content where UPDATE_TIME > to_date('${dih.last_index_time}','yyyy-MM-dd hh24:mi:ss')"
            transformer="ClobTransformer,HTMLStripTransformer">
				 <!-- 每一个field映射着数据库中列与文档中的域，column是数据库列，name是solr的域(必须是在managed-schema文件中配置过的域才行) -->
            <field column="ID" name="ID"/>
			<field column="ID" name="id"/>
			<field column="TITLE" name="title"/>
			<field column="CONTENT" name="content" clob="true" stripHTML="true" />
			<field column="PUBLISH_TIME" name="publishTime"/>
			<field column="STATIC_PAGE_LINK" name="staticPageLink"/>
	   </entity>
    </document>
</dataConfig>
```

需要配置的有数据源和document，document包括query、deltaImportQuery、deltaQuery和transformer以及实体类的字段映射为域field

##### 在managed-schema中添加域

在managed-schema中找到uniqueKey标签，改为：

```xml
<uniqueKey>ID</uniqueKey>
```

然后添加field。理论上来说data-config添加了几个field，这里就需要添加几个field。但是很奇怪的是旧版本的schema中有很多不知道的字段，在不清楚功能的情况下，只能全部复制过来。

找到field name="id"一行，将该行替换为：

```xml
    <field name="id" type="string" indexed="true" stored="true" required="true" multiValued="false" /> 
    <field name="ID" type="string" indexed="true" stored="true" required="true" /> 
    <field name="pre" type="preanalyzed" indexed="true" stored="true"/>
    <field name="sku" type="text_en_splitting_tight" indexed="true" stored="true" omitNorms="true"/>
    <field name="name" type="text_general" indexed="true" stored="true"/>
    <field name="manu" type="text_general" indexed="true" stored="true" omitNorms="true"/>
    <field name="cat" type="string" indexed="true" stored="true" multiValued="true"/>
    <field name="features" type="text_general" indexed="true" stored="true" multiValued="true"/>
    <field name="includes" type="text_general" indexed="true" stored="true" termVectors="true" termPositions="true" termOffsets="true" />

    <field name="weight" type="pfloat" indexed="true" stored="true"/>
    <field name="price"  type="pfloat" indexed="true" stored="true"/>
    <field name="popularity" type="pint" indexed="true" stored="true" />
    <field name="inStock" type="boolean" indexed="true" stored="true" />
    <field name="content" type="text_ik" indexed="true" stored="true" /> 
    <field name="title" type="text_ik" indexed="true" stored="true"/> 
    <field name="store" type="location" indexed="true" stored="true"/>
    <field name="keywords" type="text_ik" indexed="true" stored="true"  multiValued="true"/> 
    <field name="publishTime" type="pdate" indexed="true" stored="true"/> 
    <field name="PublishTime" type="pdate" indexed="true" stored="true"/> 
    <field name="cPublishTime" type="pdate" indexed="true" stored="true"/> 
    <field name="staticPageLink" type="string" indexed="true" stored="true"/>     
```

我们再次重启solr

然而此时在solr里还是没有数据的，我们需要在credit_wuhu-content下dataimport菜单中

command选择full-import

勾选commit

emtity选择表名t_Web_info_Content

然后点击execute，solr将会根据数据源去抽取数据

可以点击后边的刷新按钮查看进度，如果抽取完成，右边的状态标识将会为绿色

### 定时增量索引

##### jar包

我们需要一个dataimport-scheduler.jar包，但是这实际上已经不是apache官方推荐的jar包，而是某些热心网友编写的jar，网上找的有许多问题。建议使用本文提供的jar包

jar包复制到solr-7.7.2/server/solr-webapp/webapp/WEB-INF/lib下

注意：这里jar包不强制修改文件名，如果出现了缺包或者找不到类问题，可以改包名为dihs.jar再尝试

##### web.xml

找到solr-7.7.2/server/solr-webapp/webapp/WEB-INF下的web.xml，编辑

```xml
  <listener>
    <listener-class>org.apache.solr.handler.dataimport.scheduler.ApplicationListener</listener-class>
  </listener>
```

添加到所有servlet的前面

##### dataimport.properties

在solr-7.7.2/server/solr目录下新建conf文件夹，在该文件夹下创建dataimport.properties文件

```properties
#  to sync or not to sync    
#  1 - active; anything else - inactive    
# 这里的配置不用修改      
syncEnabled=1
#  which cores to schedule    
#  in a multi-core environment you can decide which cores you want syncronized    
#  leave empty or comment it out if using single-core deployment    
#  修改成你所使用的core，我这里是我自定义的core1    
syncCores=credit_wuhu-content
#  solr server name or IP address    
#  [defaults to localhost if empty]    
#  这个一般都是localhost不会变      
server=localhost
#  solr server port    
#  [defaults to 80 if empty]    
#  安装solr的tomcat端口，如果你使用的是默认的端口，就不用改了，否则你懂的      
port=8983
#  application name/context    
#  [defaults to current ServletContextListener's context (app) name]    
#  这里默认不改      
webapp=solr
#  URL params [mandatory]    
#  remainder of URL    
#  这里要改成下面的形式      
params=/dataimport?command=delta-import&clean=false&commit=true
#  schedule interval     
#  [defaults to 30 if empty]    
#  这里是设置定时任务的，单位是分钟，也就是多长时间你检测一次数据同步，根据项目需求修改      
#  开始测试的时候为了方便看到效果，时间可以设置短一点,我这是60秒  
interval=5
#  重做索引的时间间隔，单位分钟，默认7200，即5天;     
#  为空,为0,或者注释掉:表示永不重做索引    
reBuildIndexInterval=1440
#  重做索引的参数    
reBuildIndexParams=/dataimport?command=full-import&clean=true&commit=true
#  重做索引时间间隔的计时开始时间，第一次真正执行的时间=reBuildIndexBeginTime+reBuildIndexInterval*60*1000；    
#  两种格式：2012-04-11 03:10:00 或者  03:10:00，后一种会自动补全日期部分为服务启动时的日期    
reBuildIndexBeginTime=23:50:00
```

然后重启solr

但是我们怎么验证是否成功了呢？似乎只有等待数据库中存在新数据之后才能看到效果



至此，大体的任务就完成了，关于solr的admin界面、与springboot集成可以看我的github：https://github.com/kokoroni0607/solr



参考博文

https://www.cnblogs.com/miye/p/10690234.html

https://blog.csdn.net/weixin_44539024/article/details/105786437