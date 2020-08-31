package com.crm.service.impl;

import com.crm.domain.Dictdetail;
import com.crm.domain.PageResult;
import com.crm.mapper.DictdetailMapper;
import com.crm.query.QueryObject;
import com.crm.service.IDictdetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictdetailImpl implements IDictdetailService {
    @Autowired
    private DictdetailMapper dictdetailDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return dictdetailDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Dictdetail record) {
        return dictdetailDao.insert(record);
    }

    @Override
    public Dictdetail selectByPrimaryKey(Long id) {
        return dictdetailDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Dictdetail> selectAll() {
        return dictdetailDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Dictdetail record) {
        return dictdetailDao.updateByPrimaryKey(record);
    }

    @Override
    public List<Dictdetail> selectByPid(Long pid) {
        return dictdetailDao.selectByPid(pid);
    }
}
