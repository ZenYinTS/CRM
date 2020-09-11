package com.crm.service.impl;

import com.crm.domain.Monthattend;
import com.crm.domain.PageResult;
import com.crm.mapper.MonthattendMapper;
import com.crm.query.MonthAttendQueryObject;
import com.crm.service.IMonthAttendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonthAttendImpl implements IMonthAttendService {

    @Autowired
    private MonthattendMapper monthattendDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return monthattendDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Monthattend record) {
        return monthattendDao.insert(record);
    }

    @Override
    public Monthattend selectByPrimaryKey(Long id) {
        return monthattendDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Monthattend> selectAll() {
        return monthattendDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Monthattend record) {
        return monthattendDao.updateByPrimaryKey(record);
    }

    @Override
    public PageResult queryForPage(MonthAttendQueryObject queryObject) {
        Long total = monthattendDao.getTotalRows(queryObject);
        if (total==0)
            return new PageResult();
        List<Monthattend> monthattendList = monthattendDao.queryForPage(queryObject);
        return new PageResult(total,monthattendList);
    }
}
