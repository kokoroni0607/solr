package com.creditstate.demo.service.impl;

import com.creditstate.demo.entity.Account;
import com.creditstate.demo.service.SolrService;
import org.springframework.stereotype.Service;

/**
 * @author weiming.zhu
 * @date 2020/4/26 14:10
 */
@Service
public class SolrServiceImpl implements SolrService {
    @Override
    public Account addAccount() {
        return Account.builder().id(1).password("123456").username("test").build();
    }
}
