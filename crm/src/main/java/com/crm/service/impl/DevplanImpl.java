package com.crm.service.impl;

import com.crm.domain.Devplan;
import com.crm.domain.PageResult;
import com.crm.mapper.DevplanMapper;
import com.crm.query.DevplanQueryObject;
import com.crm.service.IDevplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevplanImpl implements IDevplanService {

    @Autowired
    private DevplanMapper devplanDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return devplanDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Devplan record) {
        return devplanDao.insert(record);
    }

    @Override
    public Devplan selectByPrimaryKey(Long id) {
        return devplanDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Devplan> selectAll() {
        return devplanDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Devplan record) {
        return devplanDao.updateByPrimaryKey(record);
    }

    @Override
    public PageResult queryForPage(DevplanQueryObject queryObject) {
        Long total = devplanDao.getTotalForPage(queryObject);
        if (total == 0)
            return new PageResult();
        List<Devplan> result = devplanDao.queryForPage(queryObject);
        return new PageResult(total,result);
    }
}
