package com.creditstate.demo.controller;

import com.creditstate.demo.entity.po.TWebInfoContentVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author weiming.zhu
 * @date 2020/6/1 15:30
 */
@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private SolrClient solrClient;

    @GetMapping("/oldQuery")
    public LinkedHashMap<String, Object> queryId(String searchText, int offset, int limit)
            throws IOException, SolrServerException {
// 创建查询对象
        SolrQuery query = new SolrQuery();
        // q 查询字符串，如果查询所有*:*
        if (StringUtils.isEmpty(searchText)) {
            query.set("q", "*:*");
            query.set("sort", "publishTime desc");
        } else {
//            query.set("q", "keywords:" + escapeQueryChars(searchText));
            // 可以针对不同field分配权重
            query.set("q", "title:" + escapeQueryChars(searchText) + "^2");
            query.set("fq", "content:" + escapeQueryChars(searchText) + "^1.5");
        }
        query.set("fl", "title,publishTime,id,staticPageLink");
        // sort 排序，请注意，如果一个字段没有被索引，那么它是无法排序的
//		query.set("qf","title^1000000.0");
        // start row 分页信息，与mysql的limit的两个参数一致效果
        query.setStart(offset);
        query.setRows(limit);
        // fl 查询哪些结果出来，不写的话，就查询全部，所以我这里就不写了
        // query.set("fl", "");
        // df 默认搜索的域
        // query.set("df", "keywords");
        // ======高亮设置===
        query.addHighlightField("title");// 高亮字段
        query.addHighlightField("content");// 高亮字段
        query.setHighlightSnippets(1);
        query.setHighlightFragsize(75);
        // 开启高亮
        query.setHighlight(true);
        // 前缀
        query.setHighlightSimplePre("<font color=\"red\">");
        // 后缀
        query.setHighlightSimplePost("</font>");
        // 执行搜索
        QueryResponse queryResponse = solrClient.query(query);
        List<TWebInfoContentVo> listvalue2 = queryResponse.getBeans(TWebInfoContentVo.class);
        SolrDocumentList results = queryResponse.getResults();
        // 查询出来的数量
        int numFound = (int) results.getNumFound();
        // 遍历搜索记录
        // 获取高亮信息
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        NamedList list = (NamedList) queryResponse.getResponse().get("highlighting");
        for (int i = 0; i < listvalue2.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (!StringUtils.isEmpty(searchText) && listvalue2.get(i).getId().equals(list.getName(j).toString())) {
                    String title = list.getVal(j).toString();
                    if (!title.equals("{}") && title.contains("title=")) {
                        int index = title.length();
                        if (title.contains("content=")) {
                            index = title.indexOf("content=");
                        }
                        title = title.substring(8, index - 1);
                        title = title.replaceFirst("]}", "");
                        title = title.replaceFirst("]", "");
                        listvalue2.get(i).setTitle(title);
                    }
                    if (!title.equals("{}") && title.contains("content=")) {
                        int start = title.indexOf("content=") + 9;
                        int end = title.length();
//						if (end - start <= 150) {
//							title = title.substring(start, end);
//						}
//						else if(end - start >= 150){
//                            title = title.substring(title.indexOf("<font"), title.indexOf("</font>") + 20);
//                        }
                        title = title.substring(start, end);
                        title = title.replaceFirst("]}", "");
                        //过滤首字符字符判定
                        String[] filter = new String[]{"《", "》", "；", ";", "’", "‘", "\'", "\"", "”", "“", "）", "（", "：", "？", "、", "。", "，", ".", ",", "{", "}", "[", "]", "~", "`", " ", "\r"};
                        for (int k = 0; k < filter.length; k++) {
                            String t = (String) title.subSequence(0, 1);
                            for (int g = 0; g < 3; g++) {
                                if (filter[k].equals(t)) {
                                    title = title.substring(1, title.length());
                                }
                            }
                        }
                        title = title.trim();
                        listvalue2.get(i).setSummary(title);
                    }
                    break;
                }
            }
            if (listvalue2.get(i).getPublishTime() != null) {
                listvalue2.get(i).setcPublishTime(sdf.format(listvalue2.get(i).getPublishTime()));
            }
//            listvalue2.get(i).setContent("");
        }
        LinkedHashMap<String, Object> mapvalue = new LinkedHashMap<String, Object>();
        mapvalue.put("list", listvalue2);
        mapvalue.put("count", numFound);
        return mapvalue;
    }

    public static String escapeQueryChars(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
                    || c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
                    || c == '*' || c == '?' || c == '|' || c == '&' || c == ';' || c == '/'
                    || Character.isWhitespace(c)) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

}
