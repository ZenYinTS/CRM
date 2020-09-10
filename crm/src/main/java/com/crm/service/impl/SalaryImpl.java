package com.crm.service.impl;

import com.crm.domain.PageResult;
import com.crm.domain.Salarytable;
import com.crm.mapper.SalarytableMapper;
import com.crm.query.SalaryQueryObject;
import com.crm.service.ISalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryImpl implements ISalaryService {

    @Autowired
    private SalarytableMapper salarytableDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return salarytableDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Salarytable record) {
        return salarytableDao.insert(record);
    }

    @Override
    public Salarytable selectByPrimaryKey(Long id) {
        return salarytableDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Salarytable> selectAll() {
        return salarytableDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Salarytable record) {
        return salarytableDao.updateByPrimaryKey(record);
    }

    @Override
    public PageResult queryForPage(SalaryQueryObject queryObject) {
        Long total = salarytableDao.getTotalRows(queryObject);
        if (total == 0){
            return new PageResult();
        }
        List<Salarytable> salarytableList = salarytableDao.queryForPage(queryObject);
        return new PageResult(total,salarytableList);
    }
}
