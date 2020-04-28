package com.creditstate.demo.controller;

import com.creditstate.demo.entity.common.Page;
import com.creditstate.demo.entity.po.Account;
import com.creditstate.demo.service.SolrService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author weiming.zhu
 * @date 2020/4/26 14:15
 */
@RestController
@RequestMapping("/solr")
public class SolrController {
    @Autowired
    private SolrService solrService;

    @Autowired
    private SolrClient solrClient;

    /**
     * 添加账户
     * @return
     */
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

    /**
     * 根据id查询账户
     * @param id
     * @return
     */
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

    /**
     * 多条件查询账户
     * @param account
     * @param page
     * @return
     */
    @GetMapping("/selectAccount")
    public String selectAccount(Account account, Page page) {
        try {
            SolrQuery solrQuery = new SolrQuery();
            handleQuery(account, solrQuery);

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
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "failed";
    }

    private void handleQuery(Account account, SolrQuery solrQuery) {
        if (StringUtils.isNotBlank(account.getUsername())) {
            solrQuery.set("q", "username:" + account.getUsername());
            if (account.getId() != null && account.getId() > 0) {
                solrQuery.set("fq", "id:" + account.getId());
            }
        }else{
            if (account.getId() != null && account.getId() > 0) {
                solrQuery.set("q", "id:" + account.getId());
            }
        }

    }
}
