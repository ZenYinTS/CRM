package com.crm.service.impl;

import com.crm.mapper.DictcatalogMapper;
import com.crm.service.IDictcatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictcatalogService implements IDictcatalogService {

    @Autowired
    private DictcatalogMapper dictcatalogDao;

    @Override
    public List selectAll() {
        return dictcatalogDao.selectAll();
    }
}
