package com.creditstate.demo.controller;

import com.creditstate.demo.entity.Account;
import com.creditstate.demo.service.SolrService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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

//    @GetMapping("/selectAccount")
//    public Account addAccount() {
//        SolrQuery solrQuery  = new SolrQuery();
//
//    }
}
